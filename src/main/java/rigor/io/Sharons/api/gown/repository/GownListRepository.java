package rigor.io.Sharons.api.gown.repository;

import rigor.io.Sharons.api.gown.entities.Gown;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GownListRepository implements GownRepository {

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

  @Override
  public boolean add(Gown gown) {
    return gowns.add(gown);
  }

  @Override
  public boolean addall(List<Gown> gowns) {
    return gowns.addAll(gowns);
  }

  @Override
  public List<Gown> setList(List<Gown> gowns) {
    GownListRepository.gowns = new ArrayList<>(gowns);
    return gowns;
  }
}
