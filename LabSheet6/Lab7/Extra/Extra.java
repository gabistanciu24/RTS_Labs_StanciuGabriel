package Lab7.Extra;

import java.util.concurrent.Semaphore;

public class Extra {

    private static final int QUEUE_SIZE = 10;
    private static final int MAX_GREEN_TIME = 10;

    private final Semaphore trafficLight;
    private final int[] queues;
    private final int[] greenTimes;
    private final Thread[] threads;

    public Extra() {
        trafficLight = new Semaphore(QUEUE_SIZE);
        queues = new int[4];
        greenTimes = new int[4];
        threads = new Thread[5];

        for (int i = 0; i < 4; i++) {
            queues[i] = 0;
            greenTimes[i] = 0;
            final int queueIndex = i;
            threads[i] = new Thread(() -> generateCars(queueIndex));
        }
        threads[4] = new Thread(() -> controlTraffic());
    }

    public void start() {
        for (int i = 0; i < 5; i++) {
            threads[i].start();
        }
    }

    private void generateCars(int queueIndex) {
        while (true) {
            try {
                Thread.sleep(1000);
                int generatedCars = (int) (Math.random() * QUEUE_SIZE);
                synchronized (queues) {
                    queues[queueIndex] += generatedCars;
                    System.out.println("Generated " + generatedCars + " cars in queue " + queueIndex);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void controlTraffic() {
        while (true) {
            try {
                Thread.sleep(1000);
                int maxGreenTime = 0;
                int maxQueueIndex = -1;
                synchronized (queues) {
                    for (int i = 0; i < 4; i++) {
                        if (queues[i] >= 2) {
                            int greenTime = Math.min(queues[i], MAX_GREEN_TIME);
                            if (greenTime > maxGreenTime) {
                                maxGreenTime = greenTime;
                                maxQueueIndex = i;
                            }
                        }
                    }

                    if (maxQueueIndex >= 0) {
                        int permits = Math.min(maxGreenTime, trafficLight.availablePermits());
                        if (permits > 0) {
                            trafficLight.acquire(permits);
                            for (int i = 0; i < 4; i++) {
                                if (i != maxQueueIndex && queues[i] > 0) {
                                    System.out.println("Waiting queue " + i + " blocked");
                                }
                                queues[i] = Math.max(queues[i] - permits, 0);
                            }
                            greenTimes[maxQueueIndex] = maxGreenTime;
                            System.out.println("Traffic light turned green for queue " + maxQueueIndex
                                    + " for " + maxGreenTime + " seconds");
                        }
                    }
                }
                synchronized (greenTimes) {
                    for (int i = 0; i < 4; i++) {
                        if (greenTimes[i] > 0) {
                            greenTimes[i]--;
                            if (greenTimes[i] == 0) {
                                trafficLight.release(10);
                                System.out.println("Traffic light turned red for queue " + i);
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
