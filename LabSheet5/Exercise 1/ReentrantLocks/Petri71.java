package Ex1.ReentrantLocks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Petri71 extends Thread{
    Lock lock1, lock2;
    String name;
    CyclicBarrier cyclicBarrier;
    int delay, activity_min1, activity_max1, activity_min2, activity_max2;

    public Petri71(String name, CyclicBarrier cyclicBarrier, Lock lock1, Lock lock2, int delay, int activity_min1, int activity_max1, int activity_min2, int activity_max2) {
        this.setName(name);
        this.cyclicBarrier = cyclicBarrier;
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.delay = delay;
        this.activity_min1 = activity_min1;
        this.activity_max1 = activity_max1;
        this.activity_min2 = activity_min2;
        this.activity_max2 = activity_max2;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");
            int k = (int) Math.round(Math.random() * (activity_max1 - activity_min1) + activity_min1);
            System.out.println(this.getName() + " k= " + k);
            for (int i = 0; i < k * 100000; i++) {
                i++;
                i--;
            }
            try {
                if (lock1.tryLock(5, TimeUnit.SECONDS)) {
                    try {

                        System.out.println(this.getName() + " - STATE 2");
                        //System.out.println(this.getName() + " acquired its first lock");

                        k = (int) Math.round(Math.random() * (activity_max2 - activity_min2) + activity_min2);
                        System.out.println(this.getName() + " k= " + k);
                        for (int i = 0; i < k * 100000; i++) {
                            i++;
                            i--;
                        }

                        if (lock2.tryLock(5, TimeUnit.SECONDS)) {
                            try {
                                System.out.println(this.getName() + " acquired its second lock");
                                System.out.println(this.getName() + " - STATE 3");

                                try {
                                    Thread.sleep(Math.round(Math.random() * this.delay * 500));
                                    System.out.println(this.getName() + " has slept for " + this.delay * 500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            } finally {
                                System.out.println(this.getName() + " released second lock");
                                lock2.unlock();
                            }
                        }
                    } finally {
                        System.out.println(this.getName() + " released first lock");
                        lock1.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(this.getName() + " - STATE 4");

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

    }
}

class Main{
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        while (true) {
            CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
                public void run() {
                    //this will be printed by a new thread before all the other threads are unlocked (they reached the await() method)
                    System.out.println("Barrier Routine");
                }
            });


        Petri71 thread1 = new Petri71("Thread1", barrier, lock1, lock2, 4, 2, 4, 4, 6);
        Petri71 thread2 = new Petri71("Thread2", barrier, lock2, lock1, 5, 3, 5, 5, 7);

        thread1.start();
        thread2.start();
        barrier.await();
        barrier.reset();
        }
    }
}
