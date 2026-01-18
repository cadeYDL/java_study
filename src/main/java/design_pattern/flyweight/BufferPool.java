package design_pattern.flyweight;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferPool {
    private final int total;
    private int free;
    private final Lock freeLock = new ReentrantLock();
    Deque<Condition> waiters = new ArrayDeque<>();
    private final AtomicInteger waiterCount = new AtomicInteger(0);
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
                waiterCount.decrementAndGet();
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
        waiterCount.incrementAndGet();
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
            waiters.addLast(cond);
            waiterCount.incrementAndGet();
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
            waiterCount.decrementAndGet();
            freeLock.unlock();
        }

    }

    public void deallocate(ByteBuffer buffer) {
        if (slots.containsKey(buffer.capacity())) {
            Slot slot = slots.get(buffer.capacity());
            try {
                slot.lock.lock();
                if(!slot.waiters.isEmpty()||waiterCount.get()==0){
                    buffer.clear();
                    slot.cache.addLast(buffer);
                    Deque<Condition> slotWaiters = slot.waiters;
                    if(!slotWaiters.isEmpty()){
                        slotWaiters.peek().signal();
                    }
                    return;
                }
            } finally {
                slot.lock.unlock();
            }
        }

        try{
            freeLock.lock();
            free+=buffer.capacity();
            signalOneWaiter();
        }finally {
            freeLock.unlock();
        }
    }

    private boolean signalOne(int size){
        if(!slots.containsKey(size)){
            return false;
        }
        Slot slot = slots.get(size);
        try{
            slot.lock.lock();
            if(!slot.waiters.isEmpty()){
                slot.waiters.peek().signal();
                return true;
            }
        }finally {
            slot.lock.unlock();
        }
        return false;
    }

    private void signalOneWaiter() {
        if(!waiters.isEmpty()){
             waiters.peek().signal();
        }
        int canSignal =get2Size(free);
        if (canSignal>free){
            canSignal>>=1;
        }
        if(slots.containsKey(canSignal)){
            if (signalOne(canSignal)){
                return;
            }
        }
        int left =0,right = canSignal;
        if(canSignal>=maxSlot){
             left = maxSlot>>1;
             right = canSignal;
        }
        while (left>=1 || right<=maxSlot){
            if(right<=maxSlot){
                if (signalOne(right)){
                    return;
                }
                right <<=1;
            }
            if(left>=1){
                if (signalOne(left)){
                    return;
                }
                if (left==1){
                    left=0;
                }else{
                    left>>=1;
                }
            }
        }
    }


}
