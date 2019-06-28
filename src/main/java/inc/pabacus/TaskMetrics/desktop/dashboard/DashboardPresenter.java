package inc.pabacus.TaskMetrics.desktop.dashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.generateToken.TokenService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.kicker.TokenHolder;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.chat.ChatView;
import inc.pabacus.TaskMetrics.desktop.easyChat.EasyChatView;
import inc.pabacus.TaskMetrics.desktop.idle.IdleView;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.desktop.productivity.ProductivityView;
import inc.pabacus.TaskMetrics.desktop.screenshot.ScreenShotView;
import inc.pabacus.TaskMetrics.desktop.settings.SettingsView;
import inc.pabacus.TaskMetrics.desktop.support.SupportView;
import inc.pabacus.TaskMetrics.desktop.tasks.TasksView;
import inc.pabacus.TaskMetrics.desktop.timesheet.TimesheetView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardPresenter implements Initializable {

  @FXML
  private JFXButton settingsButton;
  @FXML
  private ImageView settingsIcon;
  @FXML
  private ImageView supportIcon;
  @FXML
  private JFXButton supportButton;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private AnchorPane dashboardPane;
  @FXML
  private Pane userPane;
  @FXML
  private VBox vboxPane;
  @FXML
  private HBox easyChatHBox;
  @FXML
  private JFXComboBox status;
  @FXML
  private Label username;
  @FXML
  private JFXButton screenshotButton;
  @FXML
  private VBox dynamicContentPane;
  @FXML
  private JFXButton tasksButton;
  @FXML
  private JFXButton timesheetButton;
  @FXML
  private JFXButton chatButton;
  @FXML
  private JFXButton logoutBtn;
  @FXML
  private ImageView productivityIcon;
  @FXML
  private JFXButton productivityButton;

  private StandupService standupService;
  private KickerService kickerService;
  private HardwareServiceAPI hardwareServiceAPI;
  private SoftwareServiceAPI softwareServiceAPI;
  private ScreenshotServiceImpl screenshotService;
  private UserHandler userHandler;
  private TokenService tokenService;
  private ActivityListener activityListener;

  public DashboardPresenter() {
    standupService = BeanManager.standupService();
    kickerService = BeanManager.kickerService();
    hardwareServiceAPI = BeanManager.hardwareServiceAPI();
    softwareServiceAPI = BeanManager.softwareServiceAPI();
    screenshotService = BeanManager.screenshotService();
    userHandler = BeanManager.userHandler();
    tokenService = BeanManager.tokenService();
    activityListener = BeanManager.activityListener();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    username.setText(userHandler.getUsername());

    services();
    showEasyChat();
    responsive();

    ImageView taskImage = new ImageView(new Image(getClass().getResourceAsStream("/img/jobs.png")));
    setSize(taskImage);
    tasksButton.setGraphic(taskImage);

    ImageView softwareImage = new ImageView(new Image(getClass().getResourceAsStream("/img/timesheet.png")));
    setSize(softwareImage);
    timesheetButton.setGraphic(softwareImage);

    ImageView logoutImage = new ImageView(new Image(getClass().getResourceAsStream("/img/logout.png")));
    setSize(logoutImage);
    logoutBtn.setGraphic(logoutImage);

    ImageView screenshotImage = new ImageView(new Image(getClass().getResourceAsStream("/img/screenshot.png")));
    setSize(screenshotImage);
    screenshotButton.setGraphic(screenshotImage);

    ImageView chatImage = new ImageView(new Image(getClass().getResourceAsStream("/img/chat.png")));
    setSize(chatImage);
    chatButton.setGraphic(chatImage);

    ImageView supportImage = new ImageView(new Image(getClass().getResourceAsStream("/img/support.png")));
    setSize(supportImage);
    supportButton.setGraphic(supportImage);

    ImageView settingsImage = new ImageView(new Image(getClass().getResourceAsStream("/img/settings.png")));
    setSize(settingsImage);
    settingsButton.setGraphic(settingsImage);

    ImageView productivityImage = new ImageView(new Image(getClass().getResourceAsStream("/img/productivity.png")));
    setSize(productivityImage);
    productivityButton.setGraphic(productivityImage);

    status.getItems().addAll("Busy", "Meeting", "Lunch", "Offline");

    viewTasks();
    shortcutKeyPressed();
  }

  private void shortcutKeyPressed() {
    mainPane.setOnKeyPressed(
        event -> status.getScene().getAccelerators().put(new KeyCodeCombination(
            KeyCode.F, KeyCombination.CONTROL_ANY), new Runnable() {
          @Override
          public void run() {
            showEasyChat();
          }
        }));
  }

  private void responsive() {
    //trick - remove focus on the easyChat ----
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        mainPane.requestFocus();
      }
    });

    dynamicContentPane.toBack();
    //calculate width and height
    mainPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      double width = (double) newValue;

      dashboardPane.setPrefWidth(width);
      easyChatHBox.setPrefWidth(width / 1.15);
    });

    mainPane.heightProperty().addListener((observable, oldValue, newValue) -> {
      double height = (double) newValue;
      dashboardPane.setPrefHeight(height);
    });

    dashboardPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      double width = (double) newValue;
      easyChatHBox.setPrefWidth(width);
      dynamicContentPane.setPrefWidth(width / 1.2);
      vboxPane.setPrefWidth(width / 6.5);
    });

    dashboardPane.heightProperty().addListener((observable, oldValue, newValue) -> {
      double height = (double) newValue;
      dynamicContentPane.setPrefHeight(height);
      vboxPane.setPrefHeight(height);
    });

    vboxPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      double width = (double) newValue;
      status.setPrefWidth(width / 1.5);
      tasksButton.setPrefWidth(width);
      timesheetButton.setPrefWidth(width);
      screenshotButton.setPrefWidth(width);
      chatButton.setPrefWidth(width);
      logoutBtn.setPrefWidth(width);
      username.setPrefWidth(width);
      userPane.setPrefWidth(width);
    });

    vboxPane.heightProperty().addListener((observable, oldValue, newValue) -> {
      double height = (double) newValue;
      status.setPrefHeight(height / 20.5);
      tasksButton.setPrefHeight(height / 15);
      timesheetButton.setPrefHeight(height / 15);
      screenshotButton.setPrefHeight(height / 15);
      chatButton.setPrefHeight(height / 15);
      logoutBtn.setPrefHeight(height / 15);
      username.setPrefHeight(height / 15);
      userPane.setPrefHeight(height / 5);
    });

  }

  private void services() {
    hardwareServiceAPI.sendHardwareData();
    standupService.runStandup();
    softwareServiceAPI.sendSoftwareData();
    screenshotService.enableScreenShot();
    ActivityListener activityLirstener = BeanManager.activityListener();
    Runnable runnable = () -> {
      Platform.runLater(() -> GuiManager.getInstance().displayView(new IdleView()));
      activityListener.unListen();
    };
    activityListener.setEvent(runnable);
    activityListener.setInterval(10000);
//    activityListener.listen();
  }

  @FXML
  public void viewTasks() {
    updateDynamicPaneContent(new TasksView().getView());
  }

  @FXML
  public void viewScreenshots() {
    //To change the cursor to loading
    dynamicContentPane.getScene().setCursor(Cursor.WAIT);
    //PauseTransition to load completely the screenShotView
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(event -> {
      dynamicContentPane.setCache(true);
      dynamicContentPane.setCacheHint(CacheHint.SPEED);
      dynamicContentPane.getScene().setCursor(Cursor.DEFAULT);
      updateDynamicPaneContent(new ScreenShotView().getView());
    });
    pause.play();
//    GuiManager.getInstance().displayView(new ScreenShotView());
  }

  @FXML
  public void viewTimesheet() {
    updateDynamicPaneContent(new TimesheetView().getView());
  }

  @FXML
  public void viewProductivity() {
    updateDynamicPaneContent(new ProductivityView().getView());
  }

  @FXML
  public void viewChats() {
    updateDynamicPaneContent(new ChatView().getView());
  }

  @FXML
  public void viewSettings() {
    updateDynamicPaneContent(new SettingsView().getView());
  }

  public void viewSupport() {
    updateDynamicPaneContent(new SupportView().getView());
  }

  @FXML
  public void logout() {
    //Prevent from closing
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Log Out");
    alert.setHeaderText("Warning!");
    alert.setContentText("Are you sure you want to log out?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      //disable all services manually - maybe we can kill these threads/services automatically?
      kickerService.logout(TokenHolder.getToken());
      standupService.close();
      hardwareServiceAPI.cancel();
      softwareServiceAPI.cancel();
      screenshotService.disableScreenshot();
      screenshotService.shutdownScheduler();
      kickerService.stopKicker();
      tokenService.stopToken();
//      activityListener.unListen();

      GuiManager.getInstance().closeStage();
      GuiManager.getInstance().changeView(new LoginView());
    } else {
      alert.close();
    }
  }

  @FXML
  public void changeStatus() {
    System.out.println(status.getValue());
  }

  private void setSize(ImageView taskImage) {
    taskImage.setFitHeight(30);
    taskImage.setFitWidth(30);
  }

  private void updateDynamicPaneContent(Parent parent) {
    AnchorPane.setTopAnchor(parent, 0.0);
    AnchorPane.setLeftAnchor(parent, 0.0);
    AnchorPane.setBottomAnchor(parent, 0.0);
    AnchorPane.setRightAnchor(parent, 0.0);

    dynamicContentPane.getChildren().clear();
    dynamicContentPane.getChildren().add(parent);
  }

  private void showEasyChat() {
    Parent parent = new EasyChatView().getView();
    AnchorPane.setTopAnchor(parent, 0.0);
    AnchorPane.setLeftAnchor(parent, 0.0);
    AnchorPane.setBottomAnchor(parent, 0.0);
    AnchorPane.setRightAnchor(parent, 0.0);

    easyChatHBox.getChildren().clear();
    easyChatHBox.getChildren().add(parent);
  }
}
