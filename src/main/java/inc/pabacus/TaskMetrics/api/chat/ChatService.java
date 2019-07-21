package inc.pabacus.TaskMetrics.api.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.leave.Approver;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.desktop.leave.LeaveView;
import inc.pabacus.TaskMetrics.desktop.status.StatusView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChatService {


  private static final Logger logger = Logger.getLogger(ChatService.class);
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  ActivityHandler activityHandler = new ActivityHandler();
  public static String leaveDate;
  private static String startDate;
  private static String endDate;
  public static String typeOfRequest;
  public static String status = "Pending";

  public ChatService() {
    HOST = new HostConfig().getHost();
  }

  public void sendCommand(String command) {
    switch (command.toLowerCase()) {

      case "change status":
      case "status":
        GuiManager.getInstance().displayView(new StatusView());
        break;
      case "leave":
      case "leave form":
        GuiManager.getInstance().displayView(new LeaveView());
        break;
    }
  }

  public String pushCommand(String command) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
    ChatService service = new ChatService();
    String Tribely = "TRIBELY: ";
    List<Approver> getApprovers = Arrays.asList(new Approver(3L, "Rose Cayabyab", status),
                                                new Approver(4L, "Joy Cuison", status));

    switch (command.toLowerCase()) {
//    for Leaves
      case "leave":
        service.sendChat(new Chat(Tribely + "What type of leave?", formatter.format(LocalTime.now())));
        return Tribely + "What type of leave?";
      case "leave form":
        GuiManager.getInstance().displayView(new LeaveView());
        return Tribely + "Showing leave form";
      case "vl":
      case "vacation":
      case "vacation leave":
        service.sendChat(new Chat(Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)", formatter.format(LocalTime.now())));
        typeOfRequest = "Vacation Leave";
        return Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "sl":
      case "sick":
      case "sick leave":
        service.sendChat(new Chat(Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)", formatter.format(LocalTime.now())));
        typeOfRequest = "Sick Leave";
        return Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "maternity":
      case "maternity leave":
        service.sendChat(new Chat(Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)", formatter.format(LocalTime.now())));
        typeOfRequest = "Maternity Leave";
        return Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "paternity":
      case "paternity leave":
        service.sendChat(new Chat(Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)", formatter.format(LocalTime.now())));
        typeOfRequest = "Paternity Leave";
        return Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "magna carta":
      case "magnacarta":
      case "magna carta leave":
      case "magnacarta leave":
      case "MagnaCarta for Women":
        service.sendChat(new Chat(Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)", formatter.format(LocalTime.now())));
        typeOfRequest = "MagnaCarta for Women";
        return Tribely + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
//    for status
      case "status":
      case "my status":
      case "current status":
        service.sendChat(new Chat(Tribely + "Your current status is " + activityHandler.getLastLog(), formatter.format(LocalTime.now())));
        return Tribely + "Your current status is " + activityHandler.getLastLog();
      case "in":
      case "log in":
      case "login":
        service.sendChat(new Chat(Tribely + "Logged in!", formatter.format(LocalTime.now())));
        return Tribely + "Logged in!";
      case "lb":
      case "lunch":
      case "lunch break":
        service.sendChat(new Chat(Tribely + "Status change to Lunch Break!", formatter.format(LocalTime.now())));
        return Tribely + "Status change to Lunch Break!";
      case "bfb":
      case "back from lunch":
      case "back from break":
        service.sendChat(new Chat(Tribely + "Status change to Back From Break!", formatter.format(LocalTime.now())));
        return Tribely + "Status change to Back From Break!";
      case "out":
      case "logout":
      case "log out":
        service.sendChat(new Chat(Tribely + "Successfully Logged out", formatter.format(LocalTime.now())));
        return Tribely + "Successfully Logged out";
      case "break":
        service.sendChat(new Chat(Tribely + "Status change to Break!", formatter.format(LocalTime.now())));
        return Tribely + "Status change to Break!";
    }

    if (isValidDate(command)) {
      service.sendChat(new Chat(Tribely + "Success!", formatter.format(LocalTime.now())));
      leaveDate = command;
      String split[] = command.split("-");
      startDate = split[0];
      endDate = split[1];
      LeaveService services = new LeaveService();
      Leave leave = services.saveLeave(new Leave(2L, 3L, getApprovers, startDate, endDate, "testing", status, typeOfRequest));
      System.out.println("--------------------");
      System.out.println(leave);
      return Tribely + "Success!";
    } else {
      service.sendChat(new Chat(Tribely + "I don't know what you are talking about. I am still learning.", formatter.format(LocalTime.now())));
      return Tribely + "I don't know what you are talking about. I am still learning.";
    }
  }

  private static boolean isValidDate(String inDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy-MM/dd/yyyy");
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(inDate.trim());
    } catch (ParseException pe) {
      return false;
    }
    return true;
  }

  public Chat sendChat(Chat chat) {
    try {
      OkHttpClient client = SslUtil.getSslOkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(chat);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/chat")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      chat = mapper.readValue(responseBody.string(),
                              new TypeReference<Chat>() {});
      return chat;
    } catch (IOException e) {

      return chat;
    }
  }

}
