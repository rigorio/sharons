package inc.pabacus.TaskMetrics.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

  @Bean
  public TaskRepository taskRepository() {
    return new TaskWebRepository();
  }

}
