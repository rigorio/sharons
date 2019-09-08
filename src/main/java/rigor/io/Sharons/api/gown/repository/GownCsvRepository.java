package rigor.io.Sharons.api.gown.repository;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import rigor.io.Sharons.api.csv.BeanUtils;
import rigor.io.Sharons.api.csv.BeanWriter;
import rigor.io.Sharons.api.gown.Gown;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GownCsvRepository implements GownRepository {

  private BeanUtils beanUtils;

  public GownCsvRepository() {
    beanUtils = new BeanUtils();
  }

  public List<Gown> getGowns() {
    try {
      BeanUtils beanUtils = new BeanUtils();
      File file = new File("D:\\Projects\\sharons\\gowns.csv");
//      InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("gowns.csv");
      InputStream inputStream = new FileInputStream(file);
      Reader reader = beanUtils.getReader(inputStream);

      BeanListProcessor<Gown> beanListProcessor = new BeanListProcessor<>(Gown.class);
      List<Gown> gowns = beanUtils.getBeans(reader, beanListProcessor);

      return gowns;

    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  @Override
  public List<Gown> all() {
    return getGowns();
  }

  @Override
  public Optional<Gown> findById(Long id) {
    return getGowns().stream()
        .filter(gown -> gown.getId().equals(id))
        .findAny();
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public void delete(Gown gown) {

  }

  @Override
  public boolean add(Gown gown) {
    System.out.println("nanda");
    try {
      String[] headers = {"id", "name", "description", "price", "date rented", "due date"};
      FileOutputStream outputStream = new FileOutputStream("gowns.csv");
      List<Gown> all = getGowns();
      all.add(gown);
      System.out.println(all.toString());
      BeanWriterProcessor<Gown> writerProcessor = new BeanWriterProcessor<>(Gown.class);
      BeanWriter beanWriter = BeanWriter.builder()
          .beanWriterProcessor(writerProcessor)
          .headers(headers)
          .outputStream(outputStream)
          .records(all)
          .build();
      beanUtils.writeBeans(beanWriter);
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean addall(List<Gown> gowns) {
    return false;
  }

  @Override
  public List<Gown> setList(List<Gown> gowns) {
    return null;
  }
}
