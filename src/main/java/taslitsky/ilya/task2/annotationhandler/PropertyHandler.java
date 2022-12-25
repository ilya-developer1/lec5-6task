package taslitsky.ilya.task2.annotationhandler;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import taslitsky.ilya.task2.annotation.Property;
import taslitsky.ilya.task2.exception.UnsupportedTypeException;
import taslitsky.ilya.task2.exception.WrongFormatException;


public class PropertyHandler {
  private static final String DEFAULT = "";
  private Property property;
  private Field field;
  private String name;
  private String format;

  public PropertyHandler(Field field) {
    this.field = field;
    field.setAccessible(true);
    property = field.getAnnotation(Property.class);
    name = property.name().equals(DEFAULT) ?
        field.getName() : property.name();
    format = property.format();
  }

  public String getName() {
    return name;
  }

  public <T> T parse(String value) throws WrongFormatException, UnsupportedTypeException {
    Class<?> fieldType = field.getType();
     if(fieldType.equals(String.class)) {
      return (T) value;
    } else if(fieldType.equals(Instant.class) && validate(value, format)) {
      return (T) convertToInstant(value);
    } else if((fieldType.equals(Integer.class) || fieldType.equals(int.class))
         && isInteger(value)) {
        return (T) new Integer(value);
    }  else {
      throw new UnsupportedTypeException();
    }
  }

  private Instant convertToInstant(String value) {
    return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format, Locale.GERMANY))
        .atZone(ZoneId.of("Europe/Kiev")).toInstant();
  }


  private boolean validate(String date, String pattern) throws WrongFormatException {
    String regex = pattern.replaceAll("\\w", "\\\\d")
        .replace(".", "\\.");
    if (!date.matches(regex)) {
      throw new WrongFormatException();
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setLenient(false);
    try {
      sdf.parse(date);
    } catch (ParseException e) {
      throw new WrongFormatException();
    }
    return true;
  }


  private boolean isInteger(String s) {
    return isInteger(s,10);
  }

  private boolean isInteger(String s, int radix) {
    if(s.isEmpty()) return false;
    for(int i = 0; i < s.length(); i++) {
      if(i == 0 && s.charAt(i) == '-') {
        if(s.length() == 1) return false;
        else continue;
      }
      if(Character.digit(s.charAt(i),radix) < 0) return false;
    }
    return true;
  }
}
