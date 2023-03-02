public class Controller {
    private static final int noOfThreads = 6;
    private static final int processorLoad = 1000000;

    public static void main(String args[]) {
        Model model = new Model(noOfThreads);
        Window view = new Window(noOfThreads);
        view.setModel(model);

        for (int i = 0; i < noOfThreads; i++) {
            new Thread(new Fir(i, i+2, model, processorLoad)).start();
        }
    }
}
