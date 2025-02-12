# ConcurrentHashMap源码分析


## HashMap部分源码

```java
/**
 * Returns the value to which the specified key is mapped,
 * or {@code null} if this map contains no mapping for the key.
 *
 * <p>More formally, if this map contains a mapping from a key
 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
 * key.equals(k))}, then this method returns {@code v}; otherwise
 * it returns {@code null}.  (There can be at most one such mapping.)
 *
 * <p>A return value of {@code null} does not <i>necessarily</i>
 * indicate that the map contains no mapping for the key; it's also
 * possible that the map explicitly maps the key to {@code null}.
 * The {@link #containsKey containsKey} operation may be used to
 * distinguish these two cases.
 *
 * @see #put(Object, Object)
 */
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(key)) == null ? null : e.value;
}

/**
 * Implements Map.get and related methods.
 *
 * @param key the key
 * @return the node, or null if none
 */
final Node<K,V> getNode(Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n, hash; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & (hash = hash(key))]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}



/**
 * Associates the specified value with the specified key in this map.
 * If the map previously contained a mapping for the key, the old
 * value is replaced.
 *
 * @param key key with which the specified value is to be associated
 * @param value value to be associated with the specified key
 * @return the previous value associated with {@code key}, or
 *         {@code null} if there was no mapping for {@code key}.
 *         (A {@code null} return can also indicate that the map
 *         previously associated {@code null} with {@code key}.)
 */
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

/**
 * Implements Map.put and related methods.
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to put
 * @param onlyIfAbsent if true, don't change existing value
 * @param evict if false, the table is in creation mode.
 * @return previous value, or null if none
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}

```

## ConcurrentHashMap 部分源码

```java
/**
 * Returns the value to which the specified key is mapped,
 * or {@code null} if this map contains no mapping for the key.
 *
 * <p>More formally, if this map contains a mapping from a key
 * {@code k} to a value {@code v} such that {@code key.equals(k)},
 * then this method returns {@code v}; otherwise it returns
 * {@code null}.  (There can be at most one such mapping.)
 *
 * @throws NullPointerException if the specified key is null
 */
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    // 计算哈希值并确保为正数
    int h = spread(key.hashCode());
    // 检查table是否初始化，并且对应桶位是否有节点
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) { // volatile读保证可见性
        // 先检查头节点
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val; // 直接命中头节点
        }
        // 哈希值为负数表示特殊节点（树节点或ForwardingNode）
        else if (eh < 0)
            return (p = e.find(h, key)) != null ? p.val : null; // 递归查询树节点
        // 遍历链表查找
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null; // 未找到匹配节点
}

/**
 * 插入键值对（不允许null键或值）
 * @param key 键（不能为null）
 * @param value 值（不能为null）
 * @return 先前与键关联的值，如果没有则返回null
 */
public V put(K key, V value) {
    return putVal(key, value, false); // onlyIfAbsent=false表示覆盖现有值
}

/** 
 * 实现put和putIfAbsent的核心方法
 * @param key 键
 * @param value 值
 * @param onlyIfAbsent 如果为true，则不更新现有值
 */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    // 空值检查（ConcurrentHashMap不允许null键值）
    if (key == null || value == null) throw new NullPointerException();
    // 计算哈希并传播高位影响
    int hash = spread(key.hashCode());
    int binCount = 0; // 记录链表长度/树操作标记
    
    // 自旋处理插入操作
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh; K fk; V fv;
        // CASE 1: table未初始化
        if (tab == null || (n = tab.length) == 0)
            tab = initTable(); // 初始化table（CAS保证线程安全）
        // CASE 2: 目标桶为空（无哈希冲突）
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // CAS尝试插入新节点（无锁化操作）
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                break; // 插入成功退出循环
        }
        // CASE 3: 检测到正在扩容（ForwardingNode）
        else if ((fh = f.hash) == MOVED) // MOVED = -1
            tab = helpTransfer(tab, f); // 协助扩容
        // CASE 4: 检查首节点是否匹配（putIfAbsent快速路径）
        else if (onlyIfAbsent // 仅当不存在时才put
                    && fh == hash
                    && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                    && (fv = f.val) != null)
            return fv; // 直接返回现有值
        // CASE 5: 处理哈希冲突
        else {
            V oldVal = null;
            // 锁住桶的首节点（分段锁思想）
            synchronized (f) {
                // 双重检查防止节点变更
                if (tabAt(tab, i) == f) {
                    // 处理链表节点（普通节点哈希>=0）
                    if (fh >= 0) {
                        binCount = 1;
                        // 遍历链表查找或插入
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            // 找到相同key的节点
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                    (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value; // 更新值
                                break;
                            }
                            Node<K,V> pred = e;
                            // 到达链表尾部，插入新节点
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key, value);
                                break;
                            }
                        }
                    }
                    // 处理树节点
                    else if (f instanceof TreeBin) {
                        binCount = 2; // 树操作标记
                        TreeBin<K,V> t = (TreeBin<K,V>)f;
                        // 在红黑树中插入或更新节点
                        TreeNode<K,V> p = t.putTreeVal(hash, key, value);
                        if (p != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            
            // 后处理
            if (binCount != 0) {
                // 链表长度超过阈值转红黑树
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                // 返回旧值（如果存在）
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    // 更新元素计数（使用LongAdder的分段计数思想）
    addCount(1L, binCount);
    return null;
}
```

