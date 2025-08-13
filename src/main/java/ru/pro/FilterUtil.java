package ru.pro;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class FilterUtil {
    public <T> T[] filter(T[] array, Filter<T> filter) {
        if (array == null || filter == null) {
            throw new IllegalArgumentException("Аргументы не должны быть null");
        }

        return Arrays.stream(array)
                .map(filter::apply)
                .toArray(size -> Arrays.copyOf(array, size));
    }
}
