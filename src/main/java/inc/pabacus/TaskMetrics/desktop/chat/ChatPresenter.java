package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.chat.Chat;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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
  private static final String HOST = "http://localhost:8080";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chatService = BeanManager.chatService();
    //images
    Image imageView = new Image("/img/search.jpg");
    image.setImage(imageView);
    image.setFitWidth(30);
    image.setFitHeight(30);

    textProperty();
    getChatData();
  }

  private void textProperty(){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        command.setText(null);
        command.requestFocus();
      }
    });
  }

  @FXML
  private void onEnter(){
    String commands = this.command.getText();
    if (commands != null) {
      addItem(listView,commands);
      chatService.sendCommand(commands);
      command.setText(null);
      command.requestFocus();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
      ChatService service = new ChatService();
      Chat answers = service.sendChat(new Chat(commands,formatter.format(LocalTime.now())));
      System.out.println(answers);
    }
  }

  private static <T> void addItem(ListView<T> listView, T item) {
    List<T> items = listView.getItems();
    int index = items.size();
    items.add(item);
    listView.scrollTo(index);
  }

  public void getChatData(){
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/chats")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String getChats = response.body().string();
      JSONArray jsonarray = new JSONArray(getChats);
      for (int i = 0; i < jsonarray.length(); ++i) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        List<String> items = listView.getItems();
        int index = items.size();
        items.add(jsonobject.getString("message"));
        listView.scrollTo(index);
      }

    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }

  }

}
