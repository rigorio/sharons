package rigor.io.Sharons.api.gown.repository;

import rigor.io.Sharons.api.gown.Gown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockGownDataProvider {

  private static ArrayList<Gown> gowns;

  public List<Gown> mockGowns() {
    Gown gown1 = Gown.builder()
        .id(1L)
        .name("Blue bridal")
        .description("with laces")
        .price(100.30)
        .dateRented("2019-12-04")
        .dueDate("2019-12-31")
        .build();
    Gown gown2 = Gown.builder()
        .id(2L)
        .name("Red bridal")
        .description("without laces")
        .price(105.30)
        .dateRented("2019-12-04")
        .dueDate("2019-12-31")
        .build();
    Gown gown3 = Gown.builder()
        .id(3L)
        .name("Long coat")
        .description("color black")
        .price(900.30)
        .dateRented("2018-11-04")
        .dueDate("2018-12-31")
        .build();
    Gown gown4 = Gown.builder()
        .id(4L)
        .name("Dandelion hands")
        .description("you look green")
        .price(412.4)
        .dateRented("2019-04-04")
        .dueDate("2019-05-31")
        .build();
    Gown gown5 = Gown.builder()
        .id(5L)
        .name("Black laces")
        .description("Green braces")
        .price(105.30)
        .dateRented("2019-12-04")
        .dueDate("2019-12-31")
        .build();
    gowns = new ArrayList<>(Arrays.asList(gown1, gown2, gown3,
                                          gown4, gown5));
    return gowns;
  }

}
