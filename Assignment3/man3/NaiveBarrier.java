//Naive implementation of Barrier class
//Mandatory assignment 3
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Oct 28, 2021

class NaiveBarrier extends Barrier {
    
    int arrived = 0;
    int threshold = 9;
    boolean active = false;
   
    public NaiveBarrier(CarDisplayI cd) {
        super(cd);
    }

    @Override
    public void sync(int no) throws InterruptedException {

        synchronized(this) {

        if (!active) return;

        arrived++;

            if (arrived < threshold) {
                wait();
            } else {
                arrived = 0;
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
