package inc.pabacus.TaskMetrics.api.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@JsonIgnoreProperties
@Data
@Entity
@NoArgsConstructor
public class Payslip {
  private Long id;
//  private String employeeName;
  private String payRunCoverage;
  private Double basicPay;
  private Double regularHoliday;
  private Double absences;
  private Double tardiness;
  private Double totalEarnings;
  private Double hdmf;
  private Double sss;
  private Double wtax;
  private Double totalDeductions;
  private Double netPay;

//  public Payslip(PayslipAdapter payslip) {
//    LongProperty id = payslip.getId();
//    this.id = id != null ? id.get() : null;
//
//    StringProperty employeeName = payslip.getEmployeeName();
//    this.employeeName = employeeName != null ? employeeName.get() : null;
//
//    StringProperty payRunCoverage = payslip.getPayRunCoverage();
//    this.payRunCoverage = payRunCoverage != null ? payRunCoverage.get() : null;
//
//    LongProperty basicPay = payslip.getBasicPay();
//    this.basicPay = basicPay != null ? basicPay.get() : null;
//
//    LongProperty regularHoliday = payslip.getRegularHoliday();
//    this.regularHoliday = regularHoliday != null ? regularHoliday.get() : null;
//
//    LongProperty absences = payslip.getAbsences();
//    this.absences = absences != null ? absences.get() : null;
//
//    LongProperty totalEarnings = payslip.getTotalEarnings();
//    this.totalEarnings = totalEarnings != null ? totalEarnings.get() : null;
//
//    LongProperty hdmf = payslip.getHdmf();
//    this.hdmf = hdmf != null ? hdmf.get() : null;
//
//    LongProperty sss = payslip.getSss();
//    this.sss = sss != null ? sss.get() : null;
//
//    LongProperty wtax = payslip.getWtax();
//    this.wtax = wtax != null ? wtax.get() : null;
//
//    LongProperty totalDeductions = payslip.getTotalDeductions();
//    this.totalDeductions = totalDeductions != null ? totalDeductions.get() : null;
//
//    LongProperty netPay = payslip.getNetPay();
//    this.netPay = netPay != null ? netPay.get() : null;
//
//  }

  public Payslip(Long id, String payRunCoverage, Double basicPay, Double regularHoliday, Double absences, Double totalEarnings, Double hdmf, Double sss, Double wtax, Double totalDeductions, Double netPay) {
    this.id = id;
    this.payRunCoverage = payRunCoverage;
    this.basicPay = basicPay;
    this.regularHoliday = regularHoliday;
    this.absences = absences;
    this.totalEarnings = totalEarnings;
    this.hdmf = hdmf;
    this.sss = sss;
    this.wtax = wtax;
    this.totalDeductions = totalDeductions;
    this.netPay = netPay;
  }
}
