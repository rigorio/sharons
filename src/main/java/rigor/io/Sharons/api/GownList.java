package rigor.io.Sharons.api;

import rigor.io.Sharons.api.gown.Gown;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GownList implements GownRepository {

  private static List<Gown> gowns = new ArrayList<>();

  @Override
  public List<Gown> all() {
    return new ArrayList<>(gowns);
  }

  @Override
  public Optional<Gown> findById(Long id) {
    return gowns.stream()
        .filter(gown -> gown.getId().equals(id))
        .findAny()
        .map(Gown::new); // wow
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public void delete(Gown gown) {

  }
}
