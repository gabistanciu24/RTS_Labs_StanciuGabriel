package Ex4Lab4;

import java.util.concurrent.locks.ReentrantLock;

public class Petri44 extends Thread{
    ReentrantLock lock1, lock2;

    int activity1_min, activity1_max, activity_min, activity_max, sleep;

    public Petri44(ReentrantLock lock1, ReentrantLock lock2, int activity1_min, int activity1_max, int activity_min, int activity_max, int sleep) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.activity1_min = activity1_min;
        this.activity1_max = activity1_max;
        this.activity_min = activity_min;
        this.activity_max = activity_max;
        this.sleep = sleep;
    }

    public void run() {
        System.out.println(this.getName() + " - STATE 1");
        for (int i = 0; i < (int) Math.round(Math.random() * (activity1_max - activity1_min) + activity1_min) * 100000; i++) {
            i++;
            i--;
        }
        if (this.lock1.tryLock())
            try {
                System.out.println(this.getName() + " - STATE 2");
                int k = (int) Math.round(Math.random() * (activity_max - activity_min) + activity_min);
                for (int i = 0; i < k * 100000; i++) {
                    i++;
                    i--;
                }
                if (this.lock2.tryLock())

                    try {
                        System.out.println(this.getName() + " - STATE 3");
                        Thread.sleep(sleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        this.lock2.unlock();
                    }

          } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
           // finally{
                this.lock1.unlock();
         //   }

        System.out.println(this.getName() + " - STATE 4");
    }
}
class Main {
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new  ReentrantLock();
        new Petri44(lock1, lock2, 2, 4, 4, 6, 4).start();
        new Petri44(lock2, lock1, 3, 5, 5, 7, 5).start();
    }
}
