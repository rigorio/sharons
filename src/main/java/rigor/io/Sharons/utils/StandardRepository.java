package rigor.io.Sharons.utils;

import java.util.List;
import java.util.Optional;

public interface StandardRepository<T> {
  List<T> all();

  Optional<T> findById(Long id);

  void delete(Long id);

  void delete(T gown);

  boolean add(T gown);

  boolean addall(List<T> gowns);

  List<T> setList(List<T> gowns);
}
