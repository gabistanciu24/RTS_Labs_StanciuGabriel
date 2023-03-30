public class JoinTestThread extends Thread{

    Thread t;
    String name;
    int number;

    JoinTestThread(String n, Thread t, int number){

        this.setName(n);
        this.name =n;
        this.t=t;
        this.number=number;
    }

    public void run() {

        System.out.println("Thread "+number+" has entered the run() method");

        try {

            if (t != null) t.join();

            System.out.println("Thread "+number+" executing operation.");
            for(int i=1;i<=number;i++){
                if(number%i==0) {
                    Main.sum += i;
                }
                
            }
            System.out.println("Sum: "+Main.sum);
            Thread.sleep(3000);

            System.out.println("Thread "+number+" has terminated operation.");

        }

        catch(Exception e){e.printStackTrace();}

    }

}
