public class Main {
    private static final int noOfThreads = 6;
    private static final int processorLoad = 1000000;

    public static void main(String[] args) {
        Model model = new Model(noOfThreads);
        Window win = new Window(model.getProgressValues());
        for (int i = 0; i < noOfThreads; i++) {
            Fir fir = new Fir(i, i+2, model, processorLoad);
            fir.addObserver(win);
            fir.addObserver(model);
            new Thread(fir).start();
        }
    }
}