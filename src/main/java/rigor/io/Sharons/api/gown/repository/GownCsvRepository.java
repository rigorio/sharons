package rigor.io.Sharons.api.gown.repository;

import com.univocity.parsers.common.processor.BeanListProcessor;
import rigor.io.Sharons.api.csv.BeanUtils;
import rigor.io.Sharons.api.gown.Gown;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GownCsvRepository {

  public List<Gown> getGowns() {
    try {
      BeanUtils beanUtils = new BeanUtils();
      File file = new File("D:\\Projects\\sharons\\src\\main\\resources\\gowns.csv");
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

}
