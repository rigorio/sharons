package rigor.io.Sharons.dashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;
import rigor.io.Sharons.api.GownHandler;
import rigor.io.Sharons.api.GownService;
import rigor.io.Sharons.api.gown.Gown;
import rigor.io.Sharons.api.gown.GownFxAdapter;
import rigor.io.Sharons.api.gown.repository.GownListRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardPresenter implements Initializable {
  @FXML
  private TableView<GownFxAdapter> gownsTable;

  private GownService gownService;

  public DashboardPresenter() {
    gownService = new GownHandler(new GownListRepository());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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

    refreshItems();

  }

  @FXML
  public void tableClicked() {
    System.out.println("Does something?");
  }

  private void refreshItems() {
    gownsTable.setItems(getFXGowns());
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
