package rigor.io.Sharons.api.csv;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import lombok.Builder;
import lombok.Data;

import java.io.OutputStream;
import java.util.Collection;

@Data
@Builder
public class BeanWriter {

  private BeanWriterProcessor<?> beanWriterProcessor;
  private String[] headers;
  private OutputStream outputStream;
  private Collection<?> records;

}
