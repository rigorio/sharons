package inc.pabacus.TaskMetrics.desktop.tasks;

import javafx.beans.property.SimpleStringProperty;

public class InProgress {
    public SimpleStringProperty inProgress;

    public InProgress(String inProgress) {
        this.inProgress = new SimpleStringProperty(inProgress);
    }

    public String getInProgress() {
        return inProgress.get();
    }

    public void setInProgress(String inProgress) {
        this.inProgress.set(inProgress);
    }

    public SimpleStringProperty inProgressProperty() {
        return inProgress;
    }

    public InProgress() {
    }
}
