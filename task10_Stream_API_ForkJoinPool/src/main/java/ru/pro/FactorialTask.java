package ru.pro;

import lombok.AllArgsConstructor;

import java.util.concurrent.RecursiveTask;

@AllArgsConstructor
public class FactorialTask extends RecursiveTask<Integer> {
    private final int n;

    @Override
    protected Integer compute() {
        if (n <= 1) return 1;
        FactorialTask subTask = new FactorialTask(n - 1);
        subTask.fork();
        Integer subResult = subTask.join();
        return n * subResult;
    }
}
