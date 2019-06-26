package inc.pabacus.TaskMetrics.api.tasks;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceTypeAdapter {
  private LongProperty id;
  private StringProperty type;

  public InvoiceTypeAdapter(InvoiceType invoiceType) {
    this.id = new SimpleLongProperty(invoiceType.getInvoiceTypeId());
    this.type = new SimpleStringProperty(invoiceType.getType());
  }
}
