package inc.pabacus.TaskMetrics.api.timesheet;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @see DailyLogWebRepository
 */
public interface DailyLogRepository {

  List<DailyLog> findAll();


  List<DailyLog> findAll(Sort sort);


  List<DailyLog> findAllById(Iterable<Long> iterable);


  <S extends DailyLog> List<S> saveAll(Iterable<S> iterable);


  void flush();


  <S extends DailyLog> S saveAndFlush(S s);


  void deleteInBatch(Iterable<DailyLog> iterable);


  void deleteAllInBatch();


  DailyLog getOne(Long aLong);


  <S extends DailyLog> List<S> findAll(Example<S> example);


  <S extends DailyLog> List<S> findAll(Example<S> example, Sort sort);


  Page<DailyLog> findAll(Pageable pageable);


  <S extends DailyLog> S save(S s);


  Optional<DailyLog> findById(Long aLong);


  boolean existsById(Long aLong);


  long count();


  void deleteById(Long aLong);


  void delete(DailyLog dailyLog);


  void deleteAll(Iterable<? extends DailyLog> iterable);


  void deleteAll();


  <S extends DailyLog> Optional<S> findOne(Example<S> example);


  <S extends DailyLog> Page<S> findAll(Example<S> example, Pageable pageable);


  <S extends DailyLog> long count(Example<S> example);


  <S extends DailyLog> boolean exists(Example<S> example);


}
