package ru.pro;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ComplexTask {
    private String message;
    public String execute() {
        System.out.println(message);
        return message;
    }
}
