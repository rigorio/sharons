package inc.pabacus.TaskMetrics.desktop.tasks;

import javafx.beans.property.SimpleStringProperty;

public class ToDo {
    protected SimpleStringProperty toDo;

    public ToDo(String toDo) {
        this.toDo = new SimpleStringProperty(toDo);
    }

    public String getToDo() {
        return toDo.get();
    }

    public SimpleStringProperty toDoProperty() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo.set(toDo);
    }

    public ToDo() {
    }
}

