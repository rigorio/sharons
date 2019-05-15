package inc.pabacus.TaskMetrics.api.software;
import com.google.gson.Gson;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SoftwareServiceAPI {
    private static final String HOST = "http://localhost:8080";
    private SoftwareService softwareService;

    public void sendSoftwareData() {
        getSoftwareMonitoringMinutes();
        String minutes = getSoftwareMonitoringMinutes();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            try{

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                System.out.println(dateFormat.format(cal.getTime()));

                softwareService = new SoftwareHandler();
                List<SoftwareData> allSoftware = softwareService.getSoftware();

                String json = new Gson().toJson(allSoftware);

                OkHttpClient client = new OkHttpClient();
                System.out.println(json);
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "[{\n\t\"timeStamp\":\"" + dateFormat.format(cal.getTime()) + "\", \n\t\"runningSoftwares\":" + json +  "}]");
                Request request = new Request.Builder()
                        .url(HOST + "/api/runningSoftwares")
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "08af0720-79cc-ff3d-2a7d-f208202e5ec0")
                        .addHeader("Authorization", TokenRepository.getToken().getToken())
                        .build();

                Response response = client.newCall(request).execute();

            }catch (Exception x){
                x.printStackTrace();}

        };
        executor.scheduleWithFixedDelay(task, 0, Long.parseLong(minutes), TimeUnit.MINUTES);

    }

    public String getSoftwareMonitoringMinutes(){
        OkHttpClient client = new OkHttpClient();
        // code request code here
        Request request = new Request.Builder()
                .url(HOST + "/api/monitorSoftware")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", TokenRepository.getToken().getToken())
                .method("GET", null)
                .build();

        String getSoftwareMonitoringMinutes = null;

        try {

            Response response = client.newCall(request).execute();
            String getTimes = response.body().string();

            if(getTimes.equals("[]") || getTimes.equals("")){
                // default value of 5 minutes
                getSoftwareMonitoringMinutes = "5";
            }
            else {
                String getTimeJson = getTimes.replaceAll("\\[|\\]", "");
                JSONObject json = new JSONObject(getTimeJson);
                getSoftwareMonitoringMinutes = json.getString("minutes");
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return getSoftwareMonitoringMinutes;
    }

}