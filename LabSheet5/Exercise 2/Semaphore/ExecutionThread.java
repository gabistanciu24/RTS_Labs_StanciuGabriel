package Ex2.Semaphore;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class ExecutionThread extends Thread {
    Semaphore semaphore;
    String name;
    int sleep, activity_min, activity_max;
    CyclicBarrier cyclicBarrier;

    public ExecutionThread(String name, Semaphore semaphore, CyclicBarrier cyclicBarrier, int sleep, int activity_min, int activity_max) {
        this.setName(name);
        this.semaphore = semaphore;
        this.cyclicBarrier = cyclicBarrier;
        this.sleep = sleep;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
        while(true) {
            System.out.println(this.getName() + " - STATE 1");

            try {
                System.out.println(this.getName() + " trying to acquire the semaphore");
                semaphore.acquire();
                System.out.println(this.getName() + " acquired the semaphore");

                System.out.println(this.getName() + " STATE 2");
                int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(Math.round(Math.random() * sleep + sleep) * 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " released the semaphore");
            semaphore.release();
            System.out.println(this.getName() + " - STATE 3");

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}

class ExecutionThreadMiddle extends Thread {
    Semaphore semaphore1, semaphore2;
    String name;
    int delay, activity_min, activity_max;
    CyclicBarrier cyclicBarrier;

    public ExecutionThreadMiddle(String name, Semaphore semaphore1, Semaphore semaphore2, CyclicBarrier cyclicBarrier, int delay, int activity_min, int activity_max) {
        this.setName(name);
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
        this.cyclicBarrier = cyclicBarrier;
        this.delay = delay;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
            System.out.println(this.getName() + " - STATE 1");

            try {
                System.out.println(this.getName() + " trying to acquire the first semaphore");
                semaphore1.acquire();
                System.out.println(this.getName() + " acquired the first semaphore");

                System.out.println(this.getName() + " trying to acquire the second semaphore");
                semaphore2.acquire();
                System.out.println(this.getName() + " acquired the second semaphore");

                System.out.println(this.getName() + " STATE 2");
                int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }
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

            System.out.println(this.getName() + " - STATE 3");

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

    }

}


class Main {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);

       while(true){
           CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {
               public void run() {
                   //this will be printed by a new thread before all the other threads are unlocked (they reached the await() method)
                   System.out.println("Barrier Routine");
               }
           });

           ExecutionThread thread1 = new ExecutionThread("Thread1", semaphore1, barrier, 4, 2, 4);
           ExecutionThread thread2 = new ExecutionThread("Thread2", semaphore2, barrier,5, 2, 5);
           ExecutionThreadMiddle thread3 = new ExecutionThreadMiddle("Thread3", semaphore1, semaphore2, barrier,3, 3,6);

           thread1.start();
           thread2.start();
           thread3.start();
           barrier.reset();
           barrier.await();
       }
    }
}

