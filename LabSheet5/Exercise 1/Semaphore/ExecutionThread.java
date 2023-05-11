package Ex1.Semaphore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class ExecutionThread extends Thread {
    Semaphore semaphore1, semaphore2;
    String name;
    int delay, activity_min1, activity_max1, activity_min2, activity_max2;
    CountDownLatch countDownLatch;

    public ExecutionThread(String name, Semaphore semaphore1, Semaphore semaphore2, CountDownLatch countDownLatch, int delay, int activity_min1, int activity_max1, int activity_min2, int activity_max2)
    {
        this.setName(name);
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
        this.countDownLatch = countDownLatch;
        this.delay = delay;
        this.activity_min1 = activity_min1;
        this.activity_max1 = activity_max1;
        this.activity_min2 = activity_min2;
        this.activity_max2 = activity_max2;
    }

    public void run() {

            System.out.println(this.getName() + " - STATE 1");
            int k = (int) Math.round(Math.random() * (activity_max1 - activity_min1) + activity_min1);
            for (int i = 0; i < k * 100000; i++) {
                i++;
                i--;
            }
            try {
                System.out.println(this.getName() + " trying to acquire the first semaphore");
                semaphore1.tryAcquire();
                System.out.println(this.getName() + " acquired the first semaphore");
                System.out.println(this.getName() + " STATE 2");
                k = (int) Math.round(Math.random() * (activity_max2 - activity_min2) + activity_min2);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }

                System.out.println(this.getName() + " trying to acquire the first semaphore");
                semaphore2.tryAcquire();
                System.out.println(this.getName() + " acquired the first semaphore");

                System.out.println(this.getName() + " - STATE 3");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(Math.round(Math.random() * delay + delay) * 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " released the first semaphore");
            semaphore1.release();

            System.out.println(this.getName() + " released the second semaphore");
            semaphore2.release();

            System.out.println(this.getName() + " - STATE 4");

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
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);
        while(true) {
            CountDownLatch countDownLatch = new CountDownLatch(3);

            ExecutionThread thread1 = new ExecutionThread("Thread1", semaphore1, semaphore2, countDownLatch, 4, 2, 4, 4, 6);
            ExecutionThread thread2 = new ExecutionThread("Thread2", semaphore2, semaphore1, countDownLatch, 5, 3, 5, 5, 7);

            thread1.start();
            thread2.start();
            countDownLatch.countDown();
            countDownLatch.await();
        }
    }
}



