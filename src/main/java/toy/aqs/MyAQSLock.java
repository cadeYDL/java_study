package toy.aqs;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class MyAQSLock {
    private final AtomicInteger state = new AtomicInteger(0);
    private transient Thread owner;

    private final AtomicReference<Node> head;
    private final AtomicReference<Node> tail;

    static final class Node {
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;

        Node(Thread thread) {
            this.thread = thread;
        }
        Node() {} // For dummy head
    }

    public MyAQSLock() {
        Node dummy = new Node();
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void lock() {
        if (state.compareAndSet(0, 1)) {
            owner = Thread.currentThread();
            return;
        }
        if (owner == Thread.currentThread()) {
            state.incrementAndGet();
            return;
        }

        Node node = addWaiter();
        acquireQueued(node);
    }

    public void unlock() {
        if (owner != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }
        if (state.decrementAndGet() == 0) {
            owner = null;
            Node h = head.get();
            if (h.next != null) {
                unparkSuccessor(h);
            }
        }
    }

    private void acquireQueued(final Node node) {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.prev;
            if (p == head.get() && state.compareAndSet(0, 1)) {
                setHead(node);
                p.next = null; // help GC
                owner = Thread.currentThread();
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                return;
            }
            interrupted = parkAndCheckInterrupt();
        }
    }

    private Node addWaiter() {
        Node newNode = new Node(Thread.currentThread());
        Node pred = tail.get();
        newNode.prev = pred;
        if (tail.compareAndSet(pred, newNode)) {
            pred.next = newNode;
            return newNode;
        }
        // Fallback to spin enq
        enq(newNode);
        return newNode;
    }

    private void enq(final Node node) {
        for (;;) {
            Node t = tail.get();
            node.prev = t;
            if (tail.compareAndSet(t, node)) {
                t.next = node;
                return;
            }
        }
    }

    private void setHead(Node node) {
        head.set(node);
        node.thread = null;
        node.prev = null;
    }

    private void unparkSuccessor(Node h) {
        Node s = h.next;
        if (s != null && s.thread != null) {
            LockSupport.unpark(s.thread);
        }
    }

    private boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted(); // Clear interrupt status
    }
}
