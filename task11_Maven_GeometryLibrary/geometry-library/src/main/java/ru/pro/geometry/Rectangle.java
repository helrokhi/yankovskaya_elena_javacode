package ru.pro.geometry;

import lombok.AllArgsConstructor;

/**
 * Прямоугольник
 */
@AllArgsConstructor
public class Rectangle implements Shape {
    private final double width;
    private final double height;

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}
