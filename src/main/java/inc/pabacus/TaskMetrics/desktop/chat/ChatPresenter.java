package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXListView;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.chat.Chat;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
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
  private TextField command;
  @FXML
  private ImageView image;

  private ChatService chatService;
  private static final String HOST = "http://localhost:8080";
  private ActivityHandler activityHandler;
  private DailyLogService dailyLogHandler;

  public ChatPresenter() {
    activityHandler = BeanManager.activityHandler();
    dailyLogHandler = BeanManager.dailyLogService();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chatService = BeanManager.chatService();
    //images
    Image imageView = new Image("/img/sendChat.png");
    image.setImage(imageView);
    image.setFitWidth(30);
    image.setFitHeight(30);

    textProperty();
    getChatData();
    setListView();
  }

  private void textProperty() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        command.setText(null);
        command.requestFocus();
      }
    });
  }

  @FXML
  void onSend(MouseEvent event) {
    send();
  }

  @FXML
  private void onEnter() {
    send();
  }

  private String timeToday() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
    return formatter.format(LocalTime.now());
  }

  private void send() {
    String commands = this.command.getText();

    if (commands != null) {
      addItem(listView, commands);

      ChatService service = new ChatService();
      Chat chats = service.sendChat(new Chat("Me " + timeToday(), timeToday()));
      Chat answers = service.sendChat(new Chat(commands, timeToday()));
      System.out.println(answers);
      listView.getItems().add(chatService.pushCommand(commands));
      //switch breaks
      switch (commands.toLowerCase()) {
        case "in":
        case "login":
        case "log in":
          activityHandler.saveActivity(Activity.ONLINE);
          dailyLogHandler.changeLog(LogStatus.IN.getStatus());
          break;
        case "lb":
        case "lunch":
        case "lunch break":
          activityHandler.saveActivity(Activity.LB);
          dailyLogHandler.changeLog(LogStatus.LB.getStatus());
          break;
        case "bfb":
        case "back from lunch":
        case "back from break":
          activityHandler.saveActivity(Activity.BFB);
          dailyLogHandler.changeLog(LogStatus.BFB.getStatus());
          break;
        case "out":
        case "logout":
        case "log out":
          activityHandler.saveActivity(Activity.OFFLINE);
          dailyLogHandler.changeLog(LogStatus.OUT.getStatus());
          break;
        case "break":
          activityHandler.saveActivity(Activity.BREAK);
          break;
      }
    }

    setListView();
    command.setText(null);
    command.requestFocus();
  }

  private <T> void addItem(ListView<T> listView, T item) {
    List<T> items = listView.getItems();
    int index = items.size();
    items.add((T) ("Me " + timeToday()));
    items.add(item);
    listView.scrollTo(index);
  }

  private void getChatData() {
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

  private void setListView() {
    listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> param) {
        final ListCell cell = new ListCell() {
          private Text text;

          @Override
          public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
              text = new Text(item.toString());
              setWrapText(true);
              setGraphic(text);
//              if ((getIndex()) %6 < 3 ){
//                setStyle("-fx-background-color: #EFF8FD;");
//              }
            }
          }
        };
        return cell;
      }

    });
  }

}
