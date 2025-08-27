package ru.pro.geometry;

import lombok.AllArgsConstructor;

import static java.lang.Math.sqrt;

/**
 * Треугольник
 */
@AllArgsConstructor
public class Triangle implements Shape {
    private final double a, b, c;

    @Override
    public double area() {
        isTriangleExists();
        double p = perimeter() / 2;
        return sqrt(p * (p - a) * (p - b) * (p - c));
    }

    @Override
    public double perimeter() {
        isTriangleExists();
        return a + b + c;
    }

    public void isTriangleExists() {
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Невалидные стороны треугольника");
        }
    }
}
