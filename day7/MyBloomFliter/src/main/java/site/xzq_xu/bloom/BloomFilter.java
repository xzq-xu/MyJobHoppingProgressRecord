package site.xzq_xu.bloom;

import lombok.Getter;
import lombok.NonNull;

import java.util.BitSet;
import java.util.List;

import static site.xzq_xu.bloom.DefaultHashFunction.getDefaultHashFunctionList;

/**
 * 布隆过滤器基类
 * @param <T>
 */
@Getter
public abstract class BloomFilter<T> {

    // 核心成员变量
    private final BitSet bitSet; // 比特数组
    private final int size;      // 比特数组大小
    private final int hashCount; // 哈希函数数量
    private final List<HashFunction> hashFunctionList; // 哈希函数接口


    // 构造函数
    public BloomFilter(int expectedElements) {
        this(expectedElements, 0.1);
    }

    // 初始化方法
    public BloomFilter(int expectedElements, double falsePositiveRate) {
        // 计算比特数组大小 m 和哈希函数数量 k
        this.size = optimalSize(expectedElements, falsePositiveRate);
        this.hashCount = optimalHashFunctions(expectedElements, this.size);
        this.bitSet = new BitSet(this.size);
        this.hashFunctionList = initHashFunctionList(hashCount);
    }
    // 判断元素是否在布隆过滤器中
    public  abstract  boolean contains(T value);
    // 将元素添加到布隆过滤器中
    public  abstract  void add(T value);


    // 辅助方法：计算最优比特数组大小 m
    private static int optimalSize(int n, double p) {
        // 如果 p 为 0，则将其设置为最小值
        if (p == 0) p = Double.MIN_VALUE;
        // 返回计算出的最优比特数组大小 m
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    // 辅助方法：计算最优哈希函数数量 k
    private static int optimalHashFunctions(int n, int m) {
        // 计算最优哈希函数数量 k
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }


    // 初始化哈希函数列表
    @NonNull
    protected List<HashFunction>  initHashFunctionList(int hashCount){
        // 调用getDefaultHashFunctionList方法，传入hashCount参数，返回默认的哈希函数列表
        return getDefaultHashFunctionList(hashCount);
    }

    // 获取哈希函数列表,不允许修改
    public final List<HashFunction> getHashFunctionList() {
        return hashFunctionList;
    }
}
