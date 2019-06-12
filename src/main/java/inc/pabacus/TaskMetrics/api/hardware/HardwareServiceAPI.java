package inc.pabacus.TaskMetrics.api.hardware;

import com.google.gson.Gson;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import javafx.application.Platform;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HardwareServiceAPI {

  private static final Logger logger = Logger.getLogger(HardwareServiceAPI.class);
  private static final String HOST = "http://localhost:8080";
  private HardwareService hardwareService;
  private ScheduledFuture<?> scheduledFuture;

  public void sendHardwareData() {

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable task = () -> Platform.runLater(() -> {

      try {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
//            System.out.println(dateFormat.format(cal.getTime()));

        hardwareService = new WindowsHardwareHandler();

        List<HardwareData> disks = hardwareService.getDisks();
        List<HardwareData> displays = hardwareService.getDisplays();
        List<HardwareData> usbDevices = hardwareService.getUsbDevices();

        String JsonDisks = new Gson().toJson(disks);
        String JsonDisplays = new Gson().toJson(displays);
        String JsonUsbDevices = new Gson().toJson(usbDevices);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\n\t\"timeStamp\":\"" + dateFormat.format(cal.getTime()) + "\", \n\t\"disks\":" + JsonDisks + ",\n\t\"displays\":" + JsonDisplays + ",\n\t\"usbDevices\":" + JsonUsbDevices + "}]");
        Request request = new Request.Builder()
            .url(HOST + "/api/runningHardwares")
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", TokenRepository.getToken().getToken())
            .post(body)
            .build();

        Response response = client.newCall(request).execute();

      } catch (Exception x) {
        logger.warn(x.getMessage());
      }

    });
    //execute every 5 minutes
    scheduledFuture = executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
  }

  public void cancel() {
    scheduledFuture.cancel(true);
  }
}
