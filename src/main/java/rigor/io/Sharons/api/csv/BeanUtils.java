package rigor.io.Sharons.api.csv;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.*;
import java.util.List;

/**
 * Utilities used to write and read beans to and from a CSV file and List
 */
public class BeanUtils {

  /**
   * Converts a csv file to a {@link List<T>}
   *
   * @param file          location of csv
   * @param beanProcessor beanProcessor to be used to map a csv to a bean
   * @param <T>           Bean Type to use for conversion
   * @return
   */
  public <T> List<T> getBeans(String file, BeanListProcessor beanProcessor) throws IOException {
    BeanListProcessor<T> processor = beanProcessor;

    CsvParser parser = createCsvParser(processor);
    Reader reader = getReader(file);
    parser.parse(reader);
    reader.close();
    return processor.getBeans();
  }

  /**
   * This should allow converting an in memory csv file
   *
   * @param reader        reader to permit in memory csv files
   * @param beanProcessor beanProcessor to be used to map a csv to a bean
   * @param <T>           Bean Type to use for conversion
   * @return
   */
  public <T> List<T> getBeans(Reader reader, BeanListProcessor beanProcessor) throws IOException {
    BeanListProcessor<T> processor = beanProcessor;

    CsvParser parser = createCsvParser(processor);
    Reader r = reader;
    parser.parse(r);
    r.close();
    return processor.getBeans();
  }

  /**
   * Writes {@link java.util.Collection} of Beans into a csv file
   *
   * @param beanWriter utilities to be used to write a bean into a csv file
   */
  public void writeBeans(BeanWriter beanWriter) {
    CsvWriterSettings settings = getCsvWriterSettings(beanWriter);
    OutputStreamWriter writer = new OutputStreamWriter(beanWriter.getOutputStream());
    CsvWriter csvWriter = new CsvWriter(writer, settings);
    csvWriter.writeHeaders();
    csvWriter.processRecords(beanWriter.getRecords());
    csvWriter.close();
  }

  /**
   * creates an {@link InputStreamReader} for the given path
   *
   * @param path where the file can be found
   * @return an {@link InputStreamReader} for the given <b>path</b>
   */
  public Reader getReader(String path) {
    try {
      File file = new File(path);
      checkIfFileExists(file);
      FileInputStream inputStream = new FileInputStream(file);
      return new InputStreamReader(inputStream, "UTF-8");
    } catch (UnsupportedEncodingException | FileNotFoundException e) {
      throw new IllegalStateException("Unable to read input", e);
    }
  }

  /**
   * creates an {@link InputStreamReader} for the given inputStream
   *
   * @param inputStream
   * @return an {@link InputStreamReader} for the given {@link InputStream}
   */
  public Reader getReader(InputStream inputStream) {
    try {
      return new InputStreamReader(inputStream, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Unable to read input", e);
    }
  }

  private void checkIfFileExists(File file) throws FileNotFoundException {
    if (!file.exists())
      throw new FileNotFoundException(file.getName() + " does not exist");
  }

  private CsvWriterSettings getCsvWriterSettings(BeanWriter beanWriter) {
    CsvWriterSettings settings = new CsvWriterSettings();
    settings.setRowWriterProcessor(beanWriter.getBeanWriterProcessor());
    settings.setHeaders(beanWriter.getHeaders());
    return settings;
  }

  private <T> CsvParser createCsvParser(BeanListProcessor<T> ruleProcessor) {
    CsvParserSettings settings = new CsvParserSettings();
    settings.detectFormatAutomatically();
    settings.setProcessor(ruleProcessor);
    settings.setHeaderExtractionEnabled(true);
    return new CsvParser(settings);
  }
}
