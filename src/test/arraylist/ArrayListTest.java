package test.arraylist;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>(0);
        for (int i = 0; i <= 10; i++) {
            list1.add(i);
            list2.add(i);
        }
        for (Integer i : list1) {
            if (i.equals(9)) {
                list1.remove((Integer)9);
            }
        }
        list1.remove(0);
        list1.set(0, 0);
        if (list1.contains(0)) {
            System.out.println(list1.get(0));
        }
    }

}
