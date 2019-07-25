package inc.pabacus.TaskMetrics.desktop.support;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.api.payslip.Payslip;
import inc.pabacus.TaskMetrics.api.payslip.PayslipHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.leaveViewer.LeaveHolder;
import inc.pabacus.TaskMetrics.desktop.leaveViewer.LeaveViewerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class SupportPresenter implements Initializable {

  @FXML
  private JFXComboBox<String> technicalBox;
  @FXML
  private JFXComboBox statusComboBox;
  @FXML
  private TableView<LeaveAdapter> leaveTable;
  @FXML
  private JFXComboBox payslipComboBox;

  private LeaveService leaveService;
  private UserHandler userHandler;
  private TemporaryStaffHolder staffHolder;
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private JobTaskHandler jobTaskHandler = new JobTaskHandler();

  public SupportPresenter() {
    leaveService = BeanManager.leaveService();
    userHandler = BeanManager.userHandler();
    staffHolder = new TemporaryStaffHolder();
    HOST = hostConfig.getHost();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

//    Job action_center = getAction_center();

//    List<Task> tasks = jobTaskHandler.allTasks().stream()
//        .filter(task -> task.getJobId().equals(action_center.getId()))
//        .collect(Collectors.toList());
//    tasks.forEach(task -> technicalBox.getItems().add(task.getTask()));

    technicalBox.getItems().addAll("Mouse not working", "Slow internet", "Problem with Display", "Faulty Keyboard");
    initTable();

    payslipComboBox.getItems().addAll("Jul 01 - Jul 15", "July 2nd half month");

  }

  @FXML
  private void payslipView() {

    try {

      if (payslipComboBox.getValue().toString().contains("Jul 01")) {
        getPayslip();

        Platform.runLater(() -> {
          try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File", "*.pdf"));
            fileChooser.setTitle("Save to PDF");
            fileChooser.setInitialFileName("Payslip for " + payslipComboBox.getValue().toString() + ".pdf");

            Stage stage = (Stage) payslipComboBox.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            String str = file.getAbsolutePath();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(str));
            document.open();

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setWidthPercentage(80); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f};
            table.setWidths(columnWidths);

            PdfPCell payPeriod = new PdfPCell(new Paragraph("PAY PERIOD:"));
            payPeriod.setBorderColor(BaseColor.WHITE);
            payPeriod.setHorizontalAlignment(Element.ALIGN_LEFT);
            payPeriod.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell payRunCoverage = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getPayRunCoverage()));
            payRunCoverage.setBorderColor(BaseColor.WHITE);
            payRunCoverage.setHorizontalAlignment(Element.ALIGN_RIGHT);
            payRunCoverage.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell space1 = new PdfPCell(new Paragraph(" "));
            space1.setBorderColor(BaseColor.WHITE);
            space1.setHorizontalAlignment(Element.ALIGN_CENTER);
            space1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell space2 = new PdfPCell(new Paragraph(" "));
            space2.setBorderColor(BaseColor.WHITE);
            space2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            space2.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell basicPay = new PdfPCell(new Paragraph("Basic Pay"));
            basicPay.setBorderColor(BaseColor.WHITE);
            basicPay.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicPay.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getBasicPay = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getBasicPay().toString()));
            getBasicPay.setBorderColor(BaseColor.WHITE);
            getBasicPay.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getBasicPay.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell regularHoliday = new PdfPCell(new Paragraph("Regular Holiday"));
            regularHoliday.setBorderColor(BaseColor.WHITE);
            regularHoliday.setHorizontalAlignment(Element.ALIGN_LEFT);
            regularHoliday.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getregularHoliday = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getRegularHoliday().toString()));
            getregularHoliday.setBorderColor(BaseColor.WHITE);
            getregularHoliday.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getregularHoliday.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell absences = new PdfPCell(new Paragraph("Absences"));
            absences.setBorderColor(BaseColor.WHITE);
            absences.setHorizontalAlignment(Element.ALIGN_LEFT);
            absences.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getAbsences = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getAbsences().toString()));
            getAbsences.setBorderColor(BaseColor.WHITE);
            getAbsences.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getAbsences.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell tardiness = new PdfPCell(new Paragraph("Tardiness"));
            tardiness.setBorderColor(BaseColor.WHITE);
            tardiness.setHorizontalAlignment(Element.ALIGN_LEFT);
            tardiness.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getTardiness = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getTardiness().toString()));
            getTardiness.setBorderColor(BaseColor.WHITE);
            getTardiness.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getTardiness.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell space3 = new PdfPCell(new Paragraph(" "));
            space3.setBorderColor(BaseColor.WHITE);
            space3.setHorizontalAlignment(Element.ALIGN_LEFT);
            space3.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell space4 = new PdfPCell(new Paragraph(" "));
            space4.setBorderColor(BaseColor.WHITE);
            space4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            space4.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell totalEarnings = new PdfPCell(new Paragraph("Total Earnings"));
            totalEarnings.setBorderColor(BaseColor.WHITE);
            totalEarnings.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalEarnings.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getTotalEarnings = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getTotalEarnings().toString()));
            getTotalEarnings.setBorderColor(BaseColor.WHITE);
            getTotalEarnings.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getTotalEarnings.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell space5 = new PdfPCell(new Paragraph(" "));
            space5.setBorderColor(BaseColor.WHITE);
            space5.setHorizontalAlignment(Element.ALIGN_LEFT);
            space5.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell space6 = new PdfPCell(new Paragraph(" "));
            space6.setBorderColor(BaseColor.WHITE);
            space6.setHorizontalAlignment(Element.ALIGN_RIGHT);
            space6.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell hdmf = new PdfPCell(new Paragraph("HDMF Share"));
            hdmf.setBorderColor(BaseColor.WHITE);
            hdmf.setHorizontalAlignment(Element.ALIGN_LEFT);
            hdmf.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getHdmf = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getHdmf().toString()));
            getHdmf.setBorderColor(BaseColor.WHITE);
            getHdmf.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getHdmf.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell sss = new PdfPCell(new Paragraph("SSS Share"));
            sss.setBorderColor(BaseColor.WHITE);
            sss.setHorizontalAlignment(Element.ALIGN_LEFT);
            sss.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getSss = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getSss().toString()));
            getSss.setBorderColor(BaseColor.WHITE);
            getSss.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getSss.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell wtax = new PdfPCell(new Paragraph("WithHolding Tax"));
            wtax.setBorderColor(BaseColor.WHITE);
            wtax.setHorizontalAlignment(Element.ALIGN_LEFT);
            wtax.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getWtax = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getWtax().toString()));
            getWtax.setBorderColor(BaseColor.WHITE);
            getWtax.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getWtax.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell space7 = new PdfPCell(new Paragraph(" "));
            space7.setBorderColor(BaseColor.WHITE);
            space7.setHorizontalAlignment(Element.ALIGN_LEFT);
            space7.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell space8 = new PdfPCell(new Paragraph(" "));
            space8.setBorderColor(BaseColor.WHITE);
            space8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            space8.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell totalDeduction = new PdfPCell(new Paragraph("TOTAL DEDUCTIONS:"));
            totalDeduction.setBorderColor(BaseColor.WHITE);
            totalDeduction.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalDeduction.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getTotalDeduction = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getTotalDeductions().toString()));
            getTotalDeduction.setBorderColor(BaseColor.WHITE);
            getTotalDeduction.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getTotalDeduction.setVerticalAlignment(Element.ALIGN_RIGHT);

            PdfPCell netPay = new PdfPCell(new Paragraph("NET PAY:"));
            netPay.setBorderColor(BaseColor.WHITE);
            netPay.setHorizontalAlignment(Element.ALIGN_LEFT);
            netPay.setVerticalAlignment(Element.ALIGN_LEFT);

            PdfPCell getNetPay = new PdfPCell(new Paragraph(PayslipHandler.getSelectedPayslip().getNetPay().toString()));
            getNetPay.setBorderColor(BaseColor.WHITE);
            getNetPay.setHorizontalAlignment(Element.ALIGN_RIGHT);
            getNetPay.setVerticalAlignment(Element.ALIGN_RIGHT);

            //To avoid having the cell border and the content overlap, if you are having thick cell borders
            //cell1.setUserBorderPadding(true);
            //cell2.setUserBorderPadding(true);
            //cell3.setUserBorderPadding(true);

            table.addCell(payPeriod);
            table.addCell(payRunCoverage);

            table.addCell(space1);
            table.addCell(space2);

            table.addCell(basicPay);
            table.addCell(getBasicPay);
            table.addCell(regularHoliday);
            table.addCell(getregularHoliday);
            table.addCell(absences);
            table.addCell(getAbsences);
            table.addCell(tardiness);
            table.addCell(getTardiness);

            table.addCell(space3);
            table.addCell(space4);

            table.addCell(totalEarnings);
            table.addCell(getTotalEarnings);

            table.addCell(space5);
            table.addCell(space6);

            table.addCell(hdmf);
            table.addCell(getHdmf);
            table.addCell(sss);
            table.addCell(getSss);
            table.addCell(wtax);
            table.addCell(getWtax);

            table.addCell(space7);
            table.addCell(space8);

            table.addCell(totalDeduction);
            table.addCell(getTotalDeduction);
            table.addCell(netPay);
            table.addCell(getNetPay);


            document.add(table);


            document.close();
            writer.close();
          } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
          }

        });

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @NotNull
  private Job getAction_center() {
    return jobTaskHandler.allJobs().stream()
        .filter(job -> job.getJob().equals("Action Center"))
        .findFirst().get();
  }

  @FXML
  public void sendReport() {
    String taskName = technicalBox.getValue();
    Job job = getAction_center();
    Task task = jobTaskHandler.allTasks().stream()
        .filter(t -> t.getJobId().equals(job.getId()) && t.getTask().equals("Technical Issue"))
        .findFirst()
        .get();
    jobTaskHandler.createJobTask(job.getId(), task.getId(), taskName);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Success");
    alert.setHeaderText(null);
    alert.setContentText("Report has been sent");
    alert.showAndWait();
  }

  private void initTable() {
    ObservableList<LeaveAdapter> allLeaves = getAndConvertLeaves();

    TableColumn<LeaveAdapter, String> staff = new TableColumn<>("Staff");
    staff.setCellValueFactory(param -> {
      Long value = param.getValue().getUserId().getValue();
      return new SimpleStringProperty(staffHolder.getStaff(value));
    });

    TableColumn<LeaveAdapter, String> startDate = new TableColumn<>("Start Date");
    startDate.setCellValueFactory(param -> param.getValue().getStartDate());

    TableColumn<LeaveAdapter, String> endDate = new TableColumn<>("End Date");
    endDate.setCellValueFactory(param -> param.getValue().getEndDate());

    TableColumn<LeaveAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<LeaveAdapter, String> typeOfRequest = new TableColumn<>("Type");
    typeOfRequest.setCellValueFactory(param -> param.getValue().getTypeOfRequest());

    leaveTable.getColumns().addAll(staff, startDate, endDate,
                                   typeOfRequest, status);
    leaveTable.setItems(allLeaves);
  }

  @FXML
  public void modifyLeave() {
    LeaveAdapter leave = leaveTable.getSelectionModel().getSelectedItem();
    LeaveHolder.setLeave(new Leave(leave));
    GuiManager.getInstance().displayView(new LeaveViewerView());
  }

  private ObservableList<LeaveAdapter> getAndConvertLeaves() {
    return FXCollections.observableArrayList(getLeaves().stream()
                                                 .map(LeaveAdapter::new)
                                                 .collect(Collectors.toList()));
  }

  private List<Leave> getLeaves() {
//    List<Leave> all = leaveService.getAll();
    return getApprovalQueue();
  }


  private List<Leave> getApprovalQueue() {
    List<Leave> all = leaveService.getAllLeaves();
    String username = userHandler.getUsername();
    return all.stream()
        .filter(leave -> leave.getApprovers().stream()
            .anyMatch(approver -> approver.getApprover().equals(username)))
        .collect(Collectors.toList());
  }

  private void getPayslip() {

    Payslip payslip = new Payslip();

    try {

    Request request = new Request.Builder()
        .url("https://hureyweb-staging.azurewebsites.net/api/services/app/EmployeePayRun/GetByCurrentEmployee")
        // TODO will need another token generator to Hurey website
        .addHeader("Authorization", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjYiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiY2FybG8ubW9udGVtYXlvciIsIkFzcE5ldC5JZGVudGl0eS5TZWN1cml0eVN0YW1wIjoiRkRYRTdQSDM2WjNVNVA0QktMSU9BRUlSU1NONURRS1QiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJFbXBsb3llZSIsImh0dHA6Ly93d3cuYXNwbmV0Ym9pbGVycGxhdGUuY29tL2lkZW50aXR5L2NsYWltcy90ZW5hbnRJZCI6IjEiLCJzdWIiOiI2IiwianRpIjoiYzIwYjE2MTEtNjdkZC00ODM3LTlhMzAtMWRlYTRiMzIwMDM4IiwiaWF0IjoxNTY0MDExODY0LCJuYmYiOjE1NjQwMTE4NjQsImV4cCI6MTU2NDA5ODI2NCwiaXNzIjoiSFVSRVlfU1RBR0lORyIsImF1ZCI6IkhVUkVZX1NUQUdJTkcifQ.aOYSl8pfBRJQJ7LF9tbATrQaEnVUP1qdkPs0oYa4zsM")
        .method("GET", null)
        .build();

      Response response = client.newCall(request).execute();
      String getbody = response.body().string();

      JSONObject jsonObject = new JSONObject(getbody);

      JSONArray arr = jsonObject.getJSONArray("result");

      for (int i = 0; i < arr.length(); ++i) {
        //pass API using setters
        JSONObject object = arr.getJSONObject(i);
        if (object.getString("payRunCoverage").equalsIgnoreCase(payslipComboBox.getValue().toString())) {
          payslip.setPayRunCoverage(object.getString("payRunCoverage"));
          payslip.setBasicPay(object.getLong("basicPay"));
          payslip.setRegularHoliday(object.getLong("regularHoliday"));
          payslip.setAbsences(object.getLong("absences"));
          payslip.setTardiness(object.getLong("tardiness"));
          payslip.setTotalEarnings(object.getLong("totalEarnings"));
          payslip.setHdmf(object.getLong("hdmf"));
          payslip.setSss(object.getLong("sss"));
          payslip.setWtax(object.getLong("wtax"));
          payslip.setTotalDeductions(object.getLong("totalDeductions"));
          payslip.setNetPay(object.getLong("netPay"));

        }
      }

      PayslipHandler.setSelectedPayslip(payslip);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
