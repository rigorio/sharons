package inc.pabacus.TaskMetrics.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import okhttp3.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

public class Tester {

  @Test
  @Ignore
  public void testDoubleRoundup() {
    timer.accept(() -> {
      double a = 300.0 / 3600.0;
      String s = String.format("%.2f", a);
    });

    timer.accept(() -> {
      double jikan = 300.0 / 3600.0;
      DecimalFormat df = new DecimalFormat("0.00");
      String ans = "" + Double.parseDouble(df.format(jikan));
    });
  }

  private Consumer<Runnable> timer = (runnable) -> {
    Long start = System.nanoTime();
    runnable.run();
    System.out.println("Total time is " + (System.nanoTime() - start));
  };

  @Test
  @Ignore
  public void testingLeaves() throws IOException {
    LeaveService leaveService = new LeaveService();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String jsonString = new ObjectMapper().writeValueAsString(leaveService.getAll());
    RequestBody body = RequestBody.create(JSON, jsonString);
    Call call = new OkHttpClient()
        .newCall(new Request.Builder()
                     .url("http://localhost/api/user/leaves")
                     .post(body)
                     .build());

    String responseString = call.execute().body().string();

    List<Leave> leaves = new ObjectMapper().readValue(responseString, new TypeReference<List<Leave>>() {});
    leaves.forEach(System.out::println);
  }
}
