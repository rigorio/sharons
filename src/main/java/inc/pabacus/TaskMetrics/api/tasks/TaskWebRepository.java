package inc.pabacus.TaskMetrics.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskWebRepository implements TaskRepository {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public TaskWebRepository() {

  }

  @Override
  public List<Task> findAll() {
    List<Task> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/tasks")
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<Task>>() {});

    } catch (IOException e) {
      e.printStackTrace();
      return tasks;
    }
    return tasks;
  }

  @Override
  public List<Task> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Task> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public List<Task> findAllById(Iterable<Long> iterable) {
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
  public void delete(Task task) {

  }

  @Override
  public void deleteAll(Iterable<? extends Task> iterable) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends Task> S save(S s) {
    try {
      String jsonString = mapper.writeValueAsString(s);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/task")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      Task task;
      task = mapper.readValue(responseBody.string(), new TypeReference<Task>() {});
      s.setId(task.getId());
    } catch (IOException e) {
      e.printStackTrace();
      return s;
    }
    return s;
  }

  @Override
  public <S extends Task> List<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public Optional<Task> findById(Long id) {
    return findAll().stream()
        .filter(task -> task.getId().equals(id))
        .findAny();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Task> S saveAndFlush(S s) {
    return null;
  }

  @Override
  public void deleteInBatch(Iterable<Task> iterable) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Task getOne(Long aLong) {
    return null;
  }

  @Override
  public <S extends Task> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Task> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Task> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Task> boolean exists(Example<S> example) {
    return false;
  }
}
