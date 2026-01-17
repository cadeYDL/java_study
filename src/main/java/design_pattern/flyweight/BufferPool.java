package design_pattern.flyweight;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class BufferPool {
    private final int total;
    private int free;
    private final Lock freeLock = new ReentrantLock();
    private final Map<Integer,Deque<ByteBuffer>> slots = new HashMap<>();
    private final int maxSlot;

    private final Deque<Condition> waiters = new ArrayDeque<>();
    private final Map<Integer,Lock> slotsLock = new HashMap<>();
    private final Map<Integer,Deque<Condition>> slotWaiter = new HashMap<>();

    public BufferPool(int total,int maxSlot){
        this.total = total;
        this.free = total;
        this.maxSlot = get2Size(maxSlot);
        for (int i =1;i<=maxSlot;i<<=1){
            slots.put(i,new ArrayDeque<>());
            slotsLock.put(i,new ReentrantLock());
            slotWaiter.put(i, new ArrayDeque<>());
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
            return getNewBuffer(size,waiteTimeMS);
        }
        size = get2Size(size);
        Lock lock = slotsLock.get(size);
        Deque<ByteBuffer> slot = slots.get(size);
        Deque<Condition> slotWaters = slotWaiter.get(size);
        Condition cond;
        try{
            lock.lock();
            if (!slot.isEmpty()){
                return slot.pop();
            }
            cond = lock.newCondition();
        }finally {
            lock.unlock();
        }
        try {
            freeLock.lock();
            freeUp(size);
            if (size <= free) {
                free -= size;
                return ByteBuffer.allocate(size);
            }
        } finally {
            freeLock.unlock();
        }
        try{
            lock.lock();
            slotWaters.addLast(cond);
        }finally {
            lock.unlock();
        }
        long start = System.currentTimeMillis();
        long remainTime = waiteTimeMS;
        for(;;) {
            try{
                lock.lock();
                if (!slot.isEmpty()){
                    slotWaters.remove(cond);
                    return slot.pop();
                }
            }finally {
                lock.unlock();
            }
            try {
                freeLock.lock();
                freeUp(size);
                if (size <= free) {
                    free -= size;
                    slotWaters.remove(cond);
                    return ByteBuffer.allocate(size);
                }
            } finally {
                freeLock.unlock();
            }
            try{
                lock.lock();
                boolean wakeUp = cond.await(remainTime, TimeUnit.MILLISECONDS);
                if(!wakeUp){
                    slotWaters.remove(cond);
                    throw new TimeoutException("超时！"+waiteTimeMS);
                }
                remainTime = remainTime + start - System.currentTimeMillis();
            }finally {
                lock.unlock();
            }
        }
    }

    private void freeUp(int size) {
        if (size<=maxSlot){
            return;
        }
        try{
            freeLock.lock();
            HashSet<Integer> vistMap = new HashSet<>();
            while (free<size){
                int needSize = get2Size(size-free);
                int i = maxSlot;
                for(;(i>needSize||vistMap.contains(i))&&i>1;i>>=1){}
                int needCount = needSize/i;
                Lock lock = slotsLock.get(i);
                try{
                    lock.lock();
                    vistMap.add(i);
                    for(int j=0;j<needCount&&!slots.get(i).isEmpty();j++){
                        slots.get(i).pop();
                        free += i;
                    }
                }finally {
                    lock.unlock();
                }
            }
        }finally {
            freeLock.unlock();
        }

    }

    private ByteBuffer getNewBuffer(int size, long waiteTimeMS) throws Exception {
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
        try {
            while (true) {
                long start = System.currentTimeMillis();
                freeLock.lock();
                try {
                    boolean wakeup = cond.await(remainTime, TimeUnit.MILLISECONDS);
                    if (!wakeup) {
                        throw new TimeoutException("超时了");
                    }
                    freeUp(size);
                    if (size <= free) {
                        free -= size;
                        return ByteBuffer.allocate(size);
                    }
                } finally {
                    freeLock.unlock();
                }
                remainTime = remainTime + start - System.currentTimeMillis();
                if (remainTime <= 0) {
                    throw new TimeoutException("超时了");
                }
            }
        } finally {
            freeLock.lock();
            try {
                waiters.remove(cond);
            } finally {
                freeLock.unlock();
            }
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


        Lock lock = slotsLock.get(buffer.capacity());

        try {
            lock.lock();
            buffer.clear();
            slots.get(buffer.capacity()).addLast(buffer);
            Deque<Condition> slotWaiters = slotWaiter.get(buffer.capacity());
            if(!slotWaiters.isEmpty()){
                slotWaiters.peek().signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
