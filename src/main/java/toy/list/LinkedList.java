package toy.list;

import java.util.Iterator;

public class LinkedList<E> implements List<E> {
    private Node<E> head,tail;
    private int size;
    public LinkedList() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, null, head);
        head.next = tail;
    }
    @Override
    public void add(E element) {
        var old = tail.pre;
        var newNode = new Node<>(element,tail,old);
        old.next = newNode;
        tail.pre = newNode;
        size++;
    }

    @Override
    public void add(E element, int index) {
        Node<E> node = findNode(index);
        var oldPre = node.pre;
        var newNode = new Node<>(element,node,oldPre);
        node.pre = newNode;
        oldPre.next = newNode;
        size++;
    }

    private boolean outOfBounds(int index) {
        return !(index < size && index >= 0);
    }

    @Override
    public E remove(int index) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> delNode = findNode(index);
        delNode.pre.next = delNode.next;
        delNode.next.pre = delNode.pre;
        delNode.pre = null;
        delNode.next = null;
        size--;
        return delNode.element;
    }

    @Override
    public boolean remove(E element) {
        Node<E> deleteNode = findNode(element);
        if (deleteNode == null) {
            return false;
        }
        deleteNode.pre.next = deleteNode.next;
        deleteNode.next.pre = deleteNode.pre;
        deleteNode.pre = null;
        deleteNode.next = null;
        size--;
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = findNode(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    private Node<E> findNode(int index) {
        if (index>size>>1){
            index = size-index;
            Node<E> node = this.tail;
            while (index-->0){
                node = this.tail.pre;
            }
            return node;
        }
        index+=1;
        Node<E> node = head;
        while (index-->0){
            node = node.next;
        }
        return node;
    }

    private Node<E> findNode(E element) {
        Node<E> node = head;
        while (node.next!=null){
            node = node.next;
            if (element.equals(node.element)){
                return node;
            }
        }
        return null;
    }

    @Override
    public E get(int index) {
        Node<E> node = findNode(index);
        return node.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter() ;
    }

    private class Iter implements Iterator<E> {
        Node<E> cursor = head;
        @Override
        public boolean hasNext() {
            return cursor.next != null && cursor.next != tail;
        }

        @Override
        public E next() {
            cursor = cursor.next;
            return cursor.element;
        }
    }

    private class Node<E> {
        private E element;
        private Node<E> next,pre;
        private Node(E element, Node<E> next,Node<E> pre) {
            this.element = element;
            this.next = next;
            this.pre = pre;
        }
    }
}
