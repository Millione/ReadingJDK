package test.linkedlist;

import java.util.Iterator;
import java.util.LinkedList;

public class LinkedListTest {

    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        // linkedList 的基本操作
        linkedList.addFirst(0);
        linkedList.add(1);
        linkedList.add(2, 2);
        linkedList.addLast(3);

        System.out.println("LinkedList 直接输出: " + linkedList);
        System.out.println("getFirst() 获得第一个元素: " + linkedList.getFirst());
        System.out.println("getLast() 获得第最后一个元素: " + linkedList.getLast());
        System.out.println("removeFirst() 删除第一个元素并返回: " + linkedList.removeFirst());
        System.out.println("removeLast() 删除最后一个元素并返回: " + linkedList.removeLast());
        System.out.println("After remove:" + linkedList);
        System.out.println("contains() 方法判断列表是否包含 1 这个元素:" + linkedList.contains(1));
        System.out.println("linkedList 的大小 : " + linkedList.size());

        // 位置访问操作
        System.out.println("-----------------------------------------");
        linkedList.set(1, 3);
        System.out.println("After set(1, 3):" + linkedList);
        System.out.println("get(1)获得指定位置（这里为 1 ）的元素: " + linkedList.get(1));

        // search 操作
        System.out.println("-----------------------------------------");
        linkedList.add(3);
        System.out.println("indexOf(3): " + linkedList.indexOf(3));
        System.out.println("lastIndexOf(3): " + linkedList.lastIndexOf(3));

        //  queue 操作
        System.out.println("-----------------------------------------");
        System.out.println("peek(): " + linkedList.peek());
        System.out.println("element(): " + linkedList.element());
        linkedList.poll();
        System.out.println("After poll():" + linkedList);
        linkedList.remove();
        System.out.println("After remove():" + linkedList);
        linkedList.offer(4);
        System.out.println("After offer(4):" + linkedList);

        // deque操作
        System.out.println("-----------------------------------------");
        linkedList.offerFirst(2);
        System.out.println("After offerFirst(2):" + linkedList);
        linkedList.offerLast(5);
        System.out.println("After offerLast(5):" + linkedList);
        System.out.println("peekFirst(): " + linkedList.peekFirst());
        System.out.println("peekLast(): " + linkedList.peekLast());
        linkedList.pollFirst();
        System.out.println("After pollFirst():" + linkedList);
        linkedList.pollLast();
        System.out.println("After pollLast():" + linkedList);
        linkedList.push(2);
        System.out.println("After push(2):" + linkedList);
        linkedList.pop();
        System.out.println("After pop():" + linkedList);
        linkedList.add(3);
        linkedList.removeFirstOccurrence(3);
        System.out.println("After removeFirstOccurrence(3):" + linkedList);
        linkedList.removeLastOccurrence(3);
        System.out.println("After removeFirstOccurrence(3):" + linkedList);

        // 遍历操作
        System.out.println("-----------------------------------------");
        linkedList.clear();
        for (int i = 0; i < 100000; i++) {
            linkedList.add(i);
        }

        // 迭代器遍历
        long start = System.currentTimeMillis();
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        long end = System.currentTimeMillis();
        System.out.println("Iterator：" + (end - start) + " ms");

        // 顺序遍历(随机遍历)
        start = System.currentTimeMillis();
        for (int i = 0; i < linkedList.size(); i++) {
            linkedList.get(i);
        }
        end = System.currentTimeMillis();
        System.out.println("for：" + (end - start) + " ms");

        // 另一种for循环遍历
        start = System.currentTimeMillis();
        for (Integer i : linkedList) {

        }
        end = System.currentTimeMillis();
        System.out.println("for2：" + (end - start) + " ms");

        // 通过 pollFirst() 或 pollLast() 来遍历 LinkedList
        LinkedList<Integer> temp1 = new LinkedList<>();
        temp1.addAll(linkedList);
        start = System.currentTimeMillis();
        while (temp1.size() != 0) {
            temp1.pollFirst();
        }
        end = System.currentTimeMillis();
        System.out.println("pollFirst() 或 pollLast() ：" + (end - start) + " ms");

        // 通过 removeFirst() 或 removeLast() 来遍历 LinkedList
        LinkedList<Integer> temp2 = new LinkedList<>();
        temp2.addAll(linkedList);
        start = System.currentTimeMillis();
        while (temp2.size() != 0) {
            temp2.removeFirst();
        }
        end = System.currentTimeMillis();
        System.out.println("removeFirst() 或 removeLast() ：" + (end - start) + " ms");
    }

}
