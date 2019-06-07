package inc.pabacus.TaskMetrics.desktop.easyChat;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class EasyChatPresenter implements Initializable {


  @FXML
  private TextField textCommand;
  @FXML
  private JFXButton clearButton;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private HBox hboxContent;

  private ChatService chatService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    responsive();
    clearButton.setVisible(false);
    chatService = BeanManager.chatService();

    textCommand.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.equals(""))
          clearButton.setVisible(false);
        else
          clearButton.setVisible(true);
      }
    });
  }

  @FXML
  void onEnter(ActionEvent event) {
    String command = this.textCommand.getText();
    chatService.sendCommand(command);
  }

  @FXML
  private void clearButton(){
    textCommand.setText("");
    clearButton.setVisible(false);
    textCommand.requestFocus();
  }

  private void responsive(){
    mainPane.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double width = (double) newValue;
        hboxContent.setPrefWidth(width);
      }
    });
  }

}
