package inc.pabacus.TaskMetrics.desktop.chat;

import com.jfoenix.controls.JFXListView;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.generateToken.UsernameHolder;
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
import inc.pabacus.TaskMetrics.utils.web.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import static inc.pabacus.TaskMetrics.desktop.chat.ChatCell.chatData;

public class ChatPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXListView listView;
  @FXML
  private TextField command;
  @FXML
  private ImageView image;

  private static ChatData chatData = new ChatData();
  private ObservableList<ChatData> chatDataObservableList;

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
      if (checking.equals(1)) {
        addItemTribely(listView, "1. Carlo Montemayor - Sick Leave - 07/11/2019-07/11/2019");
      } else if (checking.equals(3)) {
        addItemTribely(listView, "1. Christopher May Nebril - Vacation Leave - 07/30/2019-07/30/2019");
      } else if (checking.equals(0)) {
        addItemTribely(listView, "1. Christopher May Nebril - Vacation Leave - 07/30/2019-07/30/2019 | 2. Carlo Montemayor - Sick Leave - 07/11/2019-07/11/2019");
      } else {
        addItemTribely(listView, "Nothing to approve");
      }
    } else if (commands.toLowerCase().equalsIgnoreCase("1 approve") || commands.toLowerCase().equalsIgnoreCase("1 approved")) {
      addItem(listView, commands);
      if (checking.equals(3))
        checking = 2;
      else checking = 1;
      addItemTribely(listView, "Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("2 approve") || commands.toLowerCase().equalsIgnoreCase("2 approved")) {
      addItem(listView, commands);
      if (checking.equals(1))
        checking = 2;
      else checking = 3;
      addItemTribely(listView, "Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("1 approve and 2 approve") || commands.toLowerCase().equalsIgnoreCase("1 approved and 2 approved")) {
      addItem(listView, commands);
      checking = 2;
      addItemTribely(listView, "Success!");
    } else if (commands.toLowerCase().equalsIgnoreCase("my leave")) {
      addItem(listView, commands);
      checking = 2;
      addItemTribely(listView,
              "Type:" + ChatService.typeOfRequest + ", Date: " + ChatService.leaveDate + ", Status:" + ChatService.status);
    } else if (commands != null) {
      addItem(listView, commands);

      ChatService service = new ChatService();
      Chat chats = service.sendChat(new Chat(timeToday(), timeToday()));
      Chat answers = service.sendChat(new Chat(commands, timeToday()));
      System.out.println(answers);
      addItemTribely(listView, chatService.pushCommand(commands));
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

    command.setText(null);
    command.requestFocus();

  }

  private void addItemTribely(ListView listView, String item) {
    List items = listView.getItems();
    int index = items.size();

    chatDataObservableList = FXCollections.observableArrayList();

    chatData.setUsername("Tribely");
    chatData.setTime("");
    chatData.setMessage(item);

    chatDataObservableList.add(
            new ChatData(chatData.getUsername(),
                    chatData.getMessage(), chatData.getTime())
    );

    items.addAll(chatDataObservableList);
    listView.setCellFactory(chatView -> new ChatCell());

    listView.scrollTo(index);

    command.setText(null);
    command.requestFocus();
  }

  private void addItem(ListView listView, String item) {
    List items = listView.getItems();
    int index = items.size();

    chatDataObservableList = FXCollections.observableArrayList();

    chatData.setUsername(UsernameHolder.username);
    chatData.setTime(timeToday());
    chatData.setMessage(item);

    chatDataObservableList.add(
            new ChatData(chatData.getUsername(), chatData.getMessage(),
                    chatData.getTime())
    );

    items.addAll(chatDataObservableList);
    listView.setCellFactory(chatView -> new ChatCell());

    listView.scrollTo(index);

    command.setText(null);
    command.requestFocus();
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
        List items = listView.getItems();
        chatDataObservableList = FXCollections.observableArrayList();
        String time;
        String message;
        int index = items.size();

        if (i % 2 == 0) {

          time = jsonobject.getString("time");
          message = jsonobject.getString("message");

          chatData.setUsername(UsernameHolder.username);
          chatData.setTime(time);
          chatData.setMessage(message);

          chatDataObservableList.add(
                  new ChatData(chatData.getUsername(), chatData.getMessage(),
                          chatData.getTime())
          );

          items.addAll(chatDataObservableList);
          listView.setCellFactory(chatView -> new ChatCell());

        } else {
          message = jsonobject.getString("message");

          chatData.setUsername("Tribely");
          chatData.setTime("");
          chatData.setMessage(message);

          chatDataObservableList.add(
                  new ChatData(chatData.getUsername(),
                          chatData.getMessage(), chatData.getTime())
          );
          items.addAll(chatDataObservableList);
          listView.setCellFactory(chatView -> new ChatCell());
        }
        listView.scrollTo(index);
      }

    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
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
