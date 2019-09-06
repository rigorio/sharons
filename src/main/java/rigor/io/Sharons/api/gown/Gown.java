package rigor.io.Sharons.api.gown;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gown {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private String dateRented;
  private String dueDate;

  public Gown(Gown gown) {
    this.id = gown.id;
    this.name = gown.name;
    this.description = gown.description;
    this.price = gown.price;
    this.dateRented = gown.dateRented;
    this.dueDate = gown.dueDate;
  }
}
