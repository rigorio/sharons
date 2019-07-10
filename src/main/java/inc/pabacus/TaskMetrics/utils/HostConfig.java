package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.utils.what.DefaultSettings;
import inc.pabacus.TaskMetrics.utils.what.FlatFileRepository;
import inc.pabacus.TaskMetrics.utils.what.FlatFileSettings;
import inc.pabacus.TaskMetrics.utils.what.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HostConfig {

  private Repository<Map<String, Object>, Long> hostRepository;
  private static final String KEY = "host";

  public HostConfig() {
    FlatFileSettings flatFileSettings = new DefaultSettings();
    flatFileSettings.setFileName("host.json");
    hostRepository = new FlatFileRepository(flatFileSettings);
  }

  public String getHost() {
    Optional<Map<String, Object>> any = hostRepository.findAll().stream().findAny();
    if (!any.isPresent())
      hostRepository.save(new HashMap<>()); // to write a file
    Map<String, Object> hostSettings = any.get();
    return (String) hostSettings.get(KEY);
  }

  public void updateHost(String host) {

    Map<String, Object> map = new HashMap<>();
    map.put(KEY, host);
    Map<String, Object> savedMap = hostRepository.save(map);
    System.out.println(savedMap);

  }


}
