package inc.pabacus.TaskMetrics.api.tasks;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskWebRepository implements TaskRepository {

  private OkHttpClient client = new OkHttpClient();

  public TaskWebRepository() {

  }

  @Override
  public List<Task> findAll() {

    Call call = client.newCall(new Request.Builder()
                                   .url("")
                                   .build()
    );



    return null;
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
    return null;
  }

  @Override
  public <S extends Task> List<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public Optional<Task> findById(Long aLong) {
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
