package test.vector;

import java.util.Vector;

public class VectorTest {
    public static void main(String[] args) {
        Vector<Integer> vector1 = new Vector<>();
        Vector<Integer> vector2 = new Vector<>(10);
        for (int i = 0; i <= 10; i++) {
            vector1.add(i);
            vector2.add(i);
        }
        vector1.remove(0);
        vector1.add(11);
        vector1.set(0, 1);
    }
}
