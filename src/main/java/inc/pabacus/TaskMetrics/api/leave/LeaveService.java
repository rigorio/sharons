package inc.pabacus.TaskMetrics.api.leave;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.api.cacheService.CacheService;
import inc.pabacus.TaskMetrics.api.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.desktop.leaveViewer.LeaveHolder;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaveService {

  private static final Logger logger = Logger.getLogger(LeaveService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private CacheService<CacheKey, String> cacheService;
  private final HostConfig hostConfig;

  public LeaveService() {
    hostConfig = new HostConfig();
    cacheService = new StringCacheService();
  }

  public List<Leave> getAllLeaves() {
    List<Leave> leaves = new ArrayList<>();
    String accessToken = cacheService.get(CacheKey.SHRIS_TOKEN);
    String employeeId = cacheService.get(CacheKey.EMPLOYEE_ID);

    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHris() + "/api/services/app/LeaveRequest/GetAllByManagerEmployeeId?employeeId=" + employeeId)
                                     .addHeader("Authorization", accessToken)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      JSONObject jsonObject = new JSONObject(responseString);
      System.out.println(jsonObject);

      JSONArray arr = jsonObject.getJSONArray("result");
      for (int i = 0; i < arr.length(); ++i) {
        Leave leave = new Leave();
        //pass API using setters
        JSONObject object = arr.getJSONObject(i);
        if (object.getString("managerEmployeeId").equals(employeeId)) {
          leave.setId(object.getLong("id"));
          leave.setEmployeeId(object.getString("employeeId"));
          leave.setReason(object.getString("reason"));
          leave.setAmount(object.getLong("amount"));
          leave.setStartDate(object.getString("startDate"));
          leave.setEndDate(object.getString("endDate"));
          leave.setLeaveTypeId(getLeaveType(object.getLong("leaveTypeId")));

          if (object.getLong("status") == 0) {
            leave.setStatus("Pending");
          } else if (object.getLong("status") == -1) {
            leave.setStatus("Denied");
          } else {
            leave.setStatus("Approved");
          }

          leaves.add(leave);
        }

      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return leaves;
  }

  private String getLeaveType(Long id) {
    String accessToken = cacheService.get(CacheKey.SHRIS_TOKEN);
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHris() + "/api/services/app/LeaveType/GetAll")
                                     .addHeader("Authorization", accessToken)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      JSONObject jsonObject = new JSONObject(responseString);

      JSONObject result = jsonObject.getJSONObject("result");
      JSONArray arr = result.getJSONArray("items");
      for (int i = 0; i < arr.length(); ++i) {
        JSONObject object = arr.getJSONObject(i);
        if (object.getLong("id") == id) {
          return object.getString("name");
        }

      }
    } catch (IOException e) {
      logger.warn(e.getMessage());

    }
    return null;
  }

  public String saveLeave(Long leaveId) {
    String accessToken = cacheService.get(CacheKey.SHRIS_TOKEN);
    Leave leave = LeaveHolder.getLeave();
    String request = null;
    if (leave.getStatus().equals("Approved")) {
      request = "Approve";
    } else request = "Reject";
    System.out.println(leave.getStatus());

    try {

      String jsonString = mapper.writeValueAsString("");
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHris() + "/api/services/app/LeaveRequest/" + request + "?id=" + leaveId)
                                     .addHeader("Authorization", accessToken)
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      System.out.println(responseString);
      return "Success";

    } catch (IOException e) {
      logger.warn(e.getMessage());
      return "error";
    }
  }

//  public List<Leave> getAll() {
//    List<Leave> leaves = new ArrayList<>();
//    ArrayList<Approver> approverList = new ArrayList<>();
//    Approver approverRigo = Approver.builder()
//        .approverId(1L)
//        .approver("Rigo Sarmiento")
//        .status("Pending")
//        .build();
//    approverList.add(approverRigo);
//    Approver approverMae = Approver.builder()
//        .approverId(2L)
//        .approver("Rose Mae Cayabyab")
//        .status("Pending")
//        .build();
//    approverList.add(approverMae);
//    Approver approverJoy = Approver.builder()
//        .approverId(3L)
//        .approver("Joy Cuison")
//        .status("Pending")
//        .build();
//    approverList.add(approverJoy);
//    leaves.add(Leave.builder()
//                   .id(1L)
//                   .userId(2L)
//                   .approvers(approverList)
//                   .startDate("4-2-2019")
//                   .endDate("4-4-2019")
//                   .reason("Family Outing")
//                   .status("Pending")
//                   .typeOfRequest("Vacation Leave")
//                   .build());
//    List<Approver> approverList2 = new ArrayList<>();
//    approverList2.add(approverMae);
//    Approver approverCarlo = Approver.builder()
//        .approverId(4L)
//        .approver("Carlo Montemayor")
//        .status("Pending")
//        .build();
//    approverList.add(approverCarlo);
//    leaves.add(Leave.builder()
//                   .id(2L)
//                   .userId(2L)
//                   .approvers(approverList2)
//                   .startDate("5-2-2019")
//                   .endDate("5-22-2019")
//                   .reason("At the hospital")
//                   .status("Pending")
//                   .typeOfRequest("Sick Leave")
//                   .build());
//    List<Approver> approverList3 = new ArrayList<>();
//    approverList3.add(approverCarlo);
//    approverList3.add(Approver.builder()
//                          .approverId(5L)
//                          .approver("Chris Nebril")
//                          .status("Pending")
//                          .build());
//    approverList3.add(approverRigo);
//    leaves.add(Leave.builder()
//                   .id(3L)
//                   .userId(2L)
//                   .approvers(approverList3)
//                   .startDate("7-5-2019")
//                   .endDate("7-12-2019")
//                   .reason("Child birth")
//                   .status("Pending")
//                   .typeOfRequest("Maternal")
//                   .build());
//    return leaves;
//  }
}
