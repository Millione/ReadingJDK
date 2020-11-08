package test.deprecated;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class HashTableTest {

    public static void main(String[] args) {
        // 初始化随机种子
        Random r = new Random();
        Hashtable<String, Integer> table = new Hashtable<String, Integer>();
        // 添加操作
        table.put("one", r.nextInt(10));
        table.put("two", r.nextInt(10));
        table.put("three", r.nextInt(10));

        // 打印出 table
        System.out.println("table : " + table);

        // 通过 Iterator 遍历 key-value
        Iterator iter = table.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            System.out.println("next : "+ entry.getKey() + " - " + entry.getValue());
        }

        // Hashtable 的键值对个数
        System.out.println("size : " + table.size());

        // containsKey(Object key) : 是否包含键 key
        System.out.println("contains key two : " + table.containsKey("two"));
        System.out.println("contains key five : " + table.containsKey("five"));

        // containsValue(Object value) : 是否包含值 value
        System.out.println("contains value 0 : " + table.containsValue(new Integer(0)));

        // remove(Object key) ： 删除键 key 对应的键值对
        table.remove("three");

        System.out.println("table:" + table);

        // clear() ：清空 Hashtable
        table.clear();

        // isEmpty() : Hashtable 是否为空
        System.out.println((table.isEmpty() ? "table is empty" : "table is not empty") );
    }
}
