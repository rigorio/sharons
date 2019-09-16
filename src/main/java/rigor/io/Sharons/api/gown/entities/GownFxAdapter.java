package rigor.io.Sharons.api.gown.entities;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GownFxAdapter {
  private LongProperty id;
  private StringProperty name;
  private StringProperty description;
  private DoubleProperty price;
  private StringProperty dateRented;
  private StringProperty dueDate;
  private StringProperty status;

  public GownFxAdapter(Gown gown) {
    Long id = gown.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String name = gown.getName();
    this.name = name != null ? new SimpleStringProperty(name) : null;

    String description = gown.getDescription();
    this.description = description != null ? new SimpleStringProperty(description) : null;

    Double price = gown.getPrice();
    this.price = price != null ? new SimpleDoubleProperty(price) : null;

    String dateRented = gown.getDateRented();
    this.dateRented = dateRented != null ? new SimpleStringProperty(dateRented) : null;

    String dueDate = gown.getDueDate();
    this.dueDate = dueDate != null ? new SimpleStringProperty(dueDate) : null;

    String status = gown.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;
  }
}
