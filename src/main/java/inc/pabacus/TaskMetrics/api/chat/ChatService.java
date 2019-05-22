package inc.pabacus.TaskMetrics.api.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.desktop.leave.LeaveView;
import inc.pabacus.TaskMetrics.desktop.status.StatusView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class ChatService {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
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

}
