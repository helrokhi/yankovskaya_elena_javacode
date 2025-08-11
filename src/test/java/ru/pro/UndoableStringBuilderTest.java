package ru.pro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UndoableStringBuilderTest {

    private UndoableStringBuilder usb;

    @BeforeEach
    void init() {
        usb = new UndoableStringBuilder();
    }

    @Test
    @DisplayName("Проверка append и undo")
    void testAppendAndUndo() {
        usb.snapshot();
        usb.getSb().append("Hello");

        assertAll("Проверка состояния после append и undo",
                () -> assertEquals("Hello", usb.getSb().toString(), "После append должно быть 'Hello'"),

                () -> {
                    usb.undo();
                    assertEquals("", usb.getSb().toString(), "После undo должно быть пустое значение");
                }
        );
    }

    @Test
    @DisplayName("Проверка метода toString() сгенерированного Lombok")
    void testToStringGeneratedByLombok() {
        usb.getSb().append("Test");
        String toStringResult = usb.toString();

        assertAll("Проверка toString содержит 'Test'",
                () -> assertNotNull(toStringResult, "toString не должен быть null"),
                () -> assertTrue(toStringResult.contains("Test"), "toString должен содержать 'Test'")
        );
    }

    @Test
    @DisplayName("Проверка инициализации полей через Lombok")
    void testConstructorGeneratedByLombok() {
        assertAll("Проверка полей",
                () -> assertNotNull(usb.getSb(), "StringBuilder должен быть инициализирован"),
                () -> assertNotNull(usb.getHistory(), "История должна быть инициализирована")
        );
    }

    @ParameterizedTest(name = "append(\"{0}\") корректно обновляет строку")
    @ValueSource(strings = {"abc", "123", "Тест"})
    @DisplayName("Параметризованный тест append")
    void testAppendMultipleValues(String input) {
        usb.snapshot();
        usb.getSb().append(input);
        assertEquals(input, usb.getSb().toString());
    }
}