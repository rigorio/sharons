package inc.pabacus.TaskMetrics.api;

import org.junit.Ignore;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class Tester {

  @Test
  @Ignore
  public void testDoubleRoundup() {
    timer.accept(() -> {
      double a = 300.0 / 3600.0;
      String s = String.format("%.2f", a);
    });

    timer.accept(() -> {
      double jikan = 300.0 / 3600.0;
      DecimalFormat df = new DecimalFormat("0.00");
      String ans = "" + Double.parseDouble(df.format(jikan));
    });
  }

  private Consumer<Runnable> timer = (runnable) -> {
    Long start = System.nanoTime();
    runnable.run();
    System.out.println("Total time is " + (System.nanoTime() - start));
  };

//  @Test
//  public void testValidation() {
//    ValidationService validationService = new ValidationService();
//    List<ActivityTimestamp> invalidStatuses = validationService.validation();
//    System.out.println("-------------oooh");
//    invalidStatuses.forEach(System.out::println);
//  }
}
