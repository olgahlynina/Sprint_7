package utils;

import models.Courier;
import models.Order;
import models.OrderColor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private final Random random = new Random();

    public Courier generateRandomCourier() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return new Courier(
                "courier_" + timestamp,
                "pass_" + timestamp,
                "name_" + timestamp
        );
    }

    public Order generateRandomOrder() {
        return new Order(
                "FirstName_" + random.nextInt(1000),
                "LastName_" + random.nextInt(1000),
                "Address_" + random.nextInt(1000),
                String.valueOf(random.nextInt(20)),
                "+7999" + random.nextInt(1000000),
                random.nextInt(10) + 1,
                LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE),
                "Comment_" + random.nextInt(1000),
                null
        );
    }

    public Order generateOrderWithColor(OrderColor color) {
        Order order = generateRandomOrder();
        order.setColor(Collections.singletonList(color.name()));
        return order;
    }

    public Order generateOrderWithTwoColors() {
        Order order = generateRandomOrder();
        order.setColor(Arrays.asList(OrderColor.BLACK.name(), OrderColor.GREY.name()));
        return order;
    }
}