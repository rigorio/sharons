package rigor.io.Sharons.api.gown.repository;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import rigor.io.Sharons.api.csv.BeanUtils;
import rigor.io.Sharons.api.csv.BeanWriter;
import rigor.io.Sharons.api.gown.entities.Gown;
import rigor.io.Sharons.utils.DesktopWriter;

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
      File file = new File(DesktopWriter.path() + "gowns.csv");
//      InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("gowns.csv");
      file.createNewFile();
      InputStream inputStream = new FileInputStream(file);
      Reader reader = beanUtils.getReader(inputStream);

      BeanListProcessor<Gown> beanListProcessor = new BeanListProcessor<>(Gown.class);
      List<Gown> gowns = beanUtils.getBeans(reader, beanListProcessor);
      System.out.println(gowns.toString());
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
    List<Gown> gowns = getGowns();
    boolean removed = gowns.removeIf(gown -> gown.getId().equals(id));
    System.out.println("Was it removed?");
    System.out.println(removed);
    try {
      writeGowns(gowns);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(Gown gown) {

  }

  @Override
  public boolean add(Gown gown) {
    try {
      List<Gown> all = getGowns();
      all.add(gown);
      writeGowns(all);
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean update(Gown gown) {
    delete(gown.getId());
    return add(gown);
  }

  private void writeGowns(List<Gown> all) throws FileNotFoundException {
    String[] headers = {"id", "name", "description", "price", "status", "date rented", "due date", "dateReturned", "contact"};
    FileOutputStream outputStream = new FileOutputStream(DesktopWriter.path() + "gowns.csv");
    BeanWriterProcessor<Gown> writerProcessor = new BeanWriterProcessor<>(Gown.class);
    BeanWriter beanWriter = BeanWriter.builder()
        .beanWriterProcessor(writerProcessor)
        .headers(headers)
        .outputStream(outputStream)
        .records(all)
        .build();
    beanUtils.writeBeans(beanWriter);
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
