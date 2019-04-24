package inc.pabacus.TaskMetrics.api.tasks;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @see TaskWebRepository
 */
public interface TaskRepository {
  
  List<Task> findAll();

  
  List<Task> findAll(Sort sort);

  
  List<Task> findAllById(Iterable<Long> iterable);

  
  <S extends Task> List<S> saveAll(Iterable<S> iterable);

  
  void flush();

  
  <S extends Task> S saveAndFlush(S s);

  
  void deleteInBatch(Iterable<Task> iterable);

  
  void deleteAllInBatch();

  
  Task getOne(Long aLong);

  
  <S extends Task> List<S> findAll(Example<S> example);

  
  <S extends Task> List<S> findAll(Example<S> example, Sort sort);

  
  Page<Task> findAll(Pageable pageable);

  
  <S extends Task> S save(S s);

  
  Optional<Task> findById(Long aLong);

  
  boolean existsById(Long aLong);

  
  long count();

  
  void deleteById(Long aLong);

  
  void delete(Task task);

  
  void deleteAll(Iterable<? extends Task> iterable);

  
  void deleteAll();

  
  <S extends Task> Optional<S> findOne(Example<S> example);

  
  <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable);

  
  <S extends Task> long count(Example<S> example);

  
  <S extends Task> boolean exists(Example<S> example);
}
