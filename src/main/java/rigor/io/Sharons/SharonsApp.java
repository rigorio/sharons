package rigor.io.Sharons;

import javafx.application.Application;
import javafx.stage.Stage;
import rigor.io.Sharons.api.gown.repository.GownCsvRepository;
import rigor.io.Sharons.api.gown.repository.GownListRepository;
import rigor.io.Sharons.api.gown.repository.GownRepository;
import rigor.io.Sharons.dashboard.DashboardView;
import rigor.io.Sharons.utils.GuiManager;

public class SharonsApp extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
//    GownRepository gownRepository = new GownListRepository();
//    gownRepository.setList(new GownCsvRepository().getGowns());
    stage.setResizable(false);
//    stage.getIcons().add(new Image("/img/.png"));
    MANAGER.setPrimaryStage(stage);
    MANAGER.changeView(new DashboardView());
//    MANAGER.changeView(new LoginView());
  }

  @Override
  public void stop() {
  }


}
