package inc.pabacus.TaskMetrics.api.project;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

  List<Project> findAll();


  List<Project> findAll(Sort sort);


  List<Project> findAllById(Iterable<Long> iterable);


  <S extends Project> List<S> saveAll(Iterable<S> iterable);


  void flush();


  <S extends Project> S saveAndFlush(S s);


  void deleteInBatch(Iterable<Project> iterable);


  void deleteAllInBatch();


  Project getOne(Long aLong);


  <S extends Project> List<S> findAll(Example<S> example);


  <S extends Project> List<S> findAll(Example<S> example, Sort sort);


  Page<Project> findAll(Pageable pageable);


  <S extends Project> S save(S s);


  Optional<Project> findById(Long aLong);


  boolean existsById(Long aLong);


  long count();


  void deleteById(Long aLong);


  void delete(Project project);


  void deleteAll(Iterable<? extends Project> iterable);


  void deleteAll();


  <S extends Project> Optional<S> findOne(Example<S> example);


  <S extends Project> Page<S> findAll(Example<S> example, Pageable pageable);


  <S extends Project> long count(Example<S> example);


  <S extends Project> boolean exists(Example<S> example);

}
