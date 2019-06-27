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
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  ActivityHandler activityHandler = new ActivityHandler();
  private static String startDate;
  private static String endDate;
  private static String typeOfRequest;
  private static String status = "Pending";

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
    String Systems = "System: ";
    List<Approver> getApprovers = Arrays.asList(new Approver(3L, "Rose Cayabyab", status),
                                                new Approver(4L, "Joy Cuison", status));

    switch (command.toLowerCase()) {
//    for Leaves
      case "leave":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        return Systems + "What type of leave?";
      case "leave form":
        GuiManager.getInstance().displayView(new LeaveView());
        return Systems + "Showing leave form";
      case "vl":
      case "vacation":
      case "vacation leave":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        typeOfRequest = "Vacation Leave";
        return Systems + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "sl":
      case "sick":
      case "sick leave":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        typeOfRequest = "Sick Leave";
        return Systems + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "maternity":
      case "maternity leave":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        typeOfRequest = "Maternity Leave";
        return Systems + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "paternity":
      case "paternity leave":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        typeOfRequest = "Paternity Leave";
        return Systems + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
      case "magna carta":
      case "magnacarta":
      case "magna carta leave":
      case "magnacarta leave":
      case "MagnaCarta for Women":
        service.sendChat(new Chat(Systems + "What type of leave?", formatter.format(LocalTime.now())));
        typeOfRequest = "MagnaCarta for Women";
        return Systems + "When? Start Date and End Date (MM/dd/yyyy-MM/dd/yyyy)";
//    for status
      case "status":
      case "my status":
      case "current status":
        service.sendChat(new Chat(Systems + "Your current status is " + activityHandler.getLastLog(), formatter.format(LocalTime.now())));
        return Systems + "Your current status is " + activityHandler.getLastLog();
      case "in":
      case "log in":
      case "login":
        service.sendChat(new Chat(Systems + "Logged in!", formatter.format(LocalTime.now())));
        return Systems + "Logged in!";
      case "otl":
      case "out to lunch":
        service.sendChat(new Chat(Systems + "Status change to Out To Lunch!", formatter.format(LocalTime.now())));
        return Systems + "Status change to Out To Lunch!";
      case "bfl":
      case "back from lunch":
        service.sendChat(new Chat(Systems + "Status change to Back From Lunch!", formatter.format(LocalTime.now())));
        return Systems + "Status change to Back From Lunch!";
      case "out":
      case "logout":
      case "log out":
        service.sendChat(new Chat(Systems + "Successfully Logged out", formatter.format(LocalTime.now())));
        return Systems + "Successfully Logged out";
      case "break":
        service.sendChat(new Chat(Systems + "Status change to Break!", formatter.format(LocalTime.now())));
        return Systems + "Status change to Break!";
    }

    if (isValidDate(command)) {
      service.sendChat(new Chat(Systems + "Success!", formatter.format(LocalTime.now())));
      String split[] = command.split("-");
      startDate = split[0];
      endDate = split[1];
      LeaveService services = new LeaveService();
      Leave leave = services.saveLeave(new Leave(2L, 3L, getApprovers, startDate, endDate, "testing", status, typeOfRequest));
      // maybe log this
      return Systems + "Success!";
    } else {
      service.sendChat(new Chat(Systems + "Invalid Command!", formatter.format(LocalTime.now())));
      return Systems + "Invalid Command!";
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
      OkHttpClient client = new OkHttpClient();
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
