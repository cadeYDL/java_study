package toy.list;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayList<E> implements List<E> {
    private Object[] slice;
    private int size;

    public ArrayList() {
        slice = new Object[10];
    }

    public ArrayList(int capacity) {
        slice = new Object[capacity];
    }

    @Override
    public void add(E element) {
       this.growth();
       this.slice[this.size] = element;
       this.size++;
    }

    @Override
    public void add(E element, int index) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.size - 1) {
            add(element);
        }
        this.growth();
        System.arraycopy(this.slice, index+1, this.slice, index, this.size-index);
        this.slice[index] = element;
        this.size++;
    }

    private void growth() {
        if (size < slice.length*0.75) {
            return;
        }
        var newSlice = new Object[slice.length<<1];
        System.arraycopy(slice, 0, newSlice, 0, size);
        slice = newSlice;
    }

    private boolean outOfBounds(int index) {
        return !(index < size && index >= 0);
    }

    @Override
    public E remove(int index) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        Object delNode = this.slice[index];
        if (index != this.size - 1) {
            System.arraycopy(this.slice, index+1, this.slice, index, this.size-index);
        }
        this.slice[this.size-1] = null;
        this.size--;
        return (E) delNode;
    }

    @Override
    public boolean remove(E element) {
        for (int i = 0; i < this.size; i++) {
            if (this.slice[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        var old = this.slice[index];
        slice[index] = element;
        return (E) old;
    }

    @Override
    public E get(int index) {
        if (outOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
        return (E) slice[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<E> {
        private int cursor;
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            return (E) slice[cursor++];
        }
    }
}
