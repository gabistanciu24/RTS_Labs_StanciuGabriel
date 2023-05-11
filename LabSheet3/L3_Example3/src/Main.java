
public class Main {

    static int sum = 0;

    public static void main(String[] args) {

//        JoinTestThread w1 = new JoinTestThread("Thread 1", null,50000);
//
//        JoinTestThread w2 = new JoinTestThread("Thread 2", w1, 20000);
//
//        w1.start();
//
//        w2.start();

        for(int i=0;i<3;i++){
            switch(i)
            {
                case 0: break;
                case 1: System.out.print("one ");
                case 2: System.out.print("two ");
                case 3: System.out.print("three ");
            }
        }
        System.out.println("done");

    }

}
