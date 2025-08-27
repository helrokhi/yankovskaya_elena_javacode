package ru.pro.geometry;

public interface Shape {
    double area();
    double perimeter();

    default String getDescription(Shape shape) {
        return shape.getClass().getSimpleName();
    }
}
