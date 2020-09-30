## Vector

Vector 底层是数组实现，是一个动态数组，允许元素为 null，且是线程安全的，因而在单线程情况下操作效率较低。

![](https://raw.githubusercontent.com/Millione/pb/master/img/20200926204915.png)

### 1. 构造方法

无参构造方法的容量为默认值 10，仅包含容量的构造方法则将容量增长量明置为0。

```java
protected Object[] elementData;

protected int elementCount;

protected int capacityIncrement;

public Vector() {
    this(10);
}

public Vector(int initialCapacity) {
    this(initialCapacity, 0);
}

public Vector(int initialCapacity, int capacityIncrement) {
    super();
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    this.elementData = new Object[initialCapacity];
    this.capacityIncrement = capacityIncrement;
}

public Vector(Collection<? extends E> c) {
    elementData = c.toArray();
    elementCount = elementData.length;
    // c.toArray might (incorrectly) not return Object[] (see 6260652)
    if (elementData.getClass() != Object[].class)
        elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
}
```

### 2. 增

同 ArrayList 一样，Vector 在每次增加元素时，都要先确保有足够的容量。当容量不足时，就判断容量增长量 capacityIncrement 是否为 0，如果不为 0，则新的容量为旧容量加上容量增长量，否则就为旧容量的两倍。如果扩容后容量还不够，则直接更新为传入的最小容量值。

```java
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}

private void ensureCapacityHelper(int minCapacity) {
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                     capacityIncrement : oldCapacity);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
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

```java
    public synchronized E remove(int index) {
        modCount++;
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);
        E oldValue = elementData(index);

        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--elementCount] = null; // Let gc do its work

        return oldValue;
    }
```

### 4. 改

不用修改 modCount。

```java
public synchronized E set(int index, E element) {
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);

    E oldValue = elementData(index);
    elementData[index] = element;
    return oldValue;
}
```

### 5. 查

不用修改 modCount。

```java
public synchronized E get(int index) {
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);

    return elementData(index);
}
```

### 6. 清空

```java
public void clear() {
    removeAllElements();
}

public synchronized void removeAllElements() {
    modCount++;
    // Let gc do its work
    for (int i = 0; i < elementCount; i++)
        elementData[i] = null;

    elementCount = 0;
}
```

### 7. 包含

```java
public boolean contains(Object o) {
    return indexOf(o, 0) >= 0;
}

public synchronized int indexOf(Object o, int index) {
    if (o == null) {
        for (int i = index ; i < elementCount ; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = index ; i < elementCount ; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```

### 8. 迭代器

```java
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    public boolean hasNext() {
        // Racy but within spec, since modifications are checked
        // within or after synchronization in next/previous
        return cursor != elementCount;
    }

    public E next() {
        synchronized (Vector.this) {
            checkForComodification();
            int i = cursor;
            if (i >= elementCount)
                throw new NoSuchElementException();
            cursor = i + 1;
            return elementData(lastRet = i);
        }
    }

    public void remove() {
        if (lastRet == -1)
            throw new IllegalStateException();
        synchronized (Vector.this) {
            checkForComodification();
            Vector.this.remove(lastRet);
            expectedModCount = modCount;
        }
        cursor = lastRet;
        lastRet = -1;
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        synchronized (Vector.this) {
            final int size = elementCount;
            int i = cursor;
            if (i >= size) {
                return;
            }
    @SuppressWarnings("unchecked")
            final E[] elementData = (E[]) Vector.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                action.accept(elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```
