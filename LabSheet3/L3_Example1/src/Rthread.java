public class Rthread extends Thread{

    FileService service;

    public Rthread(FileService service) {

        this.service = service;

    }

    public void run(){

        while (!Main.isStopThreads()){

            try {

                String readMsg = service.read();

                System.out.println(readMsg);

                Thread.sleep(3000);
                Thread.wait(30);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

}