import java.util.concurrent.ConcurrentHashMap;

public class TestConcurrentHashMap {
    /**
     * 单线程风格的原子计数（实际线程安全）
     * 使用compute方法实现原子操作：
     * 1. 对相同key的操作会通过synchronized同步
     * 2. 整个lambda表达式在同步块内执行
     * 3. 适合低冲突场景
     * 特点：
     * - 代码简洁易维护
     * - 高并发时可能成为性能瓶颈
     * - 自动处理ABA问题
     */
    public static void addSingle(ConcurrentHashMap<String,Integer> map, String key) {
        map.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
    }


    /**
     * 多线程安全的CAS计数实现
     * 1. 使用putIfAbsent初始化
     * 2. 通过CAS循环保证原子更新
     * 3. 适合高并发场景
     * 特点：
     * - 无锁竞争时性能更好
     * - 需要处理ABA问题
     * - 可能经历多次重试
     */
    static void add(ConcurrentHashMap<String,Integer> map, String key){
        while(true){
            Integer oldValue = map.putIfAbsent(key,1);
            if(oldValue == null || map.replace(key,oldValue,oldValue + 1)){
                break;
            }
        }
    }
}
