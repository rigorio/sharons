package inc.pabacus.TaskMetrics.utils.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Why I initially chose to utilize flat files instead of a database via Spring JPA:
 * I wanted to avoid unnecessary overhead. Spring JPA is a dependency which would
 * also require a few other dependencies for the database including Spring itself,
 * which basically meant more storage and memory being taken up by the app. By making
 * use of flat files, this is avoided and we end up with a more lightweight approach.
 * Although time may prove otherwise, and so an implementation may be done to make
 * it utilize Spring JPA instead - which is why this implements a Repository very
 * similar to Spring JPA Repositories.
 * TODO: to future dev
 * Create an implementation that will allow multiple sets of data in one file
 * (e.g. having tasks, user login details, and etc. in one single file)
 * and make use of JIMFS
 *
 * @see <a href="https://github.com/google/jimfs"> JIMFS </a>
 */
public class FlatFileRepository implements Repository<Map<String, Object>, Long> {

  private ObjectMapper mapper;
  private FlatFileSettings settings;

  public FlatFileRepository(FlatFileSettings settings) {
    mapper = new ObjectMapper(); // creating this object is considerably heavy, maybe look for a lighter implementation
    this.settings = settings;
  }

  @Override
  public List<Map<String, Object>> findAll() {
    File file = settings.getFile(settings.getFileName());
    if (file.length() == 0)
      return new ArrayList<>();
    Map<String, Object> map = null;
    try {
      String fileName = file.getAbsoluteFile().toString();
      String decryptJson = settings.transformMessage(fileName);
      map = mapper.readValue(decryptJson, new TypeReference<Map<String, Object>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>(Collections.singletonList(map));
  }

  @Override
  public <S extends Map<String, Object>> S save(S map) {
    File file = settings.getFile(settings.getFileName());
    String content = "";
    try {
      Map<String, Object> data = new HashMap<>(map); // remove additional step?
      content = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      // log me or return to avoid writing to file?
    }
    String fileName = file.getAbsoluteFile().toString();
    settings.write(content, fileName);
    return map;
  }

  @Override
  public <S extends Map<String, Object>> List<S> saveAll(List<S> data) {
    File file = settings.getFile(settings.getFileName());
    String content = "";
    try {
      content = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      // log me or return to avoid writing to file?
    }
    String fileName = file.getAbsoluteFile().toString();
    settings.write(content, fileName);

    return data;
  }

  @Override
  public Optional<Map<String, Object>> findById(Long var1) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long var1) {
    return false;
  }

  @Override
  public Iterable<Map<String, Object>> findAllById(Iterable<Long> var1) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long var1) {

  }

  @Override
  public void delete(Map<String, Object> map) {

  }

  @Override
  public void deleteAll(Iterable<? extends Map<String, Object>> var1) {

  }

  @Override
  public void deleteAll() {
    File file = settings.getFile(settings.getFileName());
    file.delete();
  }

}
