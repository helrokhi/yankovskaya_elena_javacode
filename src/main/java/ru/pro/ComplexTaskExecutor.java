package ru.pro;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newCachedThreadPool;

@AllArgsConstructor
public class ComplexTaskExecutor {
    public int numberOfTasks;

    public void executeTasks(int numberOfTasks) {
        List<ComplexTask> tasks = new ArrayList<>();
        for (int i = 1; i <= numberOfTasks; i++) {
            tasks.add(new ComplexTask("Task #" + i));
        }

        ExecutorService executor = newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();
        List<String> results = new ArrayList<>();

        CyclicBarrier barrier = new CyclicBarrier(tasks.size(), () ->
                System.out.println(currentThread().getName() + " - All tasks completed. Results: " + results));

        for (ComplexTask task : tasks) {
            futures.add(executor.submit(() -> {
                String result = task.execute();
                synchronized (results) {
                    results.add(result);
                }
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("Exception -> " + e.getMessage());
                }
                return result;
            }));
        }

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception -> " + e.getMessage());
            }
        });

        executor.shutdown();
    }
}
