package com.micer.core.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentRingList<T> {

    public ConcurrentRingList() {}

    private final Lock lock = new ReentrantLock();

    private int index = 0;

    private LinkedList list = new LinkedList();

    public int size() {
        lock.lock();
        try {
            return list.size();
        } finally {
            lock.unlock();
        }
    }

    public void add(T item) {
        lock.lock();
        try {
            list.add(item);

            lock.unlock(); } finally { lock.unlock();
        }
    }

    public void addAll(List<T> item) {
        lock.lock();
        try {
            list.addAll(item);

            lock.unlock(); } finally { lock.unlock();
        }
    }

    public T next()
    {
        lock.lock();
        try {
            if (list.size() == 0)
            {
                return null;
            }
            int temp = index;
            index = ((index + 1) % list.size());
            return (T) list.get(temp);
        } finally
        {
            lock.unlock();
        }
    }
}
