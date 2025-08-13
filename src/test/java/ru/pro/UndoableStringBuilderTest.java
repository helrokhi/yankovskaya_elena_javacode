package ru.pro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UndoableStringBuilderTest {
    private UndoableStringBuilder usb;

    @BeforeEach
    void setUp() {
        usb = new UndoableStringBuilder();
    }

    @Nested
    @DisplayName("Тесты метода append()")
    class AppendTests {
        @ParameterizedTest(name = "append(\"{0}\") добавляет строку")
        @ValueSource(strings = {"abc", "123", "Тест"})
        void testAppend(String input) {
            usb.append(input);
            assertEquals(input, usb.getSb().toString());
        }

        @Test
        @DisplayName("append() можно вызывать цепочкой")
        void testAppendChaining() {
            usb.append("abc").append("123");
            assertEquals("abc123", usb.getSb().toString());
        }
    }

    @Nested
    @DisplayName("Тесты метода insert()")
    class InsertTests {
        static Stream<Arguments> insertCases() {
            return of(
                    Arguments.of("abc", 0, "X", "Xabc"),
                    Arguments.of("abc", 1, "-", "a-bc"),
                    Arguments.of("abc", 3, "!", "abc!")
            );
        }

        @ParameterizedTest(name = "insert в \"{0}\" на позицию {1} со строкой \"{2}\"")
        @MethodSource("insertCases")
        void testInsert(String base, int pos, String insert, String expected) {
            usb.append(base).insert(pos, insert);
            assertEquals(expected, usb.getSb().toString());
        }
    }

    @Nested
    @DisplayName("Тесты метода delete()")
    class DeleteTests {
        @Test
        void testDeleteRange() {
            usb.append("abcdef").delete(2, 4);
            assertEquals("abef", usb.getSb().toString());
        }

        @Test
        void testDeleteInvalidRangeThrows() {
            usb.append("abc");
            assertThrows(StringIndexOutOfBoundsException.class, () -> usb.delete(5, 10));
        }
    }

    @Nested
    @DisplayName("Тесты undo/redo")
    class UndoRedoTests {
        @Test
        void testUndoRestoresPreviousState() {
            usb.append("abc").append("123");
            usb.undo();
            assertEquals("abc", usb.getSb().toString());
        }

        @Test
        void testRedoRestoresAfterUndo() {
            usb.append("abc").append("123");
            usb.undo();
            usb.redo();
            assertEquals("abc123", usb.getSb().toString());
        }

        @Test
        void testUndoWithoutHistoryDoesNothing() {
            usb.undo();
            assertEquals("", usb.getSb().toString());
        }

        @Test
        void testRedoWithoutHistoryDoesNothing() {
            usb.redo();
            assertEquals("", usb.getSb().toString());
        }
    }

    @Nested
    @DisplayName("Тесты метода replace()")
    class ReplaceTests {
        @Test
        void testReplaceRange() {
            usb.append("abcdef").replace(2, 5, "XYZ");
            assertEquals("abXYZf", usb.getSb().toString());
        }

        @Test
        void testReplaceInvalidRangeThrows() {
            usb.append("abc");
            assertThrows(StringIndexOutOfBoundsException.class,
                    () -> usb.replace(5, 10, "X"));
        }
    }
}