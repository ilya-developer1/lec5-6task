package taslitsky.ilya;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

import taslitsky.ilya.task2.exception.AnnotationNotFoundException;
import taslitsky.ilya.task2.exception.UnsupportedTypeException;
import taslitsky.ilya.task2.exception.WrongFormatException;
import taslitsky.ilya.task2.model.Person;
import taslitsky.ilya.task2.util.UtilTask;


public class UtilTaskTest {

    @Test
    public void loadFromResourcesTest_Success()
        throws UnsupportedTypeException, WrongFormatException, AnnotationNotFoundException {
        // GIVEN
        final String fileName = "UtilTaskTestResources/testSuccess.properties";
        final String format = "dd.MM.yyyy HH:mm";
        final String time = "29.11.2022 18:30";
        final Person expected = new Person("value1", 10, convertToInstant(format, time));

        // WHEN
        final Person actual = UtilTask.loadFromProperties(Person.class, fileName);

        // THEN
        assertEquals(expected, actual);
    }

    @Test
    public void loadFromResourcesTest_shouldThrowUnSupportedTypeException() {
        // GIVEN
        final String fileName = "UtilTaskTestResources/testUnsupportedFormatException.properties";

        // THEN
        assertThrows(UnsupportedTypeException.class, ()
            -> UtilTask.loadFromProperties(Person.class, fileName));
    }

    @Test
    public void loadFromResourcesTest_shouldThrowWrongFormatException() {
        // GIVEN
        final String fileName = "UtilTaskTestResources/testWrongFormatException.properties";

        // THEN
        assertThrows(WrongFormatException.class, ()
            -> UtilTask.loadFromProperties(Person.class, fileName));
    }

    @Test
    public void loadFromResourcesTest_shouldThrowAnnotationNotFoundException() {
        // GIVEN
        final String fileName = "UtilTaskTestResources/testSuccess.properties";


        // THEN
        assertThrows(AnnotationNotFoundException.class, ()
            -> UtilTask.loadFromProperties(Object.class, fileName));
    }

    private Instant convertToInstant(String format, String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format, Locale.GERMANY))
            .atZone(ZoneId.of("Europe/Kiev")).toInstant();
    }
}
