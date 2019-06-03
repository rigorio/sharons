package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXTextField command;
  private ChatService chatService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    makeFadeIn();
    chatService = BeanManager.chatService();
  }

  @FXML
  public void searchCommand() {
    String command = this.command.getText();
    chatService.sendCommand(command);
  }

  private void makeFadeIn(){
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000)); // 1 second
    fadeTransition.setNode(mainPane);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }
}
