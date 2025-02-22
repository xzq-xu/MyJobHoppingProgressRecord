package site.xzq_xu.bloom;

import java.util.BitSet;
import java.util.List;

public class DefaulltBloomFilter<T> extends BloomFilter<T> {


    public DefaulltBloomFilter(int expectedElements) {
        super(expectedElements);
    }

    public DefaulltBloomFilter(int expectedElements, double falsePositiveRate) {
        super(expectedElements, falsePositiveRate);
    }

    @Override
    public boolean contains(T value) {
        //全1为true，有0即为false
        BitSet bitSet = getBitSet();
        int bitSize = getSize();

        //遍历哈希函数列表
        for (HashFunction hashFunction : getHashFunctionList()) {
            //计算哈希值
            int index = hashFunction.hash(value) % bitSize;
            //如果值为0，则返回false
            if (!bitSet.get(index)){
                //表示不存在
                return false;
            }
        }
        //如果所有位置的值都为1，则返回true， 表示可能存在
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
        }
    }
}
