package inc.pabacus.TaskMetrics.api.timesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DailyLogWebRepository implements DailyLogRepository {

  private static final Logger logger = Logger.getLogger(DailyLogWebRepository.class);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public DailyLogWebRepository() {
    HOST = new HostConfig().getHost();
  }

  @Override
  public List<DailyLog> findAll() {
    List<DailyLog> dailyLogs = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/logs")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());

      String jsonString = call.execute().body().string();
      dailyLogs = mapper.readValue(jsonString, new TypeReference<List<DailyLog>>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return dailyLogs;
  }

  @Override
  public List<DailyLog> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<DailyLog> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public List<DailyLog> findAllById(Iterable<Long> iterable) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {

  }

  @Override
  public void delete(DailyLog dailyLog) {

  }

  @Override
  public void deleteAll(Iterable<? extends DailyLog> iterable) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends DailyLog> S save(S s) {
    return null;
  }

  @Override
  public <S extends DailyLog> List<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public Optional<DailyLog> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends DailyLog> S saveAndFlush(S s) {
    return null;
  }

  @Override
  public void deleteInBatch(Iterable<DailyLog> iterable) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public DailyLog getOne(Long aLong) {
    return null;
  }

  @Override
  public <S extends DailyLog> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends DailyLog> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends DailyLog> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends DailyLog> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends DailyLog> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends DailyLog> boolean exists(Example<S> example) {
    return false;
  }
}
