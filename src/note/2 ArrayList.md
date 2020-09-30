## ArrayList

ArrayList 是一个动态数组，它是线程不安全的，允许元素为 null，底层数据结构依然是数组。

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200922162748.png)

### 1. 构造方法

1. 对于 EMPTY_ELEMENTDATA 和 DEFAULTCAPACITY_EMPTY_ELEMENTDATA 空数组，二者名字不同是为了区分在构造时有无指定初始容量，后续在动态扩容时会有区别，其中默认容量采用懒加载的方式，即在增加元素时才开辟数组空间。
2. 官方 bug 6260652，Collection.toArray() 方法返回的类型不一定是 Object[]，主要因为继承的原因，父类实例的具体类型取决于在 new 时子类的类型。
3. Arrays.copyOf() 方法根据传入的类型决定是 new 还是反射来构造数组，保证返回的一定是 Object[]。
4. Array.copyOf() 可以看作是受限的 System.arraycopy() ,它主要是用来将原数组全部拷贝到一个新长度的数组，适用于数组扩容。

```java
// default constructor's emtpy array
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
// store element
transient Object[] elementData;

private int size;
// default constructor, lazy load
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

// empty array
private static final Object[] EMPTY_ELEMENTDATA = {};
// constructor with initialCapacity
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}

public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```

### 2. 增

1. 每次 add 时先判断是否需要扩容，若扩容，则默认扩容一半；若扩容一半不够（初始或 addAll() 时），则用所需最小容量作为当前容量。扩容采用复制数组实现。
2. modCount 主要用于记录对 ArrayList 的修改次数，标识线程安全。如果一个线程操作期间 modCount 发生了变化，会抛出“ConcurrentModificationException”异常，又称为“failFast”机制。此机制也主要用于迭代器、加强 for 循环等中。

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}

private static final int DEFAULT_CAPACITY = 10;

private static int calculateCapacity(Object[] elementData, int minCapacity) {
    // if default construct
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

### 3. 删

1. 当删除元素不在数组末尾时，需要复制数组移动，较为耗时。
2. removeAll() 方法删除集合中所拥有的共同元素。

```java
public E remove(int index) {
    rangeCheck(index);

    modCount++;
    E oldValue = elementData(index);

    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work

    return oldValue;
}

E elementData(int index) {
    return (E) elementData[index];
}

public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}

private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work
}

public boolean removeAll(Collection<?> c) {
    Objects.requireNonNull(c);
    return batchRemove(c, false);
}

private boolean batchRemove(Collection<?> c, boolean complement) {
    final Object[] elementData = this.elementData;
    int r = 0, w = 0;
    boolean modified = false;
    try {
        for (; r < size; r++)
            if (c.contains(elementData[r]) == complement)
                elementData[w++] = elementData[r];
    } finally {
        // Preserve behavioral compatibility with AbstractCollection,
        // even if c.contains() throws.
        if (r != size) {
            System.arraycopy(elementData, r,
                             elementData, w,
                             size - r);
            w += size - r;
        }
        if (w != size) {
            // clear to let GC do its work
            for (int i = w; i < size; i++)
                elementData[i] = null;
            modCount += size - w;
            size = w;
            modified = true;
        }
    }
    return modified;
}
```

### 4. 改

不用修改 modCount。

```java
public E set(int index, E element) {
    rangeCheck(index);

    E oldValue = elementData(index);
    elementData[index] = element;
    return oldValue;
}
```

### 5. 查

不用修改 modCount。

```java
public E get(int index) {
    rangeCheck(index);

    return elementData(index);
}
```

### 6. 清空

```java
public void clear() {
    modCount++;

    // clear to let GC do its work
    for (int i = 0; i < size; i++)
        elementData[i] = null;

    size = 0;
}
```

### 7. 包含

```java
public boolean contains(Object o) {
    return indexOf(o) >= 0;
}

public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```

### 8. 迭代器

1. 在增强 for 循环中，如果不采用迭代器的方式进行元素删除时，会抛出 ConcurrentModificationException 异常。但是只有在删除倒数第二个元素时，并不会报出异常，主要原因在于 hasNext() 方法返回 false，不会进入 next() 方法的 checkForComodification 中。
2. fail-fast 机制，增强 for 循环，其实现原理是借助 Iterator 进行元素的遍历。如果在遍历过程中，不通过 Iterator，而是通过集合类自身的方法对集合进行添加/删除操作，那么在 Iterator 进行下一次的遍历时，经检测发现有一次集合的修改操作并未通过其自身进行，那么可能是并发被其他线程执行的，这时候就会抛出异常，来提示用户可能发生了并发修改，这就是所谓的 fail-fast 机制。
3. 解决上述问题可使用 Iterator 进行元素删除、使用 Stream 的 filter、使用 fail-safe 的类等。其中 fail-safe 的类指先复制原有集合内容，后在拷贝的集合上进行遍历。

```java
public Iterator<E> iterator() {
    return new Itr();
}

private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    Itr() {}

    public boolean hasNext() {
        return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
        checkForComodification();

        try {
            ArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }


    public void forEachRemaining(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        final int size = ArrayList.this.size;
        int i = cursor;
        if (i >= size) {
            return;
        }
        final Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length) {
            throw new ConcurrentModificationException();
        }
        while (i != size && modCount == expectedModCount) {
            consumer.accept((E) elementData[i++]);
        }
        // update once at end of iteration to reduce heap write traffic
        cursor = i;
        lastRet = i - 1;
        checkForComodification();
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

### 9. 参考

[搞懂 Java ArrayList 源码](https://juejin.im/post/6844903582194466824)

[从最基本的 ArrayList 谈起](https://www.nowcoder.com/tutorial/10029/746a5793abfc4981a76342db5e7eb57e)