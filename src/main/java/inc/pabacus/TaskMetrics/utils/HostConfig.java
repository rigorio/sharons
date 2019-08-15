package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.utils.file.DefaultSettings;
import inc.pabacus.TaskMetrics.utils.file.FlatFileRepository;
import inc.pabacus.TaskMetrics.utils.file.FlatFileSettings;
import inc.pabacus.TaskMetrics.utils.file.Repository;
import javafx.scene.control.TextInputDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HostConfig {

  private Repository<Map<String, Object>, Long> hostRepository;
  private Repository<Map<String, Object>, Long> hrisRepository;
  private static final String KEY = "tribelyHost";
  private String hris = "https://hureyweb-staging.azurewebsites.net";
  private static final String HRIS_KEY = "hrisHost";

  public String getHris() {
    return hris;
  }

  public HostConfig() {
    FlatFileSettings flatFileSettings = new DefaultSettings();
    flatFileSettings.setFileName("tribelyHost.json"); // so we can run two tribely apps at the same time
    hostRepository = new FlatFileRepository(flatFileSettings);

    FlatFileSettings hrisFFS = new DefaultSettings();
    hrisFFS.setFileName("hrisHost.json"); // so we can run two tribely apps at the same time
    hrisRepository = new FlatFileRepository(hrisFFS);
  }

  public String getHrisHost() {
    Optional<Map<String, Object>> allList = hrisRepository.findAll().stream().findAny();
    if (!allList.isPresent()) {
      HashMap<String, Object> map = new HashMap<>();
      map.put(HRIS_KEY, "");
      hrisRepository.save(map);
      allList = hrisRepository.findAll().stream().findAny();
    }
    Map<String, Object> hrisFile = allList.get();
    Object value = hrisFile.get(HRIS_KEY);
    return value == null ? "" : value.toString();
  }

  public String getHost() {
    Optional<Map<String, Object>> allList = hostRepository.findAll().stream().findAny();

    if (!allList.isPresent()) {
      HashMap<String, Object> map = new HashMap<>();
      map.put(KEY, "");
      hostRepository.save(map);
      allList = hostRepository.findAll().stream().findAny();
    }

    Map<String, Object> any = allList.get();


    Map<String, Object> sampMAp = new HashMap<>();
    String host = "";
    if (any.get(KEY).equals("") || !any.containsKey(KEY)) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Error");
      dialog.setHeaderText("Host has not been set");
      dialog.setContentText("Please enter host url:");

// Traditional way to get the response value.
      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {

        host = result.get();
      }
      sampMAp.put(KEY, host);
      hostRepository.save(sampMAp); // to write a file
      return (String) hostRepository.findAll().stream().findAny().get().get(KEY);
    } else {

//      Map<String, Object> hostSettings = any.get();
      Object value = any.get(KEY);
      return (String) value;
    }
  }

  public void updateHost(String host) {

    Map<String, Object> map = new HashMap<>();
    map.put(KEY, host);
    Map<String, Object> savedMap = hostRepository.save(map);
    System.out.println(savedMap);

  }

  public void updateHureyHost(String host) {

    Map<String, Object> map = new HashMap<>();
    map.put(HRIS_KEY, host);
    Map<String, Object> savedMap = hrisRepository.save(map);
    System.out.println(savedMap);

  }

}
