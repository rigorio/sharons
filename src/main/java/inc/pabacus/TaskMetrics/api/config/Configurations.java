package inc.pabacus.TaskMetrics.api.config;

import inc.pabacus.TaskMetrics.api.tasks.TaskRepository;
import inc.pabacus.TaskMetrics.api.tasks.TaskWebRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

  @Bean
  public TaskRepository taskRepository() {
    return new TaskWebRepository();
  }

}
