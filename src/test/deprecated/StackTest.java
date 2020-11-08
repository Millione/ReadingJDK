package test.deprecated;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class StackTest {

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        // 将 1, 2, 3, 4, 5 添加到栈中
        for (int i = 1; i < 6; i++) {
            stack.push(String.valueOf(i));
        }

        // 遍历并打印出该栈
        iteratorThroughRandomAccess(stack);

        // 查找 "2" 在栈中的位置，并输出
        int pos = stack.search("2");
        System.out.println("the postion of 2 is : " + pos);

        // pop 栈顶元素之后，遍历栈
        stack.pop();
        iteratorThroughRandomAccess(stack);

        // peek 栈顶元素之后，遍历栈
        String val = (String)stack.peek();
        System.out.println("peek : " + val);
        iteratorThroughRandomAccess(stack);

        // 通过 Iterator 去遍历 Stack
        iteratorThroughIterator(stack);
    }

    /**
     * 通过快速访问遍历 Stack
     */
    public static void iteratorThroughRandomAccess(List list) {
        String val = null;
        for (int i = 0; i < list.size(); i++) {
            val = (String)list.get(i);
            System.out.print(val + " ");
        }
        System.out.println();
    }

    /**
     * 通过迭代器遍历 Stack
     */
    public static void iteratorThroughIterator(List list) {

        String val = null;
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            val = (String)iter.next();
            System.out.print(val + " ");
        }
        System.out.println();
    }
}
