package taslitsky.ilya.task2.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import taslitsky.ilya.task2.annotation.Property;
import taslitsky.ilya.task2.annotationhandler.PropertyHandler;
import taslitsky.ilya.task2.exception.AnnotationNotFoundException;
import taslitsky.ilya.task2.exception.UnsupportedTypeException;
import taslitsky.ilya.task2.exception.WrongFormatException;

public class UtilTask {
    public static <T> T loadFromProperties(Class<T> cls, String propertyFileName)
        throws WrongFormatException, UnsupportedTypeException, AnnotationNotFoundException {
        boolean isAnnotationFound = false;
        Field[] fields = cls.getDeclaredFields();
        ResourcesPropertiesReader propertiesReader = new ResourcesPropertiesReader(propertyFileName);
        T object;
        try {
            object = cls.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Property.class)) {
                    isAnnotationFound = true;
                    PropertyHandler propertyHandler = new PropertyHandler(field);
                    String name = propertyHandler.getName();
                    String value = propertiesReader.getProperty(name);
                    field.set(object, propertyHandler.parse(value));
                }
            }
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
            IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(!isAnnotationFound) {
            throw new AnnotationNotFoundException();
        }
        return object;
    }
}