## 源码解读

HashMap部分：
1. getNode方法解析：
- 使用(n-1)&hash计算桶位置
- 先检查头节点，匹配则直接返回
- 若头节点是树节点，调用getTreeNode进行树查找
- 否则遍历链表查找匹配节点

2. putVal方法关键点：
- 懒加载机制：首次插入时初始化table
- 哈希冲突处理：链表转树阈值（TREEIFY_THRESHOLD=8）
- 扩容机制：size超过threshold时调用resize()
- 树化过程：当链表长度超过阈值且table长度≥64时转为红黑树

ConcurrentHashMap部分：
1. get方法特性：
- 使用tabAt()保证可见性读取（通过Unsafe.getObjectVolatile）
- 支持在扩容过程中查询（通过ForwardingNode）
- 对树节点查询使用find方法

2. putVal并发控制：
- 使用CAS初始化table（initTable中的sizeCtl）
- 空桶插入使用CAS（casTabAt）
- 链表头节点使用synchronized同步
- 扩容时协助迁移（helpTransfer）
- addCount()方法维护size的并发更新



## 对比分析

1. 数据结构差异：
- HashMap使用数组+链表/红黑树
- ConcurrentHashMap（JDK8）采用Node数组+链表/红黑树+CAS

2. 线程安全实现：
- JDK8前使用Segment分段锁（锁分离技术），JDK8改为更细粒度的桶节点锁（synchronized + CAS）
- volatile变量保证可见性：table数组、节点next指针等字段使用volatile修饰
- CAS原子操作：tabAt/casTabAt等Unsafe方法保证原子性
- 扩容机制：多线程协作扩容（helpTransfer），通过ForwardingNode标记迁移状态

3. 性能优化点：
- 锁粒度更细（JDK8锁单个桶节点 vs JDK7锁整个Segment）
- 读操作完全无锁（tabAt使用volatile读）
- 计数器优化（addCount使用LongAdder思想）

## 面试重点总结

1. 数据结构：
- 数组+链表+红黑树结构（与HashMap相同）
- 负载因子0.75，默认初始容量16
- 链表转树阈值8，树转链表阈值6

2. 并发控制：
- JDK8的锁粒度优化（桶级别锁 vs Segment锁）
- synchronized + CAS 的实现原理
- sizeCtl字段的多种状态控制
- ForwardingNode在扩容中的作用

3. 扩容机制：
- 多线程协助扩容原理
- 迁移过程中读写操作的处理
- 容量计算（2的幂次方）

4. 哈希算法：
- spread方法的作用（保证哈希码为正数）
- (n-1) & hash 的快速取模原理

5. size计算方法：
- 分段计数（baseCount + CounterCell[]）
- 统计时如何保证最终一致性

6. 迭代器：
- 弱一致性迭代器原理
- 不会抛出ConcurrentModificationException的原因
