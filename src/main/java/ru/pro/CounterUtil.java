package ru.pro;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class CounterUtil {
    public <T> Map<T, Integer> countOccurrences(T[] array) {
        Map<T, Integer> occurrences = new HashMap<>();
        Optional.ofNullable(array)
                .ifPresent(arr -> Arrays.stream(arr)
                        .forEach(element -> occurrences.merge(element, 1, Integer::sum)));
        return occurrences;
    }
}
