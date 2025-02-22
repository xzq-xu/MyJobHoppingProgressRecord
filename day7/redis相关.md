# redis 面试相关


## redis使用中常见的问题

### 缓存击穿
系统运行中某个key失效了，恰好此时这个key的并发量很高，让大量的请求直接打到了数据库
可能的原因：该key为热点数据，或者突然变为key
解决方案： 
1. 设置key点的key永不过期
2. 给redis查询加锁让请求排队，使用双重检查锁，避免大量阻塞

### 缓存雪崩
redis中的key在某个时刻集中失效了，使得高并发的请求量直接打到数据库
可能的原因：大量的key的过期时间相同； redis服务宕机
解决方案：
1. 为每个key设置过期时间时，加上随机的延时  —————— 针对情况一
2. 为redis做好集群、哨兵以及其他高可用举措（多机房、灾备等）

### 缓存穿透
用户请求到了redis中没有的数据，导致请求依然打到了数据库
可能的原因：请求不存在的id、名称等
解决方案：
1. 参数校验     通过校验剔除一些明显不可能存在的请求
2. 缓存对象   即使从数据库中未查询，依然缓存该key，值为空对象
3. 布隆过滤器   

#### 布隆过滤器

[代码位置](./MyBloomFliter/src/main/java/site/xzq_xu/bloom/BloomFilter.java)

##### 计数布隆过滤器
判断存在
```mermaid
flowchart TD
    START["开始"]
    GET_BITSET["获取BitSet"]
    GET_BITSIZE["获取BitSet的大小"]
    ITERATE_HASHFUNCTIONS["遍历哈希函数列表"]
    CALCULATE_HASH["计算哈希值"]
    CHECK_BITSET["检查BitSet中对应位置"]
    CHECK_COUNTER["检查计数器"]
    RETURN_TRUE["返回true"]
    RETURN_FALSE["返回false"]
    END(("END"))

    START --> GET_BITSET
    GET_BITSET --> GET_BITSIZE
    GET_BITSIZE --> ITERATE_HASHFUNCTIONS
    ITERATE_HASHFUNCTIONS --> CALCULATE_HASH
    CALCULATE_HASH --> CHECK_BITSET
    CHECK_BITSET --> CHECK_COUNTER
    CHECK_COUNTER --> RETURN_TRUE
    CHECK_COUNTER --> ITERATE_HASHFUNCTIONS
    ITERATE_HASHFUNCTIONS --> RETURN_FALSE
    RETURN_TRUE --> END
    RETURN_FALSE --> END

```
添加元素
```mermaid
flowchart TD
    START(["开始"])
    GET_BITSET(["获取BitSet对象"])
    GET_SIZE(["获取BitSet的大小"])
    GET_HASHFUNCTION_LIST(["获取HashFunction列表"])
    FOR_HASHFUNCTION(["遍历HashFunction列表"])
    CALCULATE_HASH(["计算value的哈希值"])
    SET_BITSET(["将BitSet中对应位置的值设置为true"])
    INCREMENT_COUNTER(["将counters数组中对应位置的值加1"])
    END(["结束"])

    START --> GET_BITSET
    GET_BITSET --> GET_SIZE
    GET_SIZE --> GET_HASHFUNCTION_LIST
    GET_HASHFUNCTION_LIST --> FOR_HASHFUNCTION
    FOR_HASHFUNCTION --> CALCULATE_HASH
    CALCULATE_HASH --> SET_BITSET
    SET_BITSET --> INCREMENT_COUNTER
    INCREMENT_COUNTER --> FOR_HASHFUNCTION
    FOR_HASHFUNCTION --> END

```
删除元素
```mermaid
flowchart TD
    START["开始"]
    GET_BIT_SIZE["获取位数"]
    GET_HASH_FUNCTION_LIST["获取哈希函数列表"]
    ITERATE_HASH_FUNCTION_LIST["遍历哈希函数列表"]
    CALCULATE_HASH_VALUE["计算哈希值"]
    CHECK_COUNTER["检查计数器"]
    DECREMENT_COUNTER["减1"]
    END(("END"))

    START --> GET_BIT_SIZE
    GET_BIT_SIZE --> GET_HASH_FUNCTION_LIST
    GET_HASH_FUNCTION_LIST --> ITERATE_HASH_FUNCTION_LIST
    ITERATE_HASH_FUNCTION_LIST --> CALCULATE_HASH_VALUE
    CALCULATE_HASH_VALUE --> CHECK_COUNTER
    CHECK_COUNTER --> |"计数器大于0"| DECREMENT_COUNTER
    CHECK_COUNTER --> |"计数器不大于0"| ITERATE_HASH_FUNCTION_LIST
    DECREMENT_COUNTER --> ITERATE_HASH_FUNCTION_LIST
    ITERATE_HASH_FUNCTION_LIST --> END

```
