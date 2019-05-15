package inc.pabacus.TaskMetrics.api.project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectWebRepository implements ProjectRepository {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public ProjectWebRepository() {
  }

  @Override
  public List<Project> findAll() {
    List<Project> projects = new ArrayList<>();

    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/projects")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      projects = mapper.readValue(jsonString, new TypeReference<List<Project>>() {});

    } catch (IOException e) {
      e.printStackTrace();
      return projects;
    }
    return projects;
  }

  @Override
  public List<Project> findAll(Sort sort) {
    return null;
  }

  @Override
  public List<Project> findAllById(Iterable<Long> iterable) {
    return null;
  }

  @Override
  public <S extends Project> List<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Project> S saveAndFlush(S s) {
    return null;
  }

  @Override
  public void deleteInBatch(Iterable<Project> iterable) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Project getOne(Long aLong) {
    return null;
  }

  @Override
  public <S extends Project> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Project> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public Page<Project> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Project> S save(S s) {
    try {
      String jsonString = mapper.writeValueAsString(s);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/project")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      Project task;
      task = mapper.readValue(responseBody.string(), new TypeReference<Project>() {});
      s.setId(task.getId());
    } catch (IOException e) {
      e.printStackTrace();
      return s;
    }
    return s;
  }

  @Override
  public Optional<Project> findById(Long id) {
    return findAll().stream()
        .filter(project -> project.getId().equals(id))
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
  public void delete(Project project) {

  }

  @Override
  public void deleteAll(Iterable<? extends Project> iterable) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends Project> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Project> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Project> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Project> boolean exists(Example<S> example) {
    return false;
  }
}
