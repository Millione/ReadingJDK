package test.set;

import java.util.HashSet;
import java.util.Iterator;

public class HashSetTest {

    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();

        // 将元素添加到 HashSet 中
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add("e");

        // 打印 HashSet 的实际大小
        System.out.printf("size : %d\n", set.size());

        // 判断 HashSet 是否包含某个值
        System.out.printf("HashSet contains a :%s\n", set.contains("a"));
        System.out.printf("HashSet contains g :%s\n", set.contains("g"));

        // 删除 HashSet 中的 "e"
        set.remove("e");

        // 将Set转换为数组
        String[] arr = (String[])set.toArray(new String[0]);
        for (String str : arr) {
            System.out.printf("for each : %s\n", str);
        }

        // 新建一个包含 b、c、f 的 HashSet
        HashSet<String> otherSet = new HashSet<>();
        otherSet.add("b");
        otherSet.add("c");
        otherSet.add("f");

        HashSet<String> removeSet = (HashSet)set.clone();
        removeSet.removeAll(otherSet);
        System.out.printf("removeSet : %s\n", removeSet);

        HashSet<String> retainSet = (HashSet)set.clone();
        retainSet.retainAll(otherSet);
        System.out.printf("retainSet : %s\n", retainSet);

        // 遍历 HashSet
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            System.out.printf("iterator : %s\n", iterator.next());
        }

        // 清空 HashSet
        set.clear();

        // 输出 HashSet 是否为空
        System.out.printf("%s\n", set.isEmpty()?"set is empty":"set is not empty");
    }
}
