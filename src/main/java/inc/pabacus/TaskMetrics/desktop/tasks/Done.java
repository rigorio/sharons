package inc.pabacus.TaskMetrics.desktop.tasks;

import javafx.beans.property.SimpleStringProperty;

public class Done {
    protected SimpleStringProperty done;

    public Done(String done) {
        this.done = new SimpleStringProperty(done);
    }

    public String getDone() {
        return done.get();
    }

    public SimpleStringProperty doneProperty() {
        return done;
    }

    public void setDone(String done) {
        this.done.set(done);
    }

    public Done() {
    }
}
