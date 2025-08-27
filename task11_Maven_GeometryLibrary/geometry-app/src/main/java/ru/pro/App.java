package ru.pro;

import ru.pro.geometry.Shape;
import ru.pro.geometry.Circle;
import ru.pro.geometry.Rectangle;
import ru.pro.geometry.Triangle;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape triangle = new Triangle(3, 4, 5);

        printShape(circle);
        printShape(rectangle);
        printShape(triangle);
    }

    private static void printShape(Shape shape) {
        System.out.println("Фигура: " + shape.getClass().getSimpleName());
        System.out.println("Площадь: " + shape.area());
        System.out.println("Периметр: " + shape.perimeter());
        System.out.println();
    }
}