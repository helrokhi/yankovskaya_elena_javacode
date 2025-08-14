package ru.pro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CounterUtilTest {
    @Test
    void countOccurrences_withValidArray_returnsCorrectCounts() {
        String[] array = {"A", "B", "C", "B", "C", "C"};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertAll("Проверка частоты элементов",
                () -> assertEquals(3, result.get("C")),
                () -> assertEquals(2, result.get("B")),
                () -> assertEquals(1, result.get("A")),
                () -> assertNull(result.get("D"))
        );
    }

    @Test
    void countOccurrences_withEmptyArray_returnsEmptyMap() {
        String[] array = {};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertTrue(result.isEmpty(), "Ожидалась пустая карта для пустого массива");
    }

    @Test
    void countOccurrences_withNullArray_returnsEmptyMap() {
        String[] array = null;

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertTrue(result.isEmpty(), "Ожидалась пустая карта для массива равного null");
    }

    @Test
    void countOccurrences_withSingleElement_returnsCountOne() {
        String[] array = {"A"};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertEquals(1, result.get("A"), "Ожидалась частота 1 для элемента A");
    }

    @Test
    void countOccurrences_withMultipleSameElements_returnsCorrectCount() {
        String[] array = {"A", "A", "A"};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertEquals(3, result.get("A"), "Ожидалась частота 3 для элемента A");
    }

    @Test
    void countOccurrences_withNullElements() {
        String[] array = {null, "A", null, "B", "B", null};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertAll("Проверка частоты элементов, включая null",
                () -> assertEquals(3, result.get(null), "Ожидалась частота 3 для null элементов"),
                () -> assertEquals(2, result.get("B"), "Ожидалась частота 2 для элемента B"),
                () -> assertEquals(1, result.get("A"), "Ожидалась частота 1 для элемента A")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C"})
    void countOccurrences_withDifferentValues_returnsCorrectCountForEach(String value) {
        String[] array = {value, value, value};

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertEquals(3, result.get(value), "Ожидалась частота 3 для элемента: " + value);
    }

    @Test
    void countOccurrences_withLargeDataSet_returnsCorrectCounts() {
        String[] array = Stream.generate(() -> "A").limit(100000).toArray(String[]::new);

        Map<String, Integer> result = CounterUtil.countOccurrences(array);

        assertEquals(100000, result.get("A"), "Ожидалась частота 100000 для элемента 'A'");
    }
}