package ru.pro.geometry;

import lombok.AllArgsConstructor;

import static java.lang.Math.PI;

/**
 * Круг
 */
@AllArgsConstructor
public class Circle implements Shape {
    private final double radius;

    @Override
    public double area() {
        return PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * PI * radius;
    }
}
