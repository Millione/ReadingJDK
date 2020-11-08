package test.set;

import java.util.Iterator;
import java.util.TreeSet;

public class TreeSetTest {

    public static void main(String[] args) {
        String val;
        TreeSet<String> tSet = new TreeSet<>();
        // 将元素添加到 TreeSet 中
        tSet.add("aaa");
        // Set 中不允许重复元素，所以只会保存一个"aaa"
        tSet.add("aaa");
        tSet.add("bbb");
        tSet.add("eee");
        tSet.add("ddd");
        tSet.add("ccc");
        System.out.println("TreeSet : " + tSet);

        // 打印 TreeSet 的实际大小
        System.out.printf("size : %d\n", tSet.size());

        // floor(小于、等于)
        System.out.printf("floor bbb : %s\n", tSet.floor("bbb"));
        // lower(小于)
        System.out.printf("lower bbb : %s\n", tSet.lower("bbb"));
        // ceiling(大于、等于)
        System.out.printf("ceiling bbb : %s\n", tSet.ceiling("bbb"));
        System.out.printf("ceiling eee : %s\n", tSet.ceiling("eee"));
        // ceiling(大于)
        System.out.printf("higher bbb : %s\n", tSet.higher("bbb"));
        // subSet()
        System.out.printf("subSet(aaa, true, ccc, true) : %s\n", tSet.subSet("aaa", true, "ccc", true));
        System.out.printf("subSet(aaa, true, ccc, false) : %s\n", tSet.subSet("aaa", true, "ccc", false));
        System.out.printf("subSet(aaa, false, ccc, true) : %s\n", tSet.subSet("aaa", false, "ccc", true));
        System.out.printf("subSet(aaa, false, ccc, false) : %s\n", tSet.subSet("aaa", false, "ccc", false));
        // headSet()
        System.out.printf("headSet(ccc, true) : %s\n", tSet.headSet("ccc", true));
        System.out.printf("headSet(ccc, false) : %s\n", tSet.headSet("ccc", false));
        // tailSet()
        System.out.printf("tailSet(ccc, true) : %s\n", tSet.tailSet("ccc", true));
        System.out.printf("tailSet(ccc, false) : %s\n", tSet.tailSet("ccc", false));


        // 删除"ccc"
        tSet.remove("ccc");
        // 将 Set 转换为数组
        String[] arr = (String[]) tSet.toArray(new String[0]);
        for (String str : arr) {
            System.out.printf("for each : %s\n", str);
        }

        // 打印 TreeSet
        System.out.printf("TreeSet : %s\n", tSet);

        // 遍历 TreeSet
        for (Iterator iter = tSet.iterator(); iter.hasNext(); ) {
            System.out.printf("iter : %s\n", iter.next());
        }

        // 删除并返回第一个元素
        val = (String) tSet.pollFirst();
        System.out.printf("pollFirst = %s, set = %s\n", val, tSet);

        // 删除并返回最后一个元素
        val = (String) tSet.pollLast();
        System.out.printf("pollLast = %s, set = %s\n", val, tSet);

        // 清空 TreeSet
        tSet.clear();

        // 输出 TreeSet 是否为空
        System.out.printf("%s\n", tSet.isEmpty() ? "set is empty" : "set is not empty");
    }
}
