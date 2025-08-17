package ru.pro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class BlockingQueueTest {
    private BlockingQueue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new BlockingQueue<>(3);
    }

    @Nested
    @DisplayName("Базовые операции enqueue и dequeue")
    class BasicOperations {
        @Test
        @DisplayName("Элемент должен возвращаться в том же порядке (FIFO)")
        void testFifoOrder() throws InterruptedException {
            queue.enqueue("A");
            queue.enqueue("B");
            queue.enqueue("C");

            assertAll(
                    () -> assertEquals("A", queue.dequeue()),
                    () -> assertEquals("B", queue.dequeue()),
                    () -> assertEquals("C", queue.dequeue())
            );
        }

        @ParameterizedTest(name = "После добавления {0} элементов size() должен совпадать")
        @ValueSource(ints = {1, 2, 3})
        void testSizeAfterEnqueue(int count) throws InterruptedException {
            for (int i = 0; i < count; i++) {
                queue.enqueue("X" + i);
            }
            assertEquals(count, queue.size());
        }
    }

    @Nested
    @DisplayName("Блокирующее поведение")
    class BlockingBehavior {
        @Test
        @DisplayName("dequeue() должен блокироваться, пока не появится элемент")
        @Timeout(2)
        void testDequeueBlocksUntilEnqueue() throws Exception {
            ExecutorService executor = newSingleThreadExecutor();

            Future<String> future = executor.submit(() -> queue.dequeue());

            Thread.sleep(200);

            queue.enqueue("Hello");
            assertEquals("Hello", future.get(1, SECONDS));

            executor.shutdownNow();
        }

        @Test
        @DisplayName("enqueue() должен блокироваться, если очередь заполнена")
        void testEnqueueBlocksWhenFull() throws Exception {
            queue.enqueue("First");
            queue.enqueue("Second");
            queue.enqueue("Third");

            ExecutorService executor = newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                try {
                    queue.enqueue("Blocked");
                } catch (InterruptedException ignored) {
                }
            });

            Thread.sleep(200);
            assertFalse(future.isDone(), "enqueue() должен блокироваться на полной очереди");

            assertEquals("First", queue.dequeue());

            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> future.get());

            executor.shutdownNow();
        }
    }

    @Nested
    @DisplayName("Конкурентные сценарии")
    class ConcurrentScenarios {
        @Test
        @DisplayName("Несколько производителей и потребителей корректно обмениваются данными")
        @Timeout(5)
        void testMultipleProducersAndConsumers() throws InterruptedException, ExecutionException {
            int producersCount = 3;
            int consumersCount = 3;
            int itemsPerProducer = 5;
            int totalItems = producersCount * itemsPerProducer;

            ExecutorService executor = Executors.newFixedThreadPool(producersCount + consumersCount);
            ConcurrentLinkedQueue<String> consumed = new ConcurrentLinkedQueue<>();

            List<Callable<Void>> producers = new ArrayList<>();
            for (int i = 0; i < producersCount; i++) {
                int id = i;
                producers.add(() -> {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        queue.enqueue("P" + id + "-" + j);
                    }
                    return null;
                });
            }

            List<Callable<Void>> consumers = new ArrayList<>();
            int itemsPerConsumer = totalItems / consumersCount;
            for (int i = 0; i < consumersCount; i++) {
                consumers.add(() -> {
                    for (int j = 0; j < itemsPerConsumer; j++) {
                        consumed.add(queue.dequeue());
                    }
                    return null;
                });
            }

            List<Callable<Void>> tasks = new ArrayList<>();
            tasks.addAll(producers);
            tasks.addAll(consumers);
            executor.invokeAll(tasks);

            assertEquals(totalItems, consumed.size());

            executor.shutdownNow();
        }
    }
}