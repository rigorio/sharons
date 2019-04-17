package inc.pabacus.TaskMetrics.desktop.tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ToDo {

  private StringProperty title;
  private StringProperty description;
  private StringProperty status;
  private StringProperty timeSpent;

  public ToDo(String title, String description, String status) {
    this.title = new SimpleStringProperty(title);
    this.description = new SimpleStringProperty(description);
    this.status = new SimpleStringProperty(status);
  }

  public ToDo(String title, String description, String status, String timeSpent) {
    this.title = new SimpleStringProperty(title);
    this.description = new SimpleStringProperty(description);
    this.status = new SimpleStringProperty(status);
    this.timeSpent = new SimpleStringProperty(timeSpent);
  }

  public String getTimeSpent() {
    return timeSpent.get();
  }

  public StringProperty timeSpentProperty() {
    return timeSpent;
  }

  public void setTimeSpent(String timeSpent) {
    this.timeSpent.set(timeSpent);
  }

  public String getTitle() {
    return title.get();
  }

  public StringProperty titleProperty() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty getStatus() {
    return status;
  }

  public String statusProperty() {
    return status.get();
  }

  public void setStatus(String status) {
    this.status.set(status);
  }

}

