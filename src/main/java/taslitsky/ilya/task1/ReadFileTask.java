package taslitsky.ilya.task1;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.HashMap;
import java.util.Map;


public class ReadFileTask {
  public Map<String, Double> start(String path) {
    Map<String, Double> statistics = new HashMap<>();
    String fullPath = "src/main/resources/" + path;
    try (
        InputStream inputStream = Files.newInputStream(Path.of(fullPath));
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
    ) {
      reader.beginArray();
      while (reader.hasNext()) {
        Fine fine = new Gson().fromJson(reader, Fine.class);
        calcStatistics(statistics, fine.getType(), fine.getAmount());
      }
      reader.endArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return statistics;
  }

  private void calcStatistics(Map<String, Double> statistics, String type, Double amount) {
    if(statistics.containsKey(type)) {
      statistics.put(type, statistics.get(type) + amount);
      return;
    }
    statistics.put(type, amount);
  }
}
