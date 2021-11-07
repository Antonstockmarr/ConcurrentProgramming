//Implementation of a basic Barrier class (skeleton)
//Mandatory assignment 3
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Oct 28, 2021

class SafeBarrier extends Barrier {

    int arrived = 0;
    int waiting = 0;
    int threshold = 9;
    boolean open = false;
    boolean active = false;

    public SafeBarrier(CarDisplayI cd) {
        super(cd);
    }

    @Override
    public void sync(int no) throws InterruptedException {

        synchronized(this) {

            if (!active) return;

            arrived++;

            if (arrived < threshold) {
                while (open && active) wait();

                waiting++;
                while (!open && active) wait();
                waiting--;
                if (waiting == 0) {
                    open = false;
                    notifyAll();
                }
            } else {
                arrived = 0;
                open = true;
                notifyAll();
            }

        }
    }

    @Override
    public void on() {
        active = true;
    }

    @Override
    public void off() {
        synchronized(this) {
            active = false;
            arrived = 0;
            notifyAll();
        }
    }


    @Override
    // May be (ab)used for robustness testing
    public void set(int k) {
        threshold = k;
        synchronized (this) {
            notify();
        }
    }


}
