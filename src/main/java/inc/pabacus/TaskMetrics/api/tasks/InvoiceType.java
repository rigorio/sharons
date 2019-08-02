package inc.pabacus.TaskMetrics.api.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceType {
  private Long id;
  private String type;

  public InvoiceType(InvoiceTypeAdapter invoiceTypeAdaptern) {
    this.id = invoiceTypeAdaptern.getId().get();
    this.type = invoiceTypeAdaptern.getType().getValue();
  }
}
