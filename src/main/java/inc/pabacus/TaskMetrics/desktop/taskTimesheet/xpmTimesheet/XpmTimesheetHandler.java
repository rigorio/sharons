package inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class XpmTimesheetHandler implements XpmTimesheetService {
  private XpmTimesheetRepository repository;

  public XpmTimesheetHandler() {
    repository = new XpmTimesheetWebRepository();
  }

  @Override
  public XpmTimesheet saveXpmTimesheet(XpmTimesheet xpmTimesheet) {
    return null;
  }

  @Override
  public Optional<XpmTimesheet> getXpmTimesheet(Long id) {
    return Optional.empty();
  }

  @Override
  public List<XpmTimesheet> getAllgetXpmTimesheet() {
    return null;
  }

  @Override
  public List<XpmTimesheetAdapter> getAllgetXpmTimesheetAdapter() {
    return repository.findAll().stream()
        .map(XpmTimesheetAdapter::new)
        .collect(Collectors.toList());
  }
}
