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
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();

  public Leave saveLeave(Leave leave) {
    try {

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
    Approver approverRigo = Approver.builder()
        .approverId(1L)
        .approver("Rigo Sarmiento")
        .status("Pending")
        .build();
    approverList.add(approverRigo);
    Approver approverMae = Approver.builder()
        .approverId(2L)
        .approver("Rose Mae Cayabyab")
        .status("Pending")
        .build();
    approverList.add(approverMae);
    Approver approverJoy = Approver.builder()
        .approverId(3L)
        .approver("Joy Cuison")
        .status("Pending")
        .build();
    approverList.add(approverJoy);
    leaves.add(Leave.builder()
                   .id(1L)
                   .userId(2L)
                   .approvers(approverList)
                   .startDate("4-2-2019")
                   .endDate("4-4-2019")
                   .reason("Family Outing")
                   .status("Pending")
                   .typeOfRequest("Vacation Leave")
                   .build());
    List<Approver> approverList2 = new ArrayList<>();
    approverList2.add(approverMae);
    Approver approverCarlo = Approver.builder()
        .approverId(4L)
        .approver("Carlo Montemayor")
        .status("Pending")
        .build();
    approverList.add(approverCarlo);
    leaves.add(Leave.builder()
                   .id(2L)
                   .userId(2L)
                   .approvers(approverList2)
                   .startDate("5-2-2019")
                   .endDate("5-22-2019")
                   .reason("At the hospital")
                   .status("Pending")
                   .typeOfRequest("Sick Leave")
                   .build());
    List<Approver> approverList3 = new ArrayList<>();
    approverList3.add(approverCarlo);
    approverList3.add(Approver.builder()
                          .approverId(5L)
                          .approver("Chris Nebril")
                          .status("Pending")
                          .build());
    approverList3.add(approverRigo);
    leaves.add(Leave.builder()
                   .id(3L)
                   .userId(2L)
                   .approvers(approverList3)
                   .startDate("7-5-2019")
                   .endDate("7-12-2019")
                   .reason("Child birth")
                   .status("Pending")
                   .typeOfRequest("Maternal")
                   .build());
    return leaves;
  }
}
