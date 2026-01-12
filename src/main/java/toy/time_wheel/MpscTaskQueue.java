package toy.time_wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MpscTaskQueue<E> {
    private AtomicReference<Node> head = new AtomicReference<>(null);

    public List<E> getAndRemoveTasks(Predicate<E> predicate) {
        List<E> result = new ArrayList<>();
        Node<E> curNode = head.get();
        Node<E> pre=null;
        while (curNode != null){
            if (predicate.test(curNode.obj)){
                pre = curNode;
                curNode = curNode.next;
                continue;
            }
            if (pre!=null){
                pre.next = curNode.next;
                result.add(curNode.obj);
                curNode.next = null;
                curNode = pre.next;
                continue;
            }
            if(head.compareAndSet(curNode,curNode.next)){
                result.add(curNode.obj);
                pre = curNode.next;
                curNode.next = null;
                curNode = pre;
                pre = null;
                continue;
            }
            curNode = head.get();
        }
        return result;
    }


    public void push(E e) {
        Node newNode = new Node<>(e);
        for(;;){
            Node oldHead = head.get();
            newNode.next = oldHead;
            if(head.compareAndSet(oldHead,newNode)){
                break;
            }
        }
    }

    private class Node<E>{
        private Node next;
        private E obj;

        public Node(E e) {
            obj = e;
        }
    }
}
