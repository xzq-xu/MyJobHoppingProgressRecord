import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceTest {
    public static void main(String[] args) throws InterruptedException,IOException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        int threadCount = 30;
        int iterations = 1000;
        
        long start = System.nanoTime();
        CompletableFuture<?>[] completableFutures = new CompletableFuture[threadCount];
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<Void> taskFuture = CompletableFuture.runAsync(() -> {
                            for (int j = 0; j < iterations; j++) {
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                // 切换测试方法
                                TestConcurrentHashMap.add(map, "counter");
                                // TestConcurrentHashMap.addSingle(map, "counter");
                            }
            });
            completableFutures[i] = taskFuture;
        }
        CompletableFuture.allOf(completableFutures).join();
        System.out.println("counter:"+map.get("counter"));
        
        long duration = System.nanoTime() - start;
       
        System.out.printf("吞吐量: %.2f ops/ms%n",
            (threadCount * iterations) / (duration / 1000_000_000.0));
    }
}