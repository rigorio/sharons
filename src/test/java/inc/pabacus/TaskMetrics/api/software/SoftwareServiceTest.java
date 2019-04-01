package inc.pabacus.TaskMetrics.api.software;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class SoftwareServiceTest {

  private SoftwareService softwareService = new SoftwareHandler();

  @Test
  public void testSoftware() {
    List<SoftwareData> softwareServiceSoftware = softwareService.getSoftware();
    softwareServiceSoftware.forEach(System.out::println);
    assertFalse(softwareServiceSoftware.size() <= 0);
  }
}
