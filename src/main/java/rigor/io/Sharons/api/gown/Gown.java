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
  private String price;
  private String dateRented;
  private String dueDate;
}
