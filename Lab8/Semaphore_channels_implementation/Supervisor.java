package Lab8Session6.Semaphore_channels_implementation;

public class Supervisor extends Thread {
    // Inputs
    P_user ps_i2; // Output of controller (user display)

    // Outputs
    P ps_o1; // Output of supervisor (user demand)
    P ps_o2; // Output of supervisor (user display)

    // Unknowns
    P ps_1; // ???
    P ps_2; // Current position
    P ps_3; // Next move delay

    // Constructor
    public Supervisor(P_user ps_i2) {
        this.ps_i2 = ps_i2;

        ps_o1 = new P(); this.ps_o1.x = 0;
        ps_o2 = new P(); this.ps_o2.x = 0;
        
        ps_1 = new P(); this.ps_1.x = 1;
        ps_2 = new P(); this.ps_2.x = 1;
        ps_3 = new P(); this.ps_3.x = 1;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (ps_i2.tryAcquire()) {
                    ps_2.x++;
                    ps_o2.x = 1;

                    if (ps_2.x == ps_1.x && ps_2.x == ps_3.x) {
                        ps_o1.x = 1;
                    }

                    ps_i2.release();
                }
            }
        } catch (Exception e) {
        }
    }
}
