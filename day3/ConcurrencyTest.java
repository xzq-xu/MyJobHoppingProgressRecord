public class ConcurrencyTest {
    private static final int THREADS = 100;
    private static final int ITERATIONS = 100000;

    public static void main(String[] args) throws InterruptedException {
        testUnsafeCounter();
        testPerformanceComparison();
    }

    private static void testUnsafeCounter() throws InterruptedException {
        SingleThreadCounter counter = new SingleThreadCounter();
        Thread[] threads = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    counter.increment();
                }
            });
        }

        long start = System.nanoTime();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long duration = System.nanoTime() - start;

        System.out.println("【不安全测试】");
        System.out.println("预期结果：" + (THREADS * ITERATIONS));
        System.out.println("实际结果：" + counter.getCount());
        System.out.printf("耗时：%.2f ms%n%n", duration / 1e6);
    }

    private static void testPerformanceComparison() {
        MultiThreadCounter safeCounter = new MultiThreadCounter();
        SingleThreadCounter unsafeCounter = new SingleThreadCounter();

        long safeStart = System.nanoTime();
        for (int i = 0; i < THREADS * ITERATIONS; i++) {
            safeCounter.increment();
        }
        long safeDuration = System.nanoTime() - safeStart;

        long unsafeStart = System.nanoTime();
        for (int i = 0; i < THREADS * ITERATIONS; i++) {
            unsafeCounter.increment();
        }
        long unsafeDuration = System.nanoTime() - unsafeStart;

        System.out.println("【性能对比】");
        System.out.println("线程安全计数器结果："+ safeCounter.getCount());
        System.out.println("线程安全计数器耗时：" + safeDuration / 1e6 + " ms");
        System.out.println("非线程安全计数器结果："+ unsafeCounter.getCount());
        System.out.println("非线程安全计数器耗时：" + unsafeDuration / 1e6 + " ms");
    }
}
