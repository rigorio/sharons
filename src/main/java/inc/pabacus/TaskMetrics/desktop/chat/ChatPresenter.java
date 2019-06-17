package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXListView<String> listView;
  @FXML
  private JFXTextField command;
  @FXML
  private ImageView image;
  private ChatService chatService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chatService = BeanManager.chatService();
    //images
    Image imageView = new Image("/img/search.jpg");
    image.setImage(imageView);
    image.setFitWidth(30);
    image.setFitHeight(30);
    //textfield
    command.setText(null);
    command.requestFocus();
  }

  @FXML
  private void onEnter(){
    String commands = this.command.getText();
    if (commands != null) {
      addItem(listView,commands);
      chatService.sendCommand(commands);
      command.setText(null);
      command.requestFocus();
    }
  }

  private static <T> void addItem(ListView<T> listView, T item) {
    List<T> items = listView.getItems();
    int index = items.size();
    items.add(item);
    listView.scrollTo(index);
  }

}
