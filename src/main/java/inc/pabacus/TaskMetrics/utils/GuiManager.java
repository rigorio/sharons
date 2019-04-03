package inc.pabacus.TaskMetrics.utils;

import com.airhacks.afterburner.views.FXMLView;
import inc.pabacus.TaskMetrics.desktop.login.LoginPresenter;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiManager {

  private static final GuiManager INSTANCE = new GuiManager();

  private Stage primaryStage;

  private LoginView loginView;

  private GuiManager() {
  }

  public static GuiManager getInstance() {
    return INSTANCE;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void login() {
    loginView = new LoginView();
    LoginPresenter loginPresenter = (LoginPresenter) loginView.getPresenter();
    showView(loginView);
  }

  private void showView(FXMLView view) {
    primaryStage.setScene(new Scene(view.getView()));
    primaryStage.show();
  }

}
