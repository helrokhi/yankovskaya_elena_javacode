package ru.pro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilterUtilTest {
    @Test
    @DisplayName("Проверка фильтрации строк с приведением к верхнему регистру")
    void testFilterStrings() {
        String[] input = {"one", "two", "three"};
        String[] result = FilterUtil.filter(input, String::toUpperCase);

        assertArrayEquals(new String[]{"ONE", "TWO", "THREE"},
                result, "Все строки должны быть в верхнем регистре");
        assertArrayEquals(new String[]{"one", "two", "three"},
                input, "Исходный массив не должен изменяться");
    }

    @Test
    @DisplayName("Проверка фильтрации целых чисел с возведением в квадрат")
    void testFilterIntegers() {
        Integer[] input = {1, 2, 3, 4};
        Integer[] result = FilterUtil.filter(input, n -> n * n);

        assertArrayEquals(new Integer[]{1, 4, 9, 16}, result);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, input);
    }

    @Test
    @DisplayName("Проверка фильтрации пустого массива")
    void testFilterEmptyArray() {
        String[] empty = {};
        String[] result = FilterUtil.filter(empty, s -> s + "!");
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    @DisplayName("Проверка выброса IllegalArgumentException при передаче null массива")
    void testFilterNullArray() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FilterUtil.filter(null, s -> s));
        assertEquals("Аргументы не должны быть null", ex.getMessage());
    }

    @Test
    @DisplayName("Проверка выброса IllegalArgumentException при передаче null фильтра")
    void testFilterNullFilter() {
        String[] input = {"a", "b"};
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FilterUtil.filter(input, null));
        assertEquals("Аргументы не должны быть null", ex.getMessage());
    }

    static Stream<Object[]> stringTestData() {
        return Stream.of(
                new Object[]{"abc", "ABC"},
                new Object[]{"", ""},
                new Object[]{"test", "TEST"}
        );
    }

    @ParameterizedTest(name = "{index} => вход={0}, ожидаемый результат={1}")
    @MethodSource("stringTestData")
    @DisplayName("Параметризованный тест фильтрации строк")
    void testFilterStringsParameterized(String inputStr, String expected) {
        String[] input = {inputStr};
        String[] result = FilterUtil.filter(input, String::toUpperCase);
        assertArrayEquals(new String[]{expected}, result);
    }

    static Stream<Object[]> integerTestData() {
        return Stream.of(
                new Object[]{1, 1},
                new Object[]{2, 4},
                new Object[]{3, 9}
        );
    }

    @ParameterizedTest(name = "{index} => вход={0}, ожидаемый результат={1}")
    @MethodSource("integerTestData")
    @DisplayName("Параметризованный тест фильтрации целых чисел")
    void testFilterIntegersParameterized(Integer inputVal, Integer expected) {
        Integer[] input = {inputVal};
        Integer[] result = FilterUtil.filter(input, n -> n * n);
        assertArrayEquals(new Integer[]{expected}, result);
    }

    @Test
    @DisplayName("Проверка, что фильтр возвращает новый массив с новыми объектами")
    void testFilterCreatesNewArray() {
        String[] input = {"abc"};
        String[] result = FilterUtil.filter(input, s -> new StringBuilder(s).reverse().toString());

        assertArrayEquals(new String[]{"cba"}, result);
        assertNotSame(input[0], result[0], "Элементы нового массива должны быть новыми объектами");
    }
}