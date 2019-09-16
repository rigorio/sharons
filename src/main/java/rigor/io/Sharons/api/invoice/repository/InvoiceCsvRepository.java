package rigor.io.Sharons.api.invoice.repository;

import rigor.io.Sharons.api.invoice.entities.Invoice;

import java.util.List;
import java.util.Optional;

public class InvoiceCsvRepository implements InvoiceRepository {
  @Override
  public List<Invoice> all() {
    return null;
  }

  @Override
  public Optional<Invoice> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public void delete(Invoice gown) {

  }

  @Override
  public boolean add(Invoice gown) {
    return false;
  }

  @Override
  public boolean update(Invoice invoice) {
    return false;
  }

  @Override
  public boolean addall(List<Invoice> gowns) {
    return false;
  }

  @Override
  public List<Invoice> setList(List<Invoice> gowns) {
    return null;
  }
}
