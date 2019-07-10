package inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XpmTimesheetWebRepository implements XpmTimesheetRepository {
  private static final Logger logger = Logger.getLogger(XpmTimesheetWebRepository.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public XpmTimesheetWebRepository() {
    HOST = new HostConfig().getHost();
  }

  @Override
  public List<XpmTimesheet> findAll() {
    List<XpmTimesheet> xpmTimesheets = new ArrayList<>();

    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      xpmTimesheets = mapper.readValue(jsonString, new TypeReference<List<XpmTimesheet>>() {});

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return xpmTimesheets;
  }

  @Override
  public List<XpmTimesheet> findAll(Sort sort) {
    return null;
  }

  @Override
  public List<XpmTimesheet> findAllById(Iterable<Long> iterable) {
    return null;
  }

  @Override
  public <S extends XpmTimesheet> List<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends XpmTimesheet> S saveAndFlush(S s) {
    return null;
  }

  @Override
  public void deleteInBatch(Iterable<XpmTimesheet> iterable) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public XpmTimesheet getOne(Long aLong) {
    return null;
  }

  @Override
  public <S extends XpmTimesheet> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends XpmTimesheet> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public Page<XpmTimesheet> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends XpmTimesheet> S save(S s) {
    try {
      String jsonString = mapper.writeValueAsString(s);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      XpmTimesheet timesheet;
      timesheet = mapper.readValue(responseBody.string(), new TypeReference<XpmTimesheet>() {});
      s.setId(timesheet.getId());
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return s;
  }

  @Override
  public Optional<XpmTimesheet> findById(Long id) {
    return findAll().stream()
        .filter(xpmTimesheet -> xpmTimesheet.getId().equals(id))
        .findAny();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {

  }

  @Override
  public void delete(XpmTimesheet project) {

  }

  @Override
  public void deleteAll(Iterable<? extends XpmTimesheet> iterable) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends XpmTimesheet> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends XpmTimesheet> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends XpmTimesheet> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends XpmTimesheet> boolean exists(Example<S> example) {
    return false;
  }
}
