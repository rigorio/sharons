package inc.pabacus.TaskMetrics.api.leave;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaveService {

  private static final Logger logger = Logger.getLogger(LeaveService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static final String HOST = "http://localhost:8080";

  public Leave requestLeave(Leave leave) {
    try {
      OkHttpClient client = new OkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(leave);
      RequestBody body = RequestBody.create(JSON, jsonString);
      //print expected value
      System.out.println(jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/leave")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      leave = mapper.readValue(responseBody.string(),
                               new TypeReference<Leave>() {});
      return leave;
    } catch (IOException e) {
      logger.warn(e.getMessage());
      return leave;
    }
  }

  public List<Leave> getAll() {
    List<Leave> leaves = new ArrayList<>();
    ArrayList<Approver> approverList = new ArrayList<>();
    approverList.add(Approver.builder()
                         .approverId(1L)
                         .approver("wan")
                         .status("Approved")
                         .build());
    approverList.add(Approver.builder()
                         .approverId(1L)
                         .approver("wan")
                         .status("Rejected")
                         .build());
    approverList.add(Approver.builder()
                         .approverId(1L)
                         .approver("wan")
                         .status("Denied")
                         .build());
    leaves.add(Leave.builder()
                   .id(0L)
                   .userId(2L)
                   .approver(approverList)
                   .startDate("4-2-2019")
                   .endDate("4-4-2019")
                   .reason("kasi kwan")
                   .status("Pending")
                   .typeOfRequest("Maternal")
                   .build());
    approverList.add(Approver.builder()
                         .approverId(1L)
                         .approver("wan")
                         .status("Approved")
                         .build());
    leaves.add(Leave.builder()
                   .id(0L)
                   .userId(2L)
                   .approver(approverList)
                   .startDate("5-2-2019")
                   .endDate("5-22-2019")
                   .reason("kasi kwan")
                   .status("Approved")
                   .typeOfRequest("Maternal")
                   .build());
    approverList.add(Approver.builder()
                         .approverId(1L)
                         .approver("wan")
                         .status("Approved")
                         .build());
    leaves.add(Leave.builder()
                   .id(0L)
                   .userId(2L)
                   .approver(approverList)
                   .startDate("7-5-2019")
                   .endDate("7-12-2019")
                   .reason("kasi kwan")
                   .status("Pending")
                   .typeOfRequest("Maternal")
                   .build());
    return leaves;
  }
}
