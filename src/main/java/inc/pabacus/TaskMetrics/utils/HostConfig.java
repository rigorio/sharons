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
  private static final String KEY = "host";
  private String hris = "https://hureyweb-staging.azurewebsites.net";

  public String getHris() {
    return hris;
  }

  public HostConfig() {
    FlatFileSettings flatFileSettings = new DefaultSettings();
    flatFileSettings.setFileName("host.json");
    hostRepository = new FlatFileRepository(flatFileSettings);
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


}
