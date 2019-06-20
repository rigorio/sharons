package inc.pabacus.TaskMetrics.api.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.desktop.leave.LeaveView;
import inc.pabacus.TaskMetrics.desktop.status.StatusView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ChatService {

  private static final Logger logger = Logger.getLogger(ChatService.class);
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");


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
