package ru.pro;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.Deque;

@RequiredArgsConstructor
@ToString(of = "sb")
@Getter
public class UndoableStringBuilder {
    private static final int MAX_HISTORY_SIZE = 50;
    /**
     * Текущее состояние строки
     */
    private final StringBuilder sb = new StringBuilder();

    /**
     * История состояний для операций undo
     */
    private final Deque<String> history = new ArrayDeque<>();

    /**
     * История отменённых состояний для операций redo
     */
    private final Deque<String> redoHistory = new ArrayDeque<>();

    /**
     * Сохраняет текущее состояние строки в историю {@code history}
     * и очищает историю {@code redoHistory}, так как после новых изменений
     * redo больше не применим.
     */
    private void snapshot() {
        history.push(sb.toString());
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeLast();
        }
        redoHistory.clear();
    }

    /**
     * Добавляет указанную строку в конец.
     *
     * @param str строка для добавления
     * @return текущий объект {@code UndoableStringBuilder}
     */
    public UndoableStringBuilder append(String str) {
        snapshot();
        sb.append(str);
        return this;
    }

    /**
     * Вставляет строку в указанную позицию.
     *
     * @param offset индекс позиции вставки
     * @param str    строка для вставки
     * @return текущий объект {@code UndoableStringBuilder}
     */
    public UndoableStringBuilder insert(int offset, String str) {
        snapshot();
        sb.insert(offset, str);
        return this;
    }

    /**
     * Удаляет подстроку в указанном диапазоне.
     *
     * @param start начальный индекс (включительно)
     * @param end   конечный индекс (исключительно)
     * @return текущий объект {@code UndoableStringBuilder}
     */
    public UndoableStringBuilder delete(int start, int end) {
        snapshot();
        sb.delete(start, end);
        return this;
    }

    /**
     * Заменяет подстроку в указанном диапазоне новой строкой.
     *
     * @param start начальный индекс (включительно)
     * @param end   конечный индекс (исключительно)
     * @param str   строка для вставки
     * @return текущий объект {@code UndoableStringBuilder}
     */
    public UndoableStringBuilder replace(int start, int end, String str) {
        snapshot();
        sb.replace(start, end, str);
        return this;
    }

    /**
     * Отменяет последнее изменение.
     * <p>
     * Переносит текущее состояние в {@code redoHistory} и
     * восстанавливает предыдущее состояние из {@code history}.
     * </p>
     */
    public void undo() {
        if (!history.isEmpty()) {
            redoHistory.push(sb.toString());
            String prev = history.pop();
            sb.setLength(0);
            sb.append(prev);
        }
    }

    /**
     * Повторяет последнее отменённое действие.
     * <p>
     * Переносит текущее состояние в {@code history} и
     * восстанавливает отменённое состояние из {@code redoHistory}.
     * </p>
     */
    public void redo() {
        if (!redoHistory.isEmpty()) {
            history.push(sb.toString());
            String next = redoHistory.pop();
            sb.setLength(0);
            sb.append(next);
        }
    }
}
