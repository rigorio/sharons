package inc.pabacus.TaskMetrics.api.project;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectHandler implements ProjectService {

  private ProjectRepository repository;

  public ProjectHandler() {
    repository = new ProjectWebRepository();
  }

  @Override
  public Project saveProject(Project project) {
    return repository.save(project);
  }

  @Override
  public Optional<Project> getProject(Long id) {
    return repository.findById(id);
  }

  @Override
  public List<Project> getAllProjects() {
    return repository.findAll();
  }

  @Override
  public List<ProjectFXAdapter> getAllFXProjects() {
    return repository.findAll().stream()
        .map(ProjectFXAdapter::new)
        .collect(Collectors.toList());
  }

}
