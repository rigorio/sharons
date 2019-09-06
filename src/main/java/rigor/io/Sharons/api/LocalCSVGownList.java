package rigor.io.Sharons.api;

import rigor.io.Sharons.api.gown.Gown;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocalCSVGownList implements GownRepository {

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
  public List<Gown> costsMoreThan(Double price) {
    return gowns.stream()
        .filter(gown -> gown.getPrice() >= price)
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> costsLessThan(Double price) {
    return gowns.stream()
        .filter(gown -> gown.getPrice() <= price)
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> dueOn(String date) {
    return gowns.stream()
        .filter(gown -> gown.getDueDate().equals(date))
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> rentedOn(String date) {
    return gowns.stream()
        .filter(gown -> gown.getDateRented().equals(date))
        .collect(Collectors.toList());
  }
}
