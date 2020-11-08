package test.deprecated;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author LiuJie
 */
public class VectorTest {

    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();

        // 添加元素
        vector.add("1");
        vector.add("2");
        vector.add("3");
        vector.add("4");
        vector.add("5");

        // 设置第一个元素为 100
        vector.set(0, "100");
        // 将 "500" 插入到第 3 个位置
        vector.add(2, "300");
        System.out.println("vector : " + vector);

        // 顺序查找获取 100 的索引
        System.out.println("vector.indexOf(100) : " + vector.indexOf("100"));
        // 倒序查找获取 100 的索引
        System.out.println("vector.lastIndexOf(100) : " + vector.lastIndexOf("100"));
        // 获取第一个元素
        System.out.println("vector.firstElement() : " + vector.firstElement());
        // 获取第 3 个元素
        System.out.println("vector.elementAt(2) : " + vector.elementAt(2));
        // 获取最后一个元素
        System.out.println("vector.lastElement() : " + vector.lastElement());

        // 获取 vector 的大小
        System.out.println("size : " + vector.size());
        // 获取 vector 的总的容量
        System.out.println("capacity : " + vector.capacity());

        // 获取 vector 的从 2 到 4 的元素
        System.out.println("vector 2 to 4:" + vector.subList(1, 4));

        // 通过 Enumeration 遍历 vector
        Enumeration enu = vector.elements();
        while (enu.hasMoreElements()) {
            System.out.println("nextElement() : " + enu.nextElement());
        }

        Vector retainVec = new Vector();
        retainVec.add("100");
        retainVec.add("300");
        System.out.println("vector.retain() : " + vector.retainAll(retainVec));
        System.out.println("vector : " + vector);

        // 获取 vector 对应的 String 数组
        String[] arr = (String[]) vector.toArray(new String[0]);
        for (String str : arr) {
            System.out.println("str : " + str);
        }

        vector.clear();
        vector.removeAllElements();

        // 判断 vector 是否为空
        System.out.println("vector.isEmpty() : " + vector.isEmpty());
    }
}
