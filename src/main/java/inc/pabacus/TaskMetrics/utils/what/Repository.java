package inc.pabacus.TaskMetrics.utils.what;

import java.util.List;
import java.util.Optional;

/**
 * @param <T>
 * @param <ID>
 * @see FlatFileRepository
 */
public interface Repository<T, ID> {
  List<T> findAll();

  <S extends T> S save(S var1);

  <S extends T> List<S> saveAll(List<S> var1);

  Optional<T> findById(ID var1);

  boolean existsById(ID var1);

  Iterable<T> findAllById(Iterable<ID> var1);

  long count();

  void deleteById(ID var1);

  void delete(T var1);

  void deleteAll(Iterable<? extends T> var1);

  void deleteAll();
}
