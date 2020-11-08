package test.map;

import java.util.*;

public class HashMapTest {

    public static void main(String[] args) {
        Random r = new Random();
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        // 添加操作
        map.put("one", r.nextInt(10));
        map.put("two", r.nextInt(10));
        map.put("three", r.nextInt(10));

        // 打印出 map
        System.out.println("map : " + map);

        // 通过 Iterator 遍历 key-value
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            System.out.println("next : "+ entry.getKey() +" - "+entry.getValue());
        }

        // HashMap 的键值对个数
        System.out.println("size : " + map.size());

        // containsKey(Object key) : 是否包含键 key
        System.out.println("contains key two : " + map.containsKey("two"));
        System.out.println("contains key five : " + map.containsKey("five"));

        // containsValue(Object value) : 是否包含值 value
        System.out.println("contains value 0 : " + map.containsValue(new Integer(0)));

        // remove(Object key) ： 删除键 key 对应的键值对
        map.remove("three");

        System.out.println("map : " + map );

        // clear() ： 清空 HashMap
        map.clear();

        // isEmpty() : HashMap 是否为空
        System.out.println((map.isEmpty() ? "map is empty" : "map is not empty") );
    }

}
