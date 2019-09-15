package rigor.io.Sharons.api;

import rigor.io.Sharons.api.gown.Gown;
import rigor.io.Sharons.api.gown.repository.GownRepository;
import rigor.io.Sharons.utils.IdProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GownHandler implements GownService {
  private GownRepository gownRepository;

  public GownHandler(GownRepository gownRepository) {
    this.gownRepository = gownRepository;
  }

  @Override
  public List<Gown> all() {
    return gownRepository.all();
  }

  @Override
  public boolean add(Gown gown) {
    gown.setId(IdProvider.getId());
    return gownRepository.add(gown);
  }

  @Override
  public Gown edit(Gown gown) {
    return null;
  }

  @Override
  public Gown delete(Gown gown) {
    return null;
  }

  @Override
  public Optional<Gown> findById(Long id) {
    return gownRepository.findById(id);
  }

  @Override
  public List<Gown> costsMoreThan(Double price) {
    return all()
        .stream()
        .filter(gown -> gown.getPrice() >= price)
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> costsLessThan(Double price) {
    return all().stream()
        .filter(gown -> gown.getPrice() <= price)
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> dueOn(String date) {
    return all().stream()
        .filter(gown -> gown.getDueDate().equals(date))
        .collect(Collectors.toList());
  }

  @Override
  public List<Gown> rentedOn(String date) {
    return all().stream()
        .filter(gown -> gown.getDateRented().equals(date))
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Long id) {
    gownRepository.delete(id);
  }
}
