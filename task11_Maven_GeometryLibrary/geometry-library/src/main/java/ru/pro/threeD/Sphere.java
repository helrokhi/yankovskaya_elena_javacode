package ru.pro.threeD;

import lombok.AllArgsConstructor;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

@AllArgsConstructor
public class Sphere implements Solid {
    private final double radius;

    @Override
    public double volume() {
        return 4.0 / 3.0 * PI * pow(radius, 3);
    }

    @Override
    public double surfaceArea() {
        return 4 * PI * radius * radius;
    }
}

