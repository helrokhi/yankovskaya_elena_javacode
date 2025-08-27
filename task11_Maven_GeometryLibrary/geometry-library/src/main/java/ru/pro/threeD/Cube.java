package ru.pro.threeD;

import lombok.AllArgsConstructor;

import static java.lang.Math.pow;

@AllArgsConstructor
public class Cube implements Solid {
    private final double side;

    @Override
    public double volume() {
        return pow(side, 3);
    }

    @Override
    public double surfaceArea() {
        return 6 * side * side;
    }
}
