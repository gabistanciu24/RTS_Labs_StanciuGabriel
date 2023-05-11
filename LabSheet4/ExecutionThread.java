package Ex4Lab7;

import java.util.concurrent.Semaphore;

public class ExecutionThread extends Thread{
    Semaphore semaphore;
    int sleep_min, sleep_max, activity_min, activity_max;

    public ExecutionThread(String name, Semaphore semaphore, int sleep_min, int sleep_max, int activity_min, int activity_max) {
        super(name);
        this.semaphore = semaphore;
        this.sleep_min = sleep_min;
        this.sleep_max = sleep_max;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
    }

    public void run() {
        while (true) {
            System.out.println(this.getName() + " - STATE 1");
            try {
                semaphore.acquire(2);
                System.out.println("Semaphore acquired by " + this.getName());
                System.out.println(this.getName() + " - STATE 2 ");
                int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Semaphore released by " + this.getName());
            semaphore.release(2);

            System.out.println(this.getName() + " - STATE 3");
            try {
                Thread.sleep(Math.round(Math.random() * (sleep_max - sleep_min) + sleep_min) * 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " - STATE 4");
        }
    }
}

class Main {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);

        new ExecutionThread("Thread 0", semaphore, 0, 3, 4, 7).start();
        new ExecutionThread("Thread 1", semaphore, 0, 4, 6, 9).start();
        new ExecutionThread("Thread 2", semaphore, 0, 5, 8, 11).start();

    }
}

