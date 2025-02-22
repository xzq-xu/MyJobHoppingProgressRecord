package site.xzq_xu.bloom;

import java.util.BitSet;
import java.util.List;

public class CountableBloomFilter<T> extends BloomFilter<T> {

    private final int[] counters; // 计数器数组

    public CountableBloomFilter(int expectedElements) {
        super(expectedElements);
        this.counters = new int[super.getSize()];
    }

    public CountableBloomFilter(int expectedElements, double falsePositiveRate) {
        super(expectedElements, falsePositiveRate);
        this.counters = new int[super.getSize()];;
    }

    @Override
    public boolean contains(T value) {
        //获取BitSet
        BitSet bitSet = getBitSet();
        //获取BitSet的大小
        int bitSize = getSize();

        //遍历哈希函数列表
        for (HashFunction hashFunction : getHashFunctionList()) {

            //计算哈希值
            int index = hashFunction.hash(value) % bitSize;

            //如果BitSet中对应位置为1且计数器大于0，则表示存在
            if (!(bitSet.get(index) && counters[index]>0)){
                //表示不存在
                return false;
            }
        }

        return true;
    }


    @Override
    public void add(T value) {
        // 获取BitSet对象
        BitSet bitSet = getBitSet();
        // 获取BitSet的大小
        int bitSize = getSize();
        // 获取HashFunction列表
        List<HashFunction> hashFunctionList = getHashFunctionList();
        // 遍历HashFunction列表
        for (HashFunction hashFunction : hashFunctionList) {
            // 计算value的哈希值
            int index = hashFunction.hash(value) % bitSize;
            // 将BitSet中对应位置的值设置为true
            bitSet.set(index);
            counters[index]++;
        }
    }

    /**
     * 布隆过滤器实现 删除
     * @param value
     */
    public void  delete(T value) {
        // 获取位数
        int bitSize = getSize();
        // 获取哈希函数列表
        List<HashFunction> hashFunctionList = getHashFunctionList();
        // 遍历哈希函数列表
        for (HashFunction hashFunction : hashFunctionList) {
            // 计算哈希值
            int index = hashFunction.hash(value) % bitSize;
            // 如果计数器大于0，则减1
            if (counters[index]>0.) {
                counters[index]--;
            }
        }
    }

}
