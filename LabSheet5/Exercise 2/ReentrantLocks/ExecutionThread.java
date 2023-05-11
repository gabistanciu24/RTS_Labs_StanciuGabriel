package Ex2.ReentrantLocks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutionThread extends Thread {
    Lock lock;
    String name;
    int sleep, activity_min, activity_max;
    CountDownLatch countDownLatch;

    public ExecutionThread(String name, Lock lock, CountDownLatch countDownLatch, int sleep, int activity_min, int activity_max) {
        this.setName(name);
        this.lock = lock;
        this.countDownLatch = countDownLatch;
        this.sleep = sleep;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
            System.out.println(this.getName() + " - STATE 1");
            try {
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    try {
                        System.out.println(this.getName() + " acquired the lock");
                        System.out.println(this.getName() + " - STATE 2");
                        int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                        System.out.println(this.getName() + " k= " + k);
                        for (int i = 0; i < k * 100000; i++) {
                            i++;
                            i--;
                        }

                        try {
                            Thread.sleep(Math.round(Math.random() * this.sleep * 500));
                            System.out.println(this.getName() + " has slept for " + this.sleep * 500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } finally {
                        System.out.println(this.getName() + " released the lock");
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(this.getName() + " - STATE 3");

            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

}
class ExecutionThreadMiddle extends Thread {
    Lock lock1, lock2;
    String name;
    int delay, activity_min, activity_max;
    CountDownLatch countDownLatch;

    public ExecutionThreadMiddle(String name, Lock lock1, Lock lock2, CountDownLatch countDownLatch, int delay, int activity_min, int activity_max) {
        this.setName(name);
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.countDownLatch = countDownLatch;
        this.delay = delay;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
            System.out.println(this.getName() + " - STATE 1");

            try {
                if (lock1.tryLock(10, TimeUnit.SECONDS)) {
                    try {
                        System.out.println(this.getName() + " acquired its first lock");
                        if (lock2.tryLock(5, TimeUnit.SECONDS)) {
                            try {
                                System.out.println(this.getName() + " acquired its second lock");
                                int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                                System.out.println(this.getName() + " k= " + k);
                                for (int i = 0; i < k * 100000; i++) {
                                    i++;
                                    i--;
                                }
                                System.out.println(this.getName() + " - STATE 2");

                                try {
                                    Thread.sleep(Math.round(Math.random() * this.delay * 500));
                                    System.out.println(this.getName() + " has slept for " + this.delay * 500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(this.getName() + " - STATE 3");

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

            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}

class Main {
    public static void main(String[] args) throws InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        while (true) {

            CountDownLatch countDownLatch = new CountDownLatch(4);

            ExecutionThread thread1 = new ExecutionThread("Thread1", lock1, countDownLatch, 4, 2, 4);
            ExecutionThread thread2 = new ExecutionThread("Thread2", lock2, countDownLatch, 5, 2, 5);
            ExecutionThreadMiddle thread3 = new ExecutionThreadMiddle("Thread3", lock1, lock2, countDownLatch, 3, 3, 6);

            thread1.start();
            thread2.start();
            thread3.start();

            countDownLatch.countDown();
            countDownLatch.await();
        }
    }
}

