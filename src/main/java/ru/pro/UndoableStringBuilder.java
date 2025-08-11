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
    private final StringBuilder sb = new StringBuilder();
    private final Deque<String> history = new ArrayDeque<>();

    public void snapshot() {
        history.push(sb.toString());
    }

    public void undo() {
        if (!history.isEmpty()) {
            String prev = history.pop();
            sb.setLength(0);
            sb.append(prev);
        }
    }
}
