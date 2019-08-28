package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.api.timesheet.status.ValidationHandler;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
//    disableSslVerification();
    stage.setResizable(true);
//    stage.initStyle(StageStyle.TRANSPARENT);
    stage.getIcons().add(new Image("/img/PabacusLogo.png"));
    //Prevent from closing
    stage.setOnCloseRequest(evt -> {
      evt.consume();
      preventFromClosing();
    });
    MANAGER.setPrimaryStage(stage);
    MANAGER.changeView(new LoginView());
  }

  @Override
  public void stop() {
    stopProcesses();
  }

  private void preventFromClosing() {
    Alert alert = createClosePreventionAlert();
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK)
      stopProcesses();
    alert.close();
  }

  private Alert createClosePreventionAlert() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle("You're about to close the application");
    alert.setHeaderText("Warning!");
    alert.setContentText("You're about to close the application! \nPlease save all your work or you will lose your current data data!");
    return alert;
  }

  private void stopProcesses() {
//    ValidationHandler validationHandler = BeanManager.validationHandler();
//    validationHandler.runValidationChecks();
    //to force stop/close the threads.
    Thread.currentThread().interrupt();
    //to make sure app is close
    Platform.setImplicitExit(true);
    Platform.exit();
    System.exit(0);
  }
  /**
   * disable SSL
   */
  private void disableSslVerification() {
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] {
          new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
            }
          } };

      // Install the all-trusting trust manager
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      // Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      };

      // Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }
  }
}
