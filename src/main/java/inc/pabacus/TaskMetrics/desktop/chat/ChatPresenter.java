package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatPresenter implements Initializable {

  @FXML
  private JFXTextField command;
  private ChatService chatService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chatService = BeanManager.chatService();
  }

  @FXML
  public void searchCommand() {
    String command = this.command.getText();
    chatService.sendCommand(command);
  }
}
