package toy.jvm;

import java.util.ArrayDeque;
import java.util.Deque;

public class JvmStack {
    private Deque<StackFrame> stack = new ArrayDeque();

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public StackFrame peek() {
        return stack.peek();
    }

    public void push(StackFrame frame) {
        stack.push(frame);
    }

    public StackFrame pop() {
        return stack.pop();
    }

}
