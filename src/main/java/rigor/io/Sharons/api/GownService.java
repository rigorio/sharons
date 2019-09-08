package rigor.io.Sharons.api;

import rigor.io.Sharons.api.gown.Gown;

import java.util.List;
import java.util.Optional;

/**
 * @see rigor.io.Sharons.api.gown.repository.GownListRepository
 * @see rigor.io.Sharons.api.gown.repository.GownCsvRepository
 */
public interface GownService {
  List<Gown> all();

  Gown add(Gown gown);

  Gown edit(Gown gown);

  Gown delete(Gown gown);

  Optional<Gown> findById(Long id);

  List<Gown> costsMoreThan(Double price);

  List<Gown> costsLessThan(Double price);

  List<Gown> dueOn(String date);

  List<Gown> rentedOn(String date);

}
