import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    private int[] progressValues;

    public Model(int noOfThreads) {
        this.progressValues = new int[noOfThreads];
    }

    public void setProgressValue(int id, int val) {
        this.progressValues[id] = val;
        setChanged();
        notifyObservers(new ProgressUpdate(id, val));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ProgressUpdate) {
            ProgressUpdate update = (ProgressUpdate) arg;
            int id = update.getId();
            int val = update.getVal();
            this.progressValues[id] = val;
        }
    }

    public int[] getProgressValues() {
        return progressValues;
    }
}

