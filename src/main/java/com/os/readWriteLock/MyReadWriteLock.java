package com.os.readWriteLock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * @Author ws
 * @Date 2021/7/7 14:12
 */
// 使用信号量阻塞方式实现读写锁
public class MyReadWriteLock implements ReadWriteLock {
    private final MyReadWriteLock.ReadLock readerLock;
    private final MyReadWriteLock.WriteLock writerLock;

    private Semaphore readWrite = new Semaphore(1);
    private Semaphore read = new Semaphore(1);

    private volatile int readCount = 0;

    public MyReadWriteLock() {
        this.readerLock = new ReadLock();
        this.writerLock = new WriteLock();
    }


    @Override
    public Lock readLock() {
        return this.readerLock;
    }

    @Override
    public Lock writeLock() {
        return this.writerLock;
    }

    public class ReadLock implements Lock {
        @Override
        public void lock() {
            try {
                read.acquire();
                readCount++;
                // 读写互斥,读读不互斥
                if (readCount == 1) {
                    readWrite.acquire();
                }
                read.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void unlock() {
            try {
                read.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readCount--;
            // 没有读者,让写者运行
            if (readCount == 0) {
                readWrite.release();
            }
            read.release();
        }

        @Override
        public Condition newCondition() {
            return null;
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

    }

    public class WriteLock implements Lock {
        @Override
        public void lock() {
            try {
                readWrite.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void unlock() {
            readWrite.release();
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
