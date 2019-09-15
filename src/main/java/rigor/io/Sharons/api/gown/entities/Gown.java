package rigor.io.Sharons.api.gown.entities;

import com.univocity.parsers.annotations.Parsed;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gown {
  @Parsed(index = 0)
  private Long id;
  @Parsed(index = 2)
  private String name;
  @Parsed(index = 1)
  private String description;
  @Parsed(index = 3)
  private Double price;
  @Parsed(index = 4)
  private String dateRented;
  @Parsed(index = 5)
  private String dueDate;

  public Gown(Gown gown) {
    this.id = gown.id;
    this.name = gown.name;
    this.description = gown.description;
    this.price = gown.price;
    this.dateRented = gown.dateRented;
    this.dueDate = gown.dueDate;
  }

  public Gown(GownFxAdapter gown) {
    LongProperty id = gown.getId();
    this.id = id != null ? id.get() : null;

    StringProperty name = gown.getName();
    this.name = name != null ? name.get() : null;

    StringProperty description = gown.getDescription();
    this.description = description != null ? description.get() : null;

    DoubleProperty price = gown.getPrice();
    this.price = price != null ? price.get() : null;

    StringProperty dateRented = gown.getDateRented();
    this.dateRented = dateRented != null ? dateRented.get() : null;

    StringProperty dueDate = gown.getDueDate();
    this.dueDate = dueDate != null ? dueDate.get() : null;
  }
}
