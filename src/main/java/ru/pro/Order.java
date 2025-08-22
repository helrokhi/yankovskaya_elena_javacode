package ru.pro;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Order {
    private String product;
    private double cost;
}
