package rigor.io.Sharons.newGown;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import rigor.io.Sharons.api.GownHandler;
import rigor.io.Sharons.api.GownService;
import rigor.io.Sharons.api.gown.Gown;
import rigor.io.Sharons.api.gown.repository.GownCsvRepository;

import java.net.URL;
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
        .id(10L)
        .name(nameText.getText())
        .description(descText.getText())
        .price(Double.valueOf(priceText.getText()))
        .dueDate("1")
        .dateRented("f")
        .build();
    gownService.add(gown);
  }

  @FXML
  public void cancel() {
  }
}
