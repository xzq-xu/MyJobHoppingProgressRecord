package site.xzq_xu;

import site.xzq_xu.bloom.CountableBloomFilter;

public class TestBloomFilterDelete {
    public static void main(String[] args) {
        // 创建一个容量为1000，误判率为0.01的CountableBloomFilter
        CountableBloomFilter<String> bloomFilter = new CountableBloomFilter<>(1000, 0.01);

        // 向BloomFilter中添加元素
        bloomFilter.add("hello");
        bloomFilter.add("world");
        bloomFilter.add("java");
        bloomFilter.add("python");
        bloomFilter.add("c++");
        bloomFilter.add("c#");

        // 判断BloomFilter中是否包含指定元素
        System.out.println(bloomFilter.contains("hello"));
        System.out.println(bloomFilter.contains("world"));
        System.out.println(bloomFilter.contains("java"));
        System.out.println(bloomFilter.contains("python"));
        System.out.println(bloomFilter.contains("c++"));


        // 从BloomFilter中删除指定元素
        bloomFilter.delete("hello");
        bloomFilter.delete("world");

        // 判断BloomFilter中是否包含指定元素
        System.out.println(bloomFilter.contains("hello"));
        System.out.println(bloomFilter.contains("world"));



    }
}
