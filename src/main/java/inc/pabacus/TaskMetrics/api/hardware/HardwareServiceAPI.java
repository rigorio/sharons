package inc.pabacus.TaskMetrics.api.hardware;

import com.google.gson.Gson;
import okhttp3.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HardwareServiceAPI {

    private static final String HOST = "http://localhost:8080";
    private HardwareService hardwareService;

    public void sendHardwareData(){

        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            System.out.println(dateFormat.format(cal.getTime()));

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
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "08af0720-79cc-ff3d-2a7d-f208202e5ec0")
                    .build();

            Response response = client.newCall(request).execute();

        }catch (Exception x){
            x.printStackTrace();}
    }
}
