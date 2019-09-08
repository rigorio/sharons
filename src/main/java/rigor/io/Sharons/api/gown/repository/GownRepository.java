package rigor.io.Sharons.api.gown.repository;

import rigor.io.Sharons.api.gown.Gown;

import java.util.List;
import java.util.Optional;

public interface GownRepository {
  List<Gown> all();

  Optional<Gown> findById(Long id);

  void delete(Long id);

  void delete(Gown gown);
}
