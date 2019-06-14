package inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet;

import java.util.List;
import java.util.Optional;

public interface XpmTimesheetService {
    XpmTimesheet saveXpmTimesheet(XpmTimesheet xpmTimesheet);

    Optional<XpmTimesheet> getXpmTimesheet(Long id);

    List<XpmTimesheet> getAllgetXpmTimesheet();

    List<XpmTimesheetAdapter> getAllgetXpmTimesheetAdapter();

}
