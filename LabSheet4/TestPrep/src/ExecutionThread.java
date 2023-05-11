import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CyclicBarrier;
abstract class RunnableTask implements Runnable{
    CyclicBarrier cyclicBarrier;
    long timeInMilliseconds;

    public RunnableTask(CyclicBarrier cyclicBarrier,long timeInMilliseconds){
        this.cyclicBarrier=cyclicBarrier;
        this.timeInMilliseconds=timeInMilliseconds;
    }
    public void run(){
        try{
            Thread.sleep(timeInMilliseconds);
            System.out.println(Thread.currentThread().getName()+ " is waiting for"+(cyclicBarrier.getParties()-cyclicBarrier.getNumberWaiting()-1)+" other threads to reach the common barrier point");
            cyclicBarrier.await();
        }catch (InterruptedException | BrokenBarrierException e){
            e.printStackTrace();
        }
        System.out.println("As "+cyclicBarrier.getParties()+" reached the common barrier point and "+Thread.currentThread().getName()+" as released");
    }
}
public class ExecutionThread extends Thread{
    int delay, permit, activity_min, activity_max,sleep_min, sleep_max;
    Semaphore semaphore;
    Integer monitor;

    public ExecutionThread(Semaphore semaphore, int delay, int activity_min, int activity_max, int permit, int sleep_min, int sleep_max) {
        //this.monitor = monitor;
        this.semaphore = semaphore;
        this.delay = delay;
        this.activity_max = activity_max;
        this.activity_min = activity_min;
        this.permit = permit;
        this.sleep_min = sleep_min;
        this.sleep_max = sleep_max;
    }
    public void run() {
        if(this.getName().equals("thread-0")) {
            System.out.println(this.getName() + "State 0");
            System.out.println(this.getName() + "State 1");
            try {
                this.semaphore.acquire(this.permit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Acquired permit");

            try {
                Thread.sleep(this.delay * 300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + "State 2");
            int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
            for (int i = 0; i < k * 10000; i++) {
                i++; i--;
            }
            this.semaphore.release(this.permit);
            System.out.println(this.getName() + "State 3");
        }
        if(this.getName().equals("thread-1")) {
            System.out.println(this.getName() + "State 0");
            System.out.println(this.getName() + "State 1");
            try {
                this.semaphore.acquire(this.permit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + "State 2");
            int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
            for (int i = 0; i < k * 10000; i++) {
                i++; i--;
            }
            this.semaphore.release(this.permit);
            //wait sau notify sau ceva
            try{
                Thread.sleep(Math.round(Math.random()* (sleep_max - sleep_min)+sleep_min)*500);
            }catch ( Exception e){
                e.printStackTrace();
            }
            System.out.println(this.getName() + "State 3");

        }

    }
}