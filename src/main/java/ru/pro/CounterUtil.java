package ru.pro;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class CounterUtil {
    public <T> Map<T, Integer> countOccurrences(T[] array) {
        Map<T, Integer> occurrences = new HashMap<>();
        if (array != null) {
            for (T element : array) {
                occurrences.put(element, occurrences.getOrDefault(element, 0) + 1);
            }
        }
        return occurrences;
    }
}
