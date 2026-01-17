package design_pattern.flyweight;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferPool {
    private final int total;
    private int free;
    private final Lock freeLock = new ReentrantLock();
    Deque<Condition> waiters = new ArrayDeque<>();
    private final Map<Integer, Slot> slots = new HashMap<>();
    private final int maxSlot;

    private class Slot {
        private int bufferSize;
        Deque<Condition> waiters = new ArrayDeque<>();
        Lock lock = new ReentrantLock();
        Deque<ByteBuffer> cache = new ArrayDeque<>();
        Slot(int size){
            bufferSize = size;
        }
    }

    public BufferPool(int total,int maxSlot){
        this.total = total;
        this.free = total;
        this.maxSlot = get2Size(maxSlot);
        for (int i =1;i<=maxSlot;i<<=1){
            slots.put(i,new Slot(i));
        }
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

    public ByteBuffer allocate(int size,long waiteTimeMS) throws Exception {
        if(size > total || size < 0 ){
            throw new IllegalArgumentException();
        }
        if (size > maxSlot){
            freeUp(size);
            return getLargeBuffer(size,waiteTimeMS);
        }

        return getNormalBuffer(size,waiteTimeMS);
    }

    private ByteBuffer getNormalBuffer(int size, long waiteTimeMS) throws Exception {
        size = get2Size(size);
        Slot slot = slots.get(size);
        long deadLine = System.currentTimeMillis() + waiteTimeMS;
        for(;;){
            Condition cond = registerAsWaiter(slot);
            try{
                slot.lock.lock();
                ByteBuffer buffer = waitAndRery(slot,cond,deadLine);
                if(buffer!=null){
                    return buffer;
                }
            }finally {
                slot.waiters.remove(cond);
                slot.lock.unlock();
            }
        }

    }


    private ByteBuffer waitAndRery(Slot slot, Condition cond, long deadline) throws Exception {
        long remaining = deadline - System.currentTimeMillis();
        if (remaining <= 0) {
            throw new TimeoutException("超时");
        }
        ByteBuffer buffer = tryQuickAllocation(slot);
        if(buffer!=null){
            return buffer;
        }
        boolean woke = cond.await(remaining, TimeUnit.MILLISECONDS);
        if (!woke) {
            throw new TimeoutException("超时");
        }

        // 被唤醒后再次尝试
        return tryQuickAllocation(slot);
    }

    private Condition registerAsWaiter(Slot slot) {
        Condition cond = slot.lock.newCondition();
        slot.waiters.addLast(cond);
        return cond;
    }

    private ByteBuffer tryQuickAllocation(Slot slot) {
        if (!slot.cache.isEmpty()){
            return slot.cache.pop();
        }
        try {
            freeLock.lock();
            if (slot.bufferSize <= free) {
                free -= slot.bufferSize;
                return ByteBuffer.allocate(slot.bufferSize);
            }
        } finally {
            freeLock.unlock();
        }
        return null;
    }

    private void freeUp(int size) {
        try{
            freeLock.lock();
            HashSet<Integer> vistMap = new HashSet<>();
            while (free<size){
                int needSize = get2Size(size-free);
                int i = maxSlot;
                for(;(i>needSize||vistMap.contains(i))&&i>1;i>>=1){}
                int needCount = needSize/i;
                Slot slot = slots.get(i);
                try{
                    slot.lock.lock();
                    vistMap.add(i);
                    for(int j=0;j<needCount&&!slot.cache.isEmpty();j++){
                        slot.cache.pop();
                        free += i;
                    }
                }finally {
                    slot.lock.unlock();
                }
            }
        }finally {
            freeLock.unlock();
        }

    }

    private ByteBuffer getLargeBuffer(int size, long waiteTimeMS) throws Exception {
        Condition cond;
        freeLock.lock();
        try {
            freeUp(size);
            if (size <= free) {
                free -= size;
                return ByteBuffer.allocate(size);
            }
            cond = freeLock.newCondition();
        } finally {
            freeLock.unlock();
        }


        long remainTime = waiteTimeMS;
        long start = System.currentTimeMillis();
        freeLock.lock();
        try {
            while (true) {
                freeUp(size);
                if (size <= free) {
                    free -= size;
                    return ByteBuffer.allocate(size);
                }
                boolean wakeup = cond.await(remainTime, TimeUnit.MILLISECONDS);
                if (!wakeup) {
                    throw new TimeoutException("超时了");
                }
                remainTime = remainTime + start - System.currentTimeMillis();
            }
        } finally {
            waiters.remove(cond);
            freeLock.unlock();
        }

    }

    public void deallocate(ByteBuffer buffer) {
        if (buffer.capacity() > maxSlot || !slots.containsKey(buffer.capacity())) {
            try {
                freeLock.lock();
                free += buffer.capacity();
                return;
            } finally {
                freeLock.unlock();
            }
        }


        Slot slot = slots.get(buffer.capacity());

        try {
            slot.lock.lock();
            buffer.clear();
            slot.cache.addLast(buffer);
            Deque<Condition> slotWaiters = slot.waiters;
            if(!slotWaiters.isEmpty()){
                slotWaiters.peek().signal();
            }
        } finally {
            slot.lock.unlock();
        }
    }
}
