package inc.pabacus.TaskMetrics.api.payslip;

public class PayslipHandler {
  private static Payslip selectedPayslip;

  public static Payslip getSelectedPayslip() {
    return selectedPayslip;
  }

  public static void setSelectedPayslip(Payslip selectedPayslip) {
    PayslipHandler.selectedPayslip = selectedPayslip;
  }
}
