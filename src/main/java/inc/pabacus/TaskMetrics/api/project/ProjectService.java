package inc.pabacus.TaskMetrics.api.project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
  Project saveProject(Project project);

  Optional<Project> getProject(Long id);

  List<Project> getAllProjects();

  List<ProjectFXAdapter> getAllFXProjects();
}
