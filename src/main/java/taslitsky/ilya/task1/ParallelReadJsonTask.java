package taslitsky.ilya.task1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ParallelReadJsonTask {

  public static void main(String[] args) {
    ParallelReadJsonTask parallelReadJsonTask = new ParallelReadJsonTask();
    System.out.println("Start....");
    long start = System.currentTimeMillis();
    int coreSize = 8;
    parallelReadJsonTask.solutionAsync("task1", "task1Result", coreSize);
    long end = System.currentTimeMillis();
    System.out.println("Time of execution for " + coreSize + " cores = " + (end - start) / 1000.0);
  }
  public void solutionAsync(String inputDirName, String outputDirName, int coreSize) {
    // Get list of json file
    List<String> resourceFiles;
    try {
      resourceFiles = getResourceFiles(inputDirName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ConcurrentMap<String, Double> statistics = new ConcurrentHashMap<>();
    ExecutorService executorService = Executors.newFixedThreadPool(coreSize);
    List<CompletableFuture<Void>> completableFutures = new ArrayList<>(resourceFiles.size());

    // Run completable futures
    for(String resourceFile : resourceFiles) {
      CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() ->
              new ReadFileTask().start(resourceFile), executorService)
          .thenAcceptAsync(result -> addResultToMap(statistics, result), executorService);

      completableFutures.add(completableFuture);
    }

    // Wait all futures to end
    CompletableFuture
        .allOf(completableFutures.toArray(new CompletableFuture[0]))
        .join();

    // write xml result file
    writeXmlStatistics(outputDirName, statistics);
    executorService.shutdown();
  }

  private void addResultToMap(ConcurrentMap<String, Double> statistics, Map<String, Double> result) {
    for (Map.Entry<String, Double> entry:
    result.entrySet()) {
      String fineType = entry.getKey();
      Double fineAmount = entry.getValue();
      if(statistics.containsKey(fineType)) {
        statistics.put(fineType, statistics.get(fineType) + fineAmount);
      } else {
        statistics.put(fineType, fineAmount);
      }
    }
  }

  private List<String> getResourceFiles(String path) throws IOException {
    List<String> filenames = new ArrayList<>();
    try (
        InputStream in = getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
      String resource;

      while ((resource = br.readLine()) != null) {
        filenames.add(path + "/" +resource);
      }
    }

    return filenames;
  }

  private InputStream getResourceAsStream(String resource) {
    final InputStream in
        = getContextClassLoader().getResourceAsStream(resource);

    return in == null ? getClass().getResourceAsStream(resource) : in;
  }

  private ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  private void writeXmlStatistics(String outputDirName, Map<String, Double> statistics) {
    XMLStreamWriter writer = null;
    String outputPath = "src/main/resources/" + outputDirName + "/result.xml";
    try {
      writer = XMLOutputFactory.newFactory()
          .createXMLStreamWriter(new FileOutputStream(outputPath));

      writer.writeStartElement("FineStat");

      for (Map.Entry<String, Double> fine :
          statistics.entrySet()) {
        writer.writeStartElement("Fine");
        writer.writeAttribute(fine.getKey(), String.valueOf(fine.getValue()));
        writer.writeEndElement();
      }

      writer.writeEndElement();
    } catch (XMLStreamException | FileNotFoundException e) {
      e.printStackTrace();
    }

    if (writer != null) {
      try {
        writer.close();
      } catch (XMLStreamException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
