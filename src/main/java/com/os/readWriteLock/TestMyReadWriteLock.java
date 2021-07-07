package com.os.readWriteLock;

import java.util.concurrent.locks.Lock;

/**
 * @Author ws
 * @Date 2021/7/7 14:39
 */
public class TestMyReadWriteLock {
    public static void main(String[] args){
        MyReadWriteLock lock = new MyReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();
        Thread[] readThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readThreads[i] = new Thread(() -> {
                readLock.lock();
                System.out.println("reader:\t" + Thread.currentThread().getId() + "start");
                System.out.println("reader:\t" + Thread.currentThread().getId() + "end");
                readLock.unlock();
            });
        }

        Thread[] writeThreads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            writeThreads[i] = new Thread(() -> {
                writeLock.lock();
                System.out.println("writer:\t" + Thread.currentThread().getId() + "start");
                System.out.println("writer:\t" + Thread.currentThread().getId() + "end");
                writeLock.unlock();
            });
        }

        for (int i = 0; i < readThreads.length; i++) {
            readThreads[i].start();
        }

        for (int i = 0; i < writeThreads.length; i++) {
            writeThreads[i].start();
        }
    }
}
