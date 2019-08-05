package inc.pabacus.TaskMetrics.api.hardware;

import com.google.gson.Gson;
import inc.pabacus.TaskMetrics.api.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.api.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
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
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private HardwareService hardwareService;
  private ScheduledFuture<?> scheduledFuture;

  public HardwareServiceAPI() {
    HOST = hostConfig.getHost();
  }

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

        OkHttpClient client = SslUtil.getSslOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"timeStamp\":\"" + dateFormat.format(cal.getTime()) + "\", \n\t\"disks\":" + JsonDisks + ",\n\t\"displays\":" + JsonDisplays + ",\n\t\"usbDevices\":" + JsonUsbDevices + "}");
        Request request = new Request.Builder()
            .url(HOST + "/api/userhardwaresAPI")
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", new StringCacheService().get(CacheKey.TRIBELY_TOKEN))
            .post(body)
            .build();

        Response response = client.newCall(request).execute();

      } catch (Exception x) {
        System.out.println(x.getMessage());
        logger.warn(x.getMessage());
      }

    });
    //execute every 5 minutes
    scheduledFuture = executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
  }

  public void cancel() {
    scheduledFuture.cancel(true);
  }
}
