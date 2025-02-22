package site.xzq_xu;

import site.xzq_xu.bloom.BloomFilter;
import site.xzq_xu.bloom.DefaulltBloomFilter;

public class TestBloomFilter {
    public static void main(String[] args) {
        //测试布隆过滤器

        int expectedElements = 1000000;
        BloomFilter<String> bloomFilter = new DefaulltBloomFilter<>(expectedElements);


        //添加元素
        for (int i = 0; i < expectedElements; i++) {
            bloomFilter.add("test"+i);
        }

        //判断元素是否存在
        System.out.println(bloomFilter.contains("test100000"));
        System.out.println(bloomFilter.contains("test10001"));
        System.out.println(bloomFilter.contains("test123"));

        //查看误判率
        int count = 0;
        for (int i = expectedElements +1; i < 2*expectedElements; i++) {
            if (bloomFilter.contains("test"+i)){
                count++;
            }
        }
        System.out.println(count);
        System.out.println("误判率："+(count*1.0/1000000));

    }
}