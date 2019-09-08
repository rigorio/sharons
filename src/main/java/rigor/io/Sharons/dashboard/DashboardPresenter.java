package rigor.io.Sharons.dashboard;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;
import rigor.io.Sharons.api.GownHandler;
import rigor.io.Sharons.api.GownService;
import rigor.io.Sharons.api.gown.Gown;
import rigor.io.Sharons.api.gown.GownFxAdapter;
import rigor.io.Sharons.api.gown.repository.GownListRepository;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardPresenter implements Initializable {
  @FXML
  private JFXTextField priceText;
  @FXML
  private JFXComboBox priceBox;
  @FXML
  private JFXTextField filterText;
  @FXML
  private TableView<GownFxAdapter> gownsTable;

  private GownService gownService;
  private static final String ALL = "All";
  private static final String LESS_THAN = "Less than or equals";
  private static final String MORE_THAN = "More than or equals";

  public DashboardPresenter() {
    gownService = new GownHandler(new GownListRepository());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    priceBox.setItems(FXCollections.observableArrayList(Arrays.asList(ALL, LESS_THAN, MORE_THAN)));

    TableColumn<GownFxAdapter, String> name = new TableColumn<>("Name");
    name.setCellValueFactory(param -> param.getValue().getName());

    TableColumn<GownFxAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());

    TableColumn<GownFxAdapter, String> price = new TableColumn<>("Price");
    price.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getPrice().get()));

    TableColumn<GownFxAdapter, String> dateRented = new TableColumn<>("Date Rented");
    dateRented.setCellValueFactory(param -> param.getValue().getDateRented());

    TableColumn<GownFxAdapter, String> dueDate = new TableColumn<>("Due Date");
    dueDate.setCellValueFactory(param -> param.getValue().getDueDate());

    gownsTable.getColumns().addAll(name, description, price,
                                   dateRented, dueDate);

    gownsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshItems(getFXGowns());

  }

  @FXML
  public void tableClicked() {
    System.out.println("Does something?");
  }

  @FXML
  public void filter() {
    String text = filterText.getText() != null ? filterText.getText().toLowerCase() : "";
    if (text.length() < 1)
      refreshItems(getFXGowns());

    FilteredList<GownFxAdapter> wowwhatisfilteredlist = getFXGowns()
        .filtered(gown -> {
          StringProperty name = gown.getName();
          StringProperty description = gown.getDescription();
          return (name != null && name.get().toLowerCase().contains(text)) || (description != null && description.get().toLowerCase().contains(text));
        });
    refreshItems(wowwhatisfilteredlist);

  }

  @FXML
  public void priceFilter() {
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
