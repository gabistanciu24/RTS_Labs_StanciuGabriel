package Ex3;
import java.util.concurrent.CountDownLatch;

class SecondaryThreads extends Thread {

    int delay, k, permit, actMax, actMin;
    Integer monitor;
    CountDownLatch countDownLatch;
    String name;

    public SecondaryThreads(String name, Integer monitor, CountDownLatch countDownLatch, int delay, int actMax, int actMin) {
        this.setName(name);
        this.delay = delay;
        this.countDownLatch = countDownLatch;
        this.actMin = actMin;
        this.actMax = actMax;
        this.monitor = monitor;
    }

    public void run() {

        System.out.println(this.getName() + " - STATE 1");

        try {  //the time transitions sleep first before they are executed where in this case acquiring the monitor
            Thread.sleep(this.delay * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(this.getName() + " - STATE 2");
        countDownLatch.countDown();
        int k = (int) Math.round(Math.random() * (actMax - actMin) + actMin);
        for (int i = 0; i < k * 100000; i++) {
            i++;
            i--;
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

class MainThr extends Thread {
    int delay, k, permit, actMax, actMin;
    Integer monitor;
    Integer monitor2; //we should have two monitor objects one for p10 and another for p6
    CountDownLatch countDownLatch;
    String name;

    public MainThr(String name, Integer monitor, Integer monitor2, CountDownLatch countDownLatch, int delay, int actMax, int actMin) {
        this.setName(name);
        this.delay = delay;
        this.countDownLatch = countDownLatch;
        this.actMin = actMin;
        this.actMax = actMax;
        this.monitor = monitor;
        this.monitor2 = monitor2;

    }

    public void run() {

        System.out.println(this.getName() + " - STATE 1");//state1
        countDownLatch.countDown();
        try {
            Thread.sleep(this.delay * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " - STATE 2");
        int k = (int) Math.round(Math.random() * (actMax - actMin) + actMin);
        for (int i = 0; i < k * 100000; i++) {
            i++;
            i--;
        }

        synchronized (monitor){
            synchronized (monitor2){
                monitor.notify();
                monitor2.notify();
            }
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

public class Main {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Integer monitor = 1;
        Integer monitor2 = 1;

        MainThr thread1 = new MainThr("Thread1", monitor, monitor2, countDownLatch, 7, 2, 3 );
        SecondaryThreads thread2 = new SecondaryThreads("Thread2", monitor, countDownLatch,4, 3, 5);
        SecondaryThreads thread3 = new SecondaryThreads("Thread3", monitor2, countDownLatch,5, 4, 6);

        thread1.start();
        thread2.start();
        thread3.start();
        }
    }

