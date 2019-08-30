package inc.pabacus.TaskMetrics.utils.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

public class LogHelper {
  private Logger logger;
  private ObjectMapper mapper = new ObjectMapper();
  private Class<?> _class;

  public LogHelper(Logger logger) {
    this.logger = logger;
  }


  public void setClass(Class<?> _class) {
    this._class = _class;
  }

  public void logInfo(final String action, Object task) {
    try {
      logger.info("[" + _class.toString() + "] " + action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void logWarning(final String action, Object task) {
    try {
      logger.warn("[" + _class.toString() + "] " + action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void logError(final String action, Object task) {
    try {
      logger.error("[" + _class.toString() + "] " + action + (task != null ? ": " + mapper.writeValueAsString(task) : null));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
