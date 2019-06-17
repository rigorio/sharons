package inc.pabacus.TaskMetrics.api.leave;

import javax.persistence.Entity;

@Entity
public class Approver {
  private double teamLeader;
  private double supervisorId;
  private double managerId;

  public Approver() {
  }

  public Approver(double teamLeader, double supervisorId, double managerId) {
    this.teamLeader = teamLeader;
    this.supervisorId = supervisorId;
    this.managerId = managerId;
  }

  public double getTeamLeader() {
    return teamLeader;
  }

  public void setTeamLeader(double teamLeader) {
    this.teamLeader = teamLeader;
  }

  public double getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(double supervisorId) {
    this.supervisorId = supervisorId;
  }

  public double getManagerId() {
    return managerId;
  }

  public void setManagerId(double managerId) {
    this.managerId = managerId;
  }
}
