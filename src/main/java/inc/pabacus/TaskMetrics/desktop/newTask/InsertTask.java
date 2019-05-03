package inc.pabacus.TaskMetrics.desktop.newTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertTask {

    private Long id;
    private String title;
    private String description;
    private Boolean billable;
    private String status;

    public InsertTask(String title, String description, Boolean billable, String status) {
        this.title = title;
        this.description = description;
        this.billable = billable;
        this.status = status;
    }
}
