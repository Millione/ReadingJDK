## HashMap

HashMap 是 key/value 的存储结构，每个 key 对应唯一的 value，允许 key 为 null 的情况，查询和修改的平均时间复杂度是$O(1)$级别的。它是非线程安全的，且不保证元素存储的顺序。

![image-20200928095931341](C:\Users\LiuJie\AppData\Roaming\Typora\typora-user-images\image-20200928095931341.png)

### 1. 构造方法

1. initialCapacity 初始容量默认 16，如果在知道需要存储数据大小的情况下，指定合适的初始容量，可以避免不必要的扩容操作，提升效率。
2. Threshold 通过 initialCapacity * loadFactor 计算，如果桶的使用数量超过它，则需要进行扩容。
3. LoadFactor 默认负载因子 0.75，负载因子是时间和空间上的折中，当负载因子较大时，不太容易发生扩容操作，因而相对占用内存较少，但每条 Entry 链上的元素就会相对较多，查询较为耗时。
4. tableSizeFor() 方法找到大于等于容量的最小 2 的指数幂。

```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

static final int MAXIMUM_CAPACITY = 1 << 30;

static final float DEFAULT_LOAD_FACTOR = 0.75f;

static final int TREEIFY_THRESHOLD = 8;

static final int UNTREEIFY_THRESHOLD = 6;

// 当桶数达到 64 才可以进行树化
static final int MIN_TREEIFY_CAPACITY = 64;

// 数组，又叫作 bucket
transient Node<K,V>[] table;

// 作为 entrySet() 的缓存
transient Set<Map.Entry<K,V>> entrySet;

transient int size;

int threshold;

final float loadFactor;


public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}

public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}

public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}

public HashMap(Map<? extends K, ? extends V> m) {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}

// Returns a power of two size for the given target capacity
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}


```

### 2. 内部类

1. Node 是一个典型的单链表节点，其中 hash 用来存储 key 计算的 hash 值。
2. TreeNode 继承自 LinkedHashMap 中的 Entry 类。

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }
    public final String toString() { return key + "=" + value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}

static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;
    TreeNode(int hash, K key, V val, Node<K,V> next) {
        super(hash, key, val, next);
    }
```

### 3. put

put() 方法较为复杂，大致可分为三种情况：

1. table 尚未初始化，对 table 进行初始化。
   - 如果使用默认构造函数，则第一次插入元素时进行初始化，容量为 16，扩容门限是 12。
   - 如果使用非默认构造函数，则第一次插入元素时初始化容量为扩容门限，由之前的 tableSizeFor() 计算得出。
   - 如果旧容量大于 0，则新容量等于旧容量的两倍，新扩容门限也为旧扩容门限的 2 倍，注意不能超过最大值。
   - 扩容后创建新 table，并对所有数据进行遍历。
     - 如果新计算的 hash 位置桶中只有一个元素，则计算它在新桶中的位置并把它搬移过去（此时新桶中不可能有元素）。
     - 如果新计算的 hash 位置桶中第一个元素是树节点，则将树分隔成两棵树插入到新桶中。
     - 如果新计算的 hash 位置桶中第一个元素是链表节点，则分化成高低两个链表插入到新桶中，分化标准为原始哈希值与上旧容量是否为 1。
2. table 已经初始化，且通过 hash 算法找到数据相对应位置为空，直接存入数据。
3. table 已经初始化，且通过 hash 算法找到数据相对应位置不为空，即发生 hash 碰撞，之后执行如下操作：
   - 判断插入的 key 是否等于当前位置的 key，如果是则用 e 存下来当前节点。
   - 如果不等于且桶中数据类型为 TreeNode，则用红黑树进行插入。
   - 如果数据类型是链表，则进行循环判断。如果链表中包含该节点，跳出循环；如果不包含则把该节点插入到末尾，同时若链表长度超过树化阈值 8 且 桶数超过最小树化容量 64，则进行链表转红黑树。

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

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

// expansion
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

### 4. get

get 逻辑相对简单：

1. 根据 hash 值查找到指定位置的数据。
2. 如果第一个节点位置的 key 是传入的 key，则直接返回第一个节点，否则继续查找第二个节点。
3. 如果数据类型是 TreeNode，则通过红黑树查找节点数据并返回。
4. 如果是链表结构，循环查找所有节点返回数据。
5. 如果没有找到符合要求的数据，则返回 null。

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
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
```

### 5. hash

