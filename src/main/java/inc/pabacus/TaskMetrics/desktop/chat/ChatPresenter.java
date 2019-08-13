package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXListView;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.chat.Chat;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.HRISLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.LogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.controlsfx.control.Notifications;
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
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private ActivityHandler activityHandler;
  private LogService logService;
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private StringCacheService cacheService;

  public ChatPresenter() {
    HOST = hostConfig.getHost();
    activityHandler = BeanManager.activityHandler();
    logService = new HRISLogHandler();
    cacheService = new StringCacheService();
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

  private Integer checking = 0;

  private void send() {
    String commands = this.command.getText();
    if (commands.toLowerCase().equalsIgnoreCase("clear")) {
      listView.getItems().clear();
    } else if (commands.toLowerCase().equalsIgnoreCase("leave approval") || commands.toLowerCase().equalsIgnoreCase("leave approvals")) {
      addItem(listView, commands);
      if (checking.equals(1))
        listView.getItems().add("TRIBELY: 1. Carlo Montemayor - Sick Leave - 07/11/2019-07/11/2019");
      else if (checking.equals(3))
        listView.getItems().add("TRIBELY: 1. Christopher May Nebril - Vacation Leave - 07/30/2019-07/30/2019");
      else if (checking.equals(0))
        listView.getItems().add("TRIBELY: 1. Christopher May Nebril - Vacation Leave - 07/30/2019-07/30/2019 | 2. Carlo Montemayor - Sick Leave - 07/11/2019-07/11/2019");
      else listView.getItems().add("TRIBELY: Nothing to approve");
    } else if (commands.toLowerCase().equalsIgnoreCase("1 approve") || commands.toLowerCase().equalsIgnoreCase("1 approved")) {
      addItem(listView, commands);
      if (checking.equals(3))
        checking = 2;
      else checking = 1;
      listView.getItems().add("TRIBELY: Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("2 approve") || commands.toLowerCase().equalsIgnoreCase("2 approved")) {
      addItem(listView, commands);
      if (checking.equals(1))
        checking = 2;
      else checking = 3;
      listView.getItems().add("TRIBELY: Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("1 approve and 2 approve") || commands.toLowerCase().equalsIgnoreCase("1 approved and 2 approved")) {
      addItem(listView, commands);
      checking = 2;
      listView.getItems().add("TRIBELY: Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("my leave")) {
      addItem(listView, commands);
      checking = 2;
      listView.getItems().add("TRIBELY: Type:" + ChatService.typeOfRequest + ", Date: " + ChatService.leaveDate + ", Status:" + ChatService.status);
    } else if (commands != null) {
      addItem(listView, commands);

      ChatService service = new ChatService();
      Chat chats = service.sendChat(new Chat("Me " + timeToday(), timeToday()));
      Chat answers = service.sendChat(new Chat(commands, timeToday()));
      System.out.println(answers);
      listView.getItems().add(chatService.pushCommand(commands));
      //switch breaks
      LogStatus status = null;
      switch (commands.toLowerCase()) {
        case "in":
        case "login":
        case "log in":
          activityHandler.saveTimestamp(Activity.ONLINE);
          status = LogStatus.IN;
          notification("Log in");
          break;
        case "lb":
        case "lunch":
        case "lunch break":
          activityHandler.saveTimestamp(Activity.LB);
          status = LogStatus.LB;
          notification("Lunch Break");
          GuiManager.getInstance().displayView(new BreakView());
          break;
        case "bfb":
        case "back from lunch":
        case "back from break":
          activityHandler.saveTimestamp(Activity.BFB);
          status = LogStatus.BFB;
          notification("Back From Break");
          break;
        case "out":
        case "logout":
        case "log out":
          activityHandler.saveTimestamp(Activity.OFFLINE);
          status = LogStatus.OUT;
          notification("Log out");
          break;
        case "break":
          activityHandler.saveTimestamp(Activity.BREAK);
          notification("Break");
          GuiManager.getInstance().displayView(new BreakView());
          break;
      }
      logService.changeLog(status.getStatus());
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

    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/chats")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", cacheService.get(CacheKey.TRIBELY_TOKEN))
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
              if ((getIndex()) % 6 < 3) {
                setStyle("-fx-background-color: #EFF8FD;");
              } else setStyle("-fx-background-color: #FFFFFF;");
            }
          }
        };
        return cell;
      }

    });
  }

  private void notification(String notif) {
    Notifications notifications = Notifications.create()
        .title("TRIBELY")
        .text("Status changed to " + notif)
        .position(Pos.BOTTOM_RIGHT)
        .hideAfter(Duration.seconds(5));
    notifications.darkStyle();
    notifications.showWarning();
  }

}
