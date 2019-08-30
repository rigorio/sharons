package inc.pabacus.TaskMetrics.utils.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

public class LogHelper {
  private Logger logger;
  private ObjectMapper mapper = new ObjectMapper();

  public LogHelper(Logger logger) {
    this.logger = logger;
  }


  public void logInfo(final String action, Object task) {
    try {
      logger.info(action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void logWarning(final String action, Object task) {
    try {
      logger.warn(action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void logError(final String action, Object task) {
    try {
      logger.error(action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}
