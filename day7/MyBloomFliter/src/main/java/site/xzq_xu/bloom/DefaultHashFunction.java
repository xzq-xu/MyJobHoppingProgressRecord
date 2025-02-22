package site.xzq_xu.bloom;

import java.util.ArrayList;
import java.util.List;

public class DefaultHashFunction implements HashFunction {

    // 定义一个种子变量
    private final int seed;

    // 构造函数，传入一个种子值
    public DefaultHashFunction(int seed) {
        this.seed = seed;
    }

    // 实现HashFunction接口的hash方法
    @Override
    public int hash(Object obj) {
        // 计算对象的哈希码，并加上种子值
        int hashCode = obj.hashCode()+seed;
        // 对哈希码进行位运算，使其更加均匀
        hashCode ^= hashCode ^ (hashCode >>> 16);
        // 返回最终的哈希码
        return hashCode;
    }



    public static List<HashFunction> getDefaultHashFunctionList(int count) {

        // 创建一个大小为count的HashFunction列表
        List<HashFunction> hashFunctionList = new ArrayList<>(count);
        // 遍历count次
        for (int i = 0; i < count; i++) {
            // 生成一个随机数
            int random = (int)Math.round(Math.random() * 1000);
            // 将随机数加上i后作为参数创建DefaultHashFunction对象，并将其添加到列表中
            hashFunctionList.add(new DefaultHashFunction(random + i));
        }
        // 返回列表
        return hashFunctionList;
    }
}
