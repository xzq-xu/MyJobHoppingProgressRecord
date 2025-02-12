import java.util.concurrent.ConcurrentHashMap;

public class MultiThreadCounter {
    private ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<>();

    public void increment() {
        countMap.put("count", countMap.getOrDefault("count", 0) + 1);
    }

    public int getCount() {
        return countMap.getOrDefault("count", 0);
    }
}
