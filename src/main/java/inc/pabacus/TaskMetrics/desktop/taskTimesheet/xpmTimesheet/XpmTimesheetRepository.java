package inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface XpmTimesheetRepository {
  List<XpmTimesheet> findAll();


  List<XpmTimesheet> findAll(Sort sort);


  List<XpmTimesheet> findAllById(Iterable<Long> iterable);


  <S extends XpmTimesheet> List<S> saveAll(Iterable<S> iterable);


  void flush();


  <S extends XpmTimesheet> S saveAndFlush(S s);


  void deleteInBatch(Iterable<XpmTimesheet> iterable);


  void deleteAllInBatch();


  XpmTimesheet getOne(Long aLong);


  <S extends XpmTimesheet> List<S> findAll(Example<S> example);


  <S extends XpmTimesheet> List<S> findAll(Example<S> example, Sort sort);


  Page<XpmTimesheet> findAll(Pageable pageable);


  <S extends XpmTimesheet> S save(S s);


  Optional<XpmTimesheet> findById(Long aLong);


  boolean existsById(Long aLong);


  long count();


  void deleteById(Long aLong);


  void delete(XpmTimesheet project);


  void deleteAll(Iterable<? extends XpmTimesheet> iterable);


  void deleteAll();


  <S extends XpmTimesheet> Optional<S> findOne(Example<S> example);


  <S extends XpmTimesheet> Page<S> findAll(Example<S> example, Pageable pageable);


  <S extends XpmTimesheet> long count(Example<S> example);


  <S extends XpmTimesheet> boolean exists(Example<S> example);
}
