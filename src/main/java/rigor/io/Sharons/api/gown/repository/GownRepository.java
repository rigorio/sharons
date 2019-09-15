package rigor.io.Sharons.api.gown.repository;

import rigor.io.Sharons.api.gown.entities.Gown;

import java.util.List;
import java.util.Optional;

/**
 * @see rigor.io.Sharons.api.gown.repository.GownListRepository
 * @see rigor.io.Sharons.api.gown.repository.GownCsvRepository
 */
public interface GownRepository {
  List<Gown> all();

  Optional<Gown> findById(Long id);

  void delete(Long id);

  void delete(Gown gown);

  boolean add(Gown gown);

  boolean addall(List<Gown> gowns);

  List<Gown> setList(List<Gown> gowns);
}
