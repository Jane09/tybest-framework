package com.tybest.base.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tb
 * @date 2019/1/3 17:47
 */
public class LockAll {

    public static void main(String[] args) {

    }


    /**
     * pessimistic  悲观锁 synchronized，Lock   多写
     * optimistic   乐观锁 CAS 自旋实现    多读
     */

    class PessAndOptimisitic {
        /**
         * 悲观锁
         */
        private  synchronized void pessLock1(){

        }

        private ReentrantLock lock = new ReentrantLock();

        private void pessLock2() {
            lock.lock();
            try{
                System.out.println("do some business");
            }finally {
                lock.unlock();
            }
        }

        /**
         * 乐观锁 CAS (compare and swap)
         * 问题：
         * ABA问题：CAS需要在操作值的时候检查内存值是否发生变化，没有发生变化才会更新内存值。但是如果内存值原来是A，后来变成了B，然后又变成了A，那么CAS进行检查时会发现值没有发生变化，
         *          但是实际上是有变化的。ABA问题的解决思路就是在变量前面添加版本号，每次变量更新的时候都把版本号加一，这样变化过程就从“A－B－A”变成了“1A－2B－3A”
         * 循环时间长开销大：
         *      CAS操作如果长时间不成功，会导致其一直自旋，给CPU带来非常大的开销
         * 只能保证一个共享变量的原子操作：
         *      对一个共享变量执行操作时，CAS能够保证原子操作，但是对多个共享变量操作时，CAS是无法保证操作的原子性的
         *      Long应该是保证不了原子性
         *
         * 阻塞或唤醒一个Java线程需要操作系统切换CPU状态来完成，这种状态转换需要耗费处理器时间
         */
        private AtomicInteger optimisitic = new AtomicInteger(0);

        private void doOptim() {
            optimisitic.incrementAndGet();
        }

    }
}
