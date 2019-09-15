package rigor.io.Sharons.newGown;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import rigor.io.Sharons.api.GownHandler;
import rigor.io.Sharons.api.GownService;
import rigor.io.Sharons.api.gown.Gown;
import rigor.io.Sharons.api.gown.repository.GownCsvRepository;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewGownPresenter implements Initializable {
  @FXML
  private JFXTextField priceText;
  @FXML
  private JFXTextField descText;
  @FXML
  private JFXTextField nameText;

  private GownService gownService;

  public NewGownPresenter() {
    gownService = new GownHandler(new GownCsvRepository());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  public void add() {
    Gown gown = Gown.builder()
//        .id(10L)
        .name(nameText.getText())
        .description(descText.getText())
        .price(Double.valueOf(priceText.getText()))
        .dueDate("1")
        .dateRented("f")
        .build();
    boolean success = gownService.add(gown);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle(success ? "Success" : "Failed");
    alert.setHeaderText(success ? "Gown added" : "Error encountered. Gown was not added, try again.");
    if (success)
      alert.setContentText("Please refresh the table to see changes");
    Optional<ButtonType> result = alert.showAndWait();
    alert.close();
    if (result.isPresent() && result.get() == ButtonType.OK)
      ((Stage) priceText.getScene().getWindow()).close();
  }

  @FXML
  public void cancel() {
  }
}
