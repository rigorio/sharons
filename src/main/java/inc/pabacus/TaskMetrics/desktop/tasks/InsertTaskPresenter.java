package inc.pabacus.TaskMetrics.desktop.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertTaskPresenter implements Initializable {

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXComboBox<String> billableComboBox;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXComboBox<String> projectComboBox;

    @FXML
    private JFXComboBox<String> businessComboBox;

    ObservableList<String> projectList = FXCollections.observableArrayList("ABC","XYZ","ASD");
    ObservableList<String> billableList = FXCollections.observableArrayList("True","False");

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String HOST = "http://localhost:8080";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    getItems();
    billableComboBox.setPromptText("Choose Billable");
    projectComboBox.setPromptText("Choose Project");
    businessComboBox.setPromptText("Choose Business Value");
    projectComboBox.setItems(projectList);
    billableComboBox.setItems(billableList);
    // set default value
    projectComboBox.setValue("ABC");
    billableComboBox.setValue("True");
    businessComboBox.setValue("Accounting");
    }

    @FXML
    public void close(){
        Stage stage = (Stage) projectComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save(){
        boolean isMprojectComboBoxEmpty = projectComboBox.getSelectionModel().isEmpty();
        boolean isbillableComboBoxEmpty = billableComboBox.getSelectionModel().isEmpty();
        boolean isbusinessComboBoxEmpty = businessComboBox.getSelectionModel().isEmpty();
        boolean isDescriptionEmpty = descriptionField.getText().isEmpty();

        if(isMprojectComboBoxEmpty || isbillableComboBoxEmpty || isDescriptionEmpty || isbusinessComboBoxEmpty){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill out all the fields");
            alert.showAndWait();
        }else{
            String title = projectComboBox.getValue();
            String description = descriptionField.getText();
            Boolean billable = Boolean.valueOf(billableComboBox.getValue());
            String status = "BACKLOG";
            InsertTaskPresenter insertTask = new InsertTaskPresenter();
            InsertTask task = insertTask.saveTask(new InsertTask(title,description,billable,status));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Task saved!");
            alert.showAndWait();
            //close the window after creating the task
            Stage stage = (Stage) projectComboBox.getScene().getWindow();
            stage.close();
        }
    }

    public InsertTask saveTask(InsertTask task) {
        InsertTask insertTask;
        try {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String jsonString = mapper.writeValueAsString(task);
            RequestBody body = RequestBody.create(JSON, jsonString);

            Call call = client.newCall(new Request.Builder()
                    .url(HOST + "/api/task") // TODO add path
                    .post(body)
                    .build());
            ResponseBody responseBody = call.execute().body();
            insertTask = mapper.readValue(responseBody.string(),
                    new TypeReference<InsertTask>() {});
            return insertTask;
        } catch (IOException e) {
            return task;
        }
    }

    public void getItems(){
        try {
            OkHttpClient client = new OkHttpClient();
            // code request code here
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/businessValue")
                    .addHeader("Accept", "application/json")
                    .method("GET", null)
                    .build();

            Response response = null;

            response = client.newCall(request).execute();

            String getTimes = response.body().string();

            JSONArray jsonArray = new JSONArray(getTimes);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String value1 = jsonObject1.getString("business");

                businessComboBox.getItems().addAll(value1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
