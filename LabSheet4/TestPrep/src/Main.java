import javax.management.monitor.Monitor;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        //Integer monitor = new Integer(1);
        new ExecutionThread(semaphore,0,3,7,1,0,0).start();
        new ExecutionThread(semaphore,0,2,4,1,2,5).start();
    }
}