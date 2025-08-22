package ru.pro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        //1) Создайте список заказов с разными продуктами и их стоимостями.
        Stream<Order> orderStream = orders.stream();

        //2) Группируйте заказы по продуктам.
        Stream<Map.Entry<String, Double>> productCostStream = orderStream
                .map(o -> Map.entry(o.getProduct(), o.getCost()));

        //3) Для каждого продукта найдите общую стоимость всех заказов.
        Stream<Map.Entry<String, Double>> summedStream = productCostStream
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ))
                .entrySet()
                .stream();

        //4) Отсортируйте продукты по убыванию общей стоимости.
        Stream<Map.Entry<String, Double>> sortedStream = summedStream
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed());

        //5) Выберите три самых дорогих продукта.
        Stream<Map.Entry<String, Double>> top3Stream = sortedStream.limit(3);

        //6) Выведите результат: список трех самых дорогих продуктов и их общая стоимость.
        top3Stream.forEach(e ->
                System.out.println(e.getKey() + " -> " + e.getValue()));

//        Laptop -> 2700.0
//        Smartphone -> 1700.0
//        Tablet -> 500.0
    }
}
