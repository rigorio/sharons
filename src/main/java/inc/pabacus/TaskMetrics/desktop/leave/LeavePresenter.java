package inc.pabacus.TaskMetrics.desktop.leave;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheService;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.utils.web.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LeavePresenter implements Initializable {
  @FXML
  private AnchorPane mainPane;
  @FXML
  private TextArea reason;
  @FXML
  private JFXDatePicker startDate;
  @FXML
  private JFXDatePicker endDate;
  @FXML
  private JFXComboBox<String> requestDropdown;
  @FXML
  private JFXButton close;
  @FXML
  private JFXComboBox<String> supervisorDropdown;
  @FXML
  private JFXComboBox<String> managerDropdown;

  private static String HOST;
  private CacheService<CacheKey, String> cacheService;
  private final HostConfig hostConfig;

  public LeavePresenter() {
    hostConfig = new HostConfig();
    HOST = new HostConfig().getHost();
    cacheService = new StringCacheService();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    supervisorDropdown.getItems().addAll("Rose Cayabyab", "Rufino Quimen");
    managerDropdown.getItems().addAll("Joy Cuison", "Rodel Caras");

    Platform.runLater(this::getTypesOfRequestLeave);

    dates();
  }

  @FXML
  public void sendForm() {
    if (isNotEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return;
    }
    submitRequest();
  }

  private void dates() {
    startDate.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();
        setDisable(empty || date.compareTo(today) < 0);
      }
    });

    startDate.valueProperty().addListener((ov, oldValue, newValue) -> {
      endDate.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean empty) {
          super.updateItem(date, empty);
          setDisable(empty || date.compareTo(startDate.getValue()) < 0);
        }
      });
    });
  }

  private void submitRequest() {
    String requestString = this.requestDropdown.getValue();
    String startDateString = String.valueOf(this.startDate.getValue());
    String endDateString = String.valueOf(this.endDate.getValue());
    String reason = this.reason.getText();
    Long status = 0L;

    LeaveService leaveService = new LeaveService();
    leaveService.sendLeave(new SendLeave(cacheService.get(CacheKey.EMPLOYEE_ID),cacheService.get(CacheKey.MANAGER_ID),reason,0L,1L,startDateString,endDateString,null,null));
    // maybe log this (MAYBE)
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Request submitted!");
    alert.showAndWait();
    cancel();
  }

  public void cancel() {
    ((Stage) mainPane.getScene().getWindow()).close();
  }

  private boolean isNotEmpty() {
    return reason.getText().isEmpty() || startDate.getValue() == null || endDate.getValue() == null || requestDropdown.getValue() == null;
  }

  @FXML
  void close(ActionEvent event) {
    cancel();
  }

  private void getTypesOfRequestLeave() {

    String accessToken = cacheService.get(CacheKey.SHRIS_TOKEN);

    OkHttpClient client = SslUtil.getSslOkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(hostConfig.getHris() + "/api/services/app/LeaveType/GetAll")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", accessToken)
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String getTypesOfLeaves = response.body().string();
      System.out.println(getTypesOfLeaves);

      JSONObject jsonObject = new JSONObject(getTypesOfLeaves);
      JSONObject object = jsonObject.getJSONObject("result");

      JSONArray jsonarray = object.getJSONArray("items");
      for (int i = 0; i < jsonarray.length(); ++i) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        requestDropdown.getItems().addAll(jsonobject.getString("name"));
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }

  }
}
