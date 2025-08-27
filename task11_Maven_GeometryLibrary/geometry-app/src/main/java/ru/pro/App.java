package ru.pro;

import ru.pro.geometry.Circle;
import ru.pro.geometry.Rectangle;
import ru.pro.geometry.Shape;
import ru.pro.geometry.Triangle;
import ru.pro.threeD.Cube;
import ru.pro.threeD.Solid;
import ru.pro.threeD.Sphere;

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

        Solid cube = new Cube(3);
        Solid sphere = new Sphere(5);

        printSolid(cube);
        printSolid(sphere);

    }

    private static void printShape(Shape shape) {
        System.out.println("Фигура: " + shape.getDescription(shape));
        System.out.println("Площадь: " + shape.area());
        System.out.println("Периметр: " + shape.perimeter());
        System.out.println();
    }

    private static void printSolid(Solid solid) {
        System.out.println("Фигура: " + solid.getClass().getSimpleName());
        System.out.println("Объём = " + solid.volume());
        System.out.println("Площадь поверхности = " + solid.surfaceArea());
        System.out.println();
    }
}