package rigor.io.Sharons.dashboard;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import rigor.io.Sharons.api.gown.GownHandler;
import rigor.io.Sharons.api.gown.GownService;
import rigor.io.Sharons.api.gown.GownStatus;
import rigor.io.Sharons.api.gown.entities.Gown;
import rigor.io.Sharons.api.gown.entities.GownFxAdapter;
import rigor.io.Sharons.api.gown.repository.GownCsvRepository;
import rigor.io.Sharons.edit.EditPresenter;
import rigor.io.Sharons.edit.EditView;
import rigor.io.Sharons.utils.GuiManager;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardPresenter implements Initializable {
  @FXML
  private JFXTextField clientText;
  @FXML
  private JFXTextField contactText;
  @FXML
  private JFXDatePicker dateRentedText;
  @FXML
  private JFXDatePicker dueDateText;
  @FXML
  private JFXComboBox statusSearchText;
  @FXML
  private JFXTextField priceSearchText;
  @FXML
  private JFXComboBox<String> priceOptionsText;
  @FXML
  private JFXTextField filterText;
  @FXML
  private TableView<GownFxAdapter> gownsTable;
  @FXML
  private JFXComboBox statusBox;
  @FXML
  private JFXTextField priceText;
  @FXML
  private JFXTextArea descText;
  @FXML
  private JFXTextField nameText;

  private GownService gownService;
  private static final String ALL = "All";
  private static final String LESS_THAN = "Less than or equals";
  private static final String MORE_THAN = "More than or equals";

  public DashboardPresenter() {
    gownService = new GownHandler(new GownCsvRepository());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    priceOptionsText.setItems(FXCollections.observableArrayList(Arrays.asList(ALL, LESS_THAN, MORE_THAN)));
    priceOptionsText.setValue(ALL);
    priceSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*"))
        priceSearchText.setText(newValue.replaceAll("[^\\d]", ""));
    });

    ObservableList es = FXCollections.observableArrayList(Arrays.stream(GownStatus.values()).map(GownStatus::getStatus).collect(Collectors.toList()));
    statusSearchText.setItems(es);
    statusSearchText.getItems().add("All");
    statusBox.setItems(es);

    gownsTable.getSelectionModel().setSelectionMode(
        SelectionMode.MULTIPLE
                                                   );

    TableColumn<GownFxAdapter, String> name = new TableColumn<>("Name");
    name.setCellValueFactory(param -> param.getValue().getName());

    TableColumn<GownFxAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());

    TableColumn<GownFxAdapter, String> price = new TableColumn<>("Price");
    price.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getPrice().get()));

    TableColumn<GownFxAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<GownFxAdapter, String> dateRented = new TableColumn<>("Date Rented");
    dateRented.setCellValueFactory(param -> param.getValue().getDateRented());

    TableColumn<GownFxAdapter, String> dueDate = new TableColumn<>("Due Date");
    dueDate.setCellValueFactory(param -> param.getValue().getDueDate());

    TableColumn<GownFxAdapter, String> client = new TableColumn<>("Client");
    client.setCellValueFactory(param -> param.getValue().getClient());

    TableColumn<GownFxAdapter, String> contact = new TableColumn<>("Contact");
    contact.setCellValueFactory(param -> param.getValue().getContact());


    gownsTable.getColumns().addAll(name, description, price, status,
                                   dateRented, dueDate, client, contact
                                  );

    gownsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshItems(getFXGowns());

  }

  @FXML
  public void filter() {
    String text = filterText.getText() != null ? filterText.getText().toLowerCase() : "";
    String statusText = statusSearchText.getValue() != null ? statusSearchText.getValue().toString().toLowerCase() : "";
    if (text.length() < 1)
      refreshItems(getFXGowns());

    String priceBoxFilter = priceOptionsText.getValue();
    String givenPrice = priceSearchText.getText();
    FilteredList<GownFxAdapter> gownList = getFXGowns()
        .filtered(gown -> {
          StringProperty name = gown.getName();
          StringProperty description = gown.getDescription();
          boolean searchFilter = (name != null && name.get().toLowerCase().contains(text)) || (description != null && description.get().toLowerCase().contains(text));
          DoubleProperty price = gown.getPrice();
          StringProperty status = gown.getStatus();
          boolean priceFilter = true;
          if (!priceBoxFilter.equalsIgnoreCase("all")) {
            if (givenPrice != null && givenPrice.length() > 0)
              switch (priceBoxFilter) {
                case MORE_THAN:
                  priceFilter = price.get() >= Double.parseDouble(givenPrice);
                  break;
                case LESS_THAN:
                  priceFilter = price.get() <= Double.parseDouble(givenPrice);
                  break;
                default:
                  priceFilter = true;
                  break;
              }
          }
          boolean statusFilter = (status != null && status.get().toLowerCase().contains(statusText.toLowerCase()));
          if (statusText.equalsIgnoreCase("all"))
            statusFilter = true;
          return searchFilter && priceFilter && statusFilter;
        });
    refreshItems(gownList);

  }

  @FXML
  public void refresh() {
    refreshItems(getFXGowns());
  }

  @FXML
  public void add() {
    Gown gown = Gown.builder()
        .name(nameText.getText())
        .description(descText.getText())
        .price(!priceText.getText().equals("") ? Double.valueOf(priceText.getText()) : 0.0)
        .dueDate(dateRentedText.getValue().toString())
        .dateRented(dueDateText.getValue().toString())
        .status(statusBox.getValue() != null ? statusBox.getValue().toString() : GownStatus.AVAILABLE.getStatus())
        .client(clientText.getText())
        .contact(contactText.getText())
        .build();
    boolean success = gownService.add(gown);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle(success ? "Success" : "Failed");
    alert.setHeaderText(success ? "Gown added" : "Error encountered. Gown was not added, try again.");
    alert.showAndWait();
    refresh();
  }

  @FXML
  public void edit() {
    EditPresenter.selectedGown = gownsTable.getSelectionModel().getSelectedItem();
    GuiManager.getInstance().displayView(new EditView());
  }

  @FXML
  public void delete() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle("Confirm deletion");
    alert.setHeaderText("Delete item?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      ObservableList<GownFxAdapter> selectedItems = gownsTable.getSelectionModel().getSelectedItems();
      for (GownFxAdapter gownAdapter : selectedItems) {
        LongProperty id = gownAdapter.getId();
        if (id != null) {
          gownService.delete(id.get());
        }
      }
      refresh();
    }
  }

  private void refreshItems(ObservableList<GownFxAdapter> gowns) {
    gownsTable.setItems(gowns);
  }

  @NotNull
  private ObservableList<GownFxAdapter> getFXGowns() {
    return FXCollections.observableArrayList(allGowns().stream()
                                                 .map(GownFxAdapter::new)
                                                 .collect(Collectors.toList()));
  }

  private List<Gown> allGowns() {
    return gownService.all();
  }
}
