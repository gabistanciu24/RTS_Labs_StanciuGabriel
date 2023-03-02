import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Window extends JFrame implements Observer {
    private Model model;
    private int noOfThreads;
    private JProgressBar[] bars;

    public Window(int noOfThreads) {
        setLayout(null);
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.noOfThreads = noOfThreads;
        init();
        this.setVisible(true);
    }

    private void init() {
        bars = new JProgressBar[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            JProgressBar pb = new JProgressBar();
            pb.setMaximum(1000);
            pb.setBounds(50, (i + 1) * 30, 350, 20);
            this.add(pb);
            bars[i] = pb;
        }
    }

    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        int id = (int) arg;
        int progress = model.getProgress(id);
        bars[id].setValue(progress);
    }
}
