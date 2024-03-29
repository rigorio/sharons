package rigor.io.Sharons.api.gown;

import rigor.io.Sharons.api.gown.entities.Gown;

import java.util.List;
import java.util.Optional;

/**
 * @see GownHandler
 */
public interface GownService {
  List<Gown> all();

  boolean add(Gown gown);

  boolean edit(Gown gown);

  Gown delete(Gown gown);

  Optional<Gown> findById(Long id);

  List<Gown> costsMoreThan(Double price);

  List<Gown> costsLessThan(Double price);

  List<Gown> dueOn(String date);

  List<Gown> rentedOn(String date);

  void delete(Long id);
}
