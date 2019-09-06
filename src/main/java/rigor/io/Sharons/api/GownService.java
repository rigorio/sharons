package rigor.io.Sharons.api;

import rigor.io.Sharons.api.gown.Gown;

import java.util.List;
import java.util.Optional;

public interface GownService {
  List<Gown> all();

  Optional<Gown> findById(Long id);

  List<Gown> costsMoreThan(Double price);

  List<Gown> costsLessThan(Double price);

  List<Gown> dueOn(String date);

  List<Gown> rentedOn(String date);

}
