package test.hashmap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapTest {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        // 键不能重复，值可以重复
        map.put("san", "张三");
        map.put("si", "李四");
        map.put("wu", "王五");
        map.put("wang", "老王");
        // 老王被覆盖
        map.put("wang", "老王2");
        map.put("lao", "老王");
        System.out.println("-------直接输出 Map-------");
        System.out.println(map);

        // 获取 Map 中的所有 key
        System.out.println("-------foreach 获取 Map 中所有 key------");
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.print(key + "  ");
        }
        System.out.println();

        // 获取 Map 中所有 value
        System.out.println("-------foreach 获取 Map 中所有 value------");
        Collection<String> values = map.values();
        for (String value : values) {
            System.out.print(value + "  ");
        }
        System.out.println();

        // 得到 key 值的同时得到 value
        System.out.println("-------得到 key 值的同时得到 value-------");
        Set<String> keys2 = map.keySet();
        for (String key : keys2) {
            System.out.print(key + "：" + map.get(key) + "   ");

        }

        // 使用 entry 直接一次获取 key 和 value
        Set<Map.Entry<String, String>> entrys = map.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }

        // HashMap其他常用方法
        System.out.println("map.size()：" + map.size());
        System.out.println("map.isEmpty()：" + map.isEmpty());
        System.out.println(map.remove("san"));
        System.out.println("after map.remove(\"san\")：" + map);
        System.out.println("map.get(\"si\")：" + map.get("si"));
        System.out.println("map.containsKey(\"si\")：" + map.containsKey("si"));
        System.out.println("containsValue(\"李四\")：" + map.containsValue("李四"));
        System.out.println(map.replace("si", "李四2"));
        System.out.println("after map.replace(\"si\", \"李四2\")：" + map);
    }

}
