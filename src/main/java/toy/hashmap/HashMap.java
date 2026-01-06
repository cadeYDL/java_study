package toy.hashmap;

import lombok.Getter;

import java.util.Iterator;
import java.util.function.BiPredicate;

public class HashMap<K extends Comparable<K>,V> {

    private EntryHead<K,V>[] table;

    @Getter
    private int size;

    public HashMap(int size) {
        table = new EntryHead[get2Size(size)];
    }
    private int get2Size(int size) {
        size--;
        size |= size >> 1;
        size |= size >> 2;
        size |= size >> 4;
        size |= size >> 8;
        size |= size >> 16;
        size++;
        return size;
    }

    private int getIndex(K key) {
        return getIndex(key, table.length);
    }

    private int getIndex(K key,int size) {
        return key.hashCode() & (size-1);
    }

    public void put(K key, V value) {
        Entry<K,V> e = getEntry(key);
        if (e != null) {
            e.value = value;
            return;
        }
        Entry<K,V> newEntry = new Entry<>(key, value);
        size++;
        resizeIfNecessary();
        putEntry(newEntry,this.table);
    }

    public void forEach(BiPredicate<K,V> consumer) {
        int startInx = (int) (System.currentTimeMillis() & (table.length-1));
        for (int i = 0; i < table.length; i++) {
            int inx = (startInx+i)&(table.length-1);
            EntryHead<K,V> head  = this.table[inx];
            if (head == null) {
                continue;
            }
            head.getIterator().forEachRemaining((enrty)->{
                consumer.test(enrty.key,enrty.value);
            });
        }
    }

    private void putEntry(Entry<K,V> entry,EntryHead<K,V>[] table) {
        int inx = this.getIndex(entry.key,table.length);
        EntryHead<K,V> head = table[inx];
        if (head==null){
            head = new EntryHead<>();
        }
        head.addEntry(entry);
        table[inx] = head;
    }

    private void resizeIfNecessary() {
        if (size < table.length*0.75) {
            return;
        }
        EntryHead<K,V>[] newTable = new EntryHead[table.length<<1];
        for(EntryHead<K,V> head : table){
            if (head==null){
                continue;
            }
            Iterator<Entry<K,V>> itr = head.getIterator();
            while (itr.hasNext()) {
                Entry<K,V> entry = itr.next();
                putEntry(new Entry<>(entry.key,entry.value),newTable);
            }
        }
        this.table = newTable;
    }

    private Entry<K,V> getEntry(K key) {
        int inx = this.getIndex(key);
        EntryHead<K,V> head = table[inx];
        if (head==null){
            return null;
        }
        return head.getEntry(key);
    }

    public V get(K key) {
        Entry<K,V> e = getEntry(key);
        if (e == null) {
            return null;
        }
        return e.value;
    }

    public V remove(K key) {
        int inx = this.getIndex(key);
        Entry<K,V> entry = table[inx].removeEntry(key);
        if (entry == null) {
            return null;
        }
        size--;
        return entry.value;
    }


    private class EntryHead<K,V>{
        Entry<K,V> head;
        int size;
        void addEntry(Entry<K,V> entry) {
            size++;
            if(head==null){
                head = entry;
                return;
            }
            head.prev = entry;
            entry.next = head;
            head = entry;
        }

        Entry<K,V> removeEntry(K key){
            if(head==null){
                return null;
            }
            Entry<K,V> prev=null;
            Iterator<Entry<K,V>> iterator = getIterator();
            while (iterator.hasNext()) {
                Entry<K,V> entry = iterator.next();
                if(entry.key.equals(key)){
                    entry.prev = null;
                    Entry<K,V> newEntry = entry.next;
                    entry.next = null;
                    if (newEntry!=null){
                        newEntry.prev = null;
                    }
                    if (prev==null){
                        head = newEntry;
                    }else{
                        prev.next = newEntry;
                    }
                    size--;
                    return entry;
                }
                prev = entry;
            }
            return null;
        }

        public Iterator<Entry<K,V>> getIterator() {
            return new Itr(head);
        }

        public Entry<K,V> getEntry(K key) {
            if(head==null){
                return null;
            }
            Iterator<Entry<K,V>> itr = getIterator();
            while (itr.hasNext()) {
                Entry<K,V> entry = itr.next();
                if (entry.key.equals(key)) {
                    return entry;
                }
            }
            return null;
        }

        private class Itr<K,V> implements Iterator<Entry<K,V>> {
            Entry<K,V> cur;
            Itr(Entry<K,V> cur) {
                this.cur = cur;
            }
            @Override
            public boolean hasNext() {
                return cur!=null;
            }

            @Override
            public Entry<K, V> next() {
                if (cur==null){
                    return null;
                }
                Entry<K,V> oldCur = cur;
                cur = cur.next;
                return oldCur;
            }
        }
    }
    private class Entry<K,V>{
         K key;
         V value;
         Entry next,prev;
         public Entry(K key, V value) {
            this.key = key;
            this.value = value;
         }
    }
}