1. hash() 扰动函数，让 hashcode 值和无符号右移 16 位后的 hashcode 值进行异或，是为了让高位与低位进行混合，两者都参与运算，使 hash 值分布更加均匀。
2. 使用 hash & (n - 1) 代替取余运算，要求 n 为 2 的幂次方。

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

first = tab[(n - 1) & hash]) 
```

### 6. remove

通过 key 找到需要移除的元素的过程和 get 方法几乎一致，最后在查找到 key 对应的节点之后，根据节点的位置和类型，进行相应的删除操作即可。

```java
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}

final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {
            if (p instanceof TreeNode)
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            else if (node == p)
                tab[index] = node.next;
            else
                p.next = node.next;
            ++modCount;
            --size;
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```

### 7.多线程 resize 成环

JDK1.7 关键代码：

```java
public V put(K key, V value) {
    ......
    // 计算 Hash 值
    int hash = hash(key.hashCode());
    int i = indexFor(hash, table.length);
    // 如果该 key 已被插入，则替换掉旧的 value（链接操作）
    for (Entry<K,V> e = table[i]; e != null; e = e.next) {
        Object k;
        if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
            V oldValue = e.value;
            e.value = value;
            e.recordAccess(this);
            return oldValue;
        }
    }
    modCount++;
    // 该 key 不存在，需要增加一个结点
    addEntry(hash, key, value, i);
    return null;
}

void addEntry(int hash, K key, V value, int bucketIndex) {
    Entry<K,V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
    // 查看当前的 size 是否超过了设定的阈值 threshold，如果超过，需要 resize
    if (size++ >= threshold)
        resize(2 * table.length);
}

void resize(int newCapacity) {
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
    ......
    // 创建一个新的 Hash Table
    Entry[] newTable = new Entry[newCapacity];
    // 将 Old Hash Table 上的数据迁移到 New Hash Table 上
    transfer(newTable);
    table = newTable;
    threshold = (int)(newCapacity * loadFactor);
}

void transfer(Entry[] newTable) {
    Entry[] src = table;
    int newCapacity = newTable.length;
    // 从OldTable里摘一个元素出来，然后放到NewTable中
    for (int j = 0; j < src.length; j++) {
        Entry<K,V> e = src[j];
        if (e != null) {
            src[j] = null;
            do {
                Entry<K,V> next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            } while (e != null);
        }
    }
}
```

正常 resize() 过程：

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200930110012.png)

并发过程下 resize():

```java
do {
    Entry<K,V> next = e.next; // 线程一执行到这 cpu 时间片用完，被调度挂起
    int i = indexFor(e.hash, newCapacity);
    // newTable[i] 看成链表头，初始为 null
    e.next = newTable[i];
    newTable[i] = e;
    e = next;
} while (e != null);
```

1. 假设有两个线程在执行，线程一挂起，线程二执行完成后：

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200930110438.png)

2. 线程一被调度执行:

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200930110751.png)

3. 

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200930111217.png)

4. 执行以下两句成环

   ```java
   e.next = newTable[i];
   newTable[i] = e;
   ```

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200930111914.png)

概况来说，JDK1.7 HashMap 在多线程扩容时形成闭环的原因主要在于产生新链表的顺序和旧链表是完全相反的。在 JDK1.8 中采用头尾指针的方式保证产生新链表的顺序与旧链表是一致的。

### 8. 总结

1. HashMap 是一种散列表，底层数据结构在 JDK1.7 之前是由数组加链表实现，1.8 之后引入了红黑树。当桶的链表长度小于 8 时，发生 hash 冲突后会增加链表长度，当链表长度大于等于 8 的时候，会先判断桶的数量，如果容量小于 64 则会先扩容，大于 64 则会将链表转为红黑树以提升效率。
2. HashMap 的容量是 2 的 n 次幂，无论初始传入容量是多少，最终都会转化成 2 的 n 次幂，一是可以用位运算代替取模运算，提升效率；二是可以在一定程度上降低 hash 冲突。
3. HashMap 是非线程安全的容器，在多线程的操作下会存在异常情况，如多线程 resize 形成闭环（JDK1.7 之前，1.8之后修复），多线程 put 元素丢失（并发本身问题），多线程 put 元素后 get 为 null。

### 9. 参考

[讨论一种特殊的 K.V 存储方式 HashMap(上)](https://www.nowcoder.com/tutorial/10029/13657f0ede704a90809989665311c6a0)

[讨论一种特殊的 K.V 存储方式 HashMap(下)](https://www.nowcoder.com/tutorial/10029/d5be3271ed9b49db9f659615726610d1)