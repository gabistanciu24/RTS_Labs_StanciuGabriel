import java.util.Observable;

public class Fir extends Observable implements Runnable {

    private int id;
    private int priority;
    private Model model;
    private int processorLoad;

    public Fir(int id, int priority, Model model, int procLoad) {
        this.id = id;
        this.model = model;
        this.processorLoad = procLoad;
        this.setPriority(priority);
    }

    @Override
    public void run() {
        int c = 0;
        while (c < 1000) {
            for (int j = 0; j < this.processorLoad; j++) {
                j++;
                j--;
            }
            c++;
            setChanged();
            notifyObservers(c);
        }
    }
}
