package taslitsky.ilya.task2.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesPropertiesReader {
  private final String PROPERTIES_FILE_NAME;
  private final Properties property;

  public ResourcesPropertiesReader(String PROPERTIES_FILE_NAME) {
    this.PROPERTIES_FILE_NAME = PROPERTIES_FILE_NAME;
    property = new Properties();
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
      property.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getProperty(String key) {
    return property.getProperty(key);
  }
}
