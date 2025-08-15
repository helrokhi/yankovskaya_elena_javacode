package ru.pro;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

@UtilityClass
public class CounterUtil {
    public <T> Map<T, Integer> countOccurrences(T[] array) {
        Map<T, Integer> occurrences = new HashMap<>();
        ofNullable(array)
                .ifPresent(arr -> stream(arr)
                        .forEach(element -> occurrences.merge(element, 1, Integer::sum)));
        return occurrences;
    }
}
