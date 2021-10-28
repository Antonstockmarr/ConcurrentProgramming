//Implementation of Alley class with inner alley (skeleton)
//CP Lab 3
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Oct 25, 2021

public class DoubleAlley extends Alley {

    int up = 0;
    int down = 0;
    int innerUp = 0;

    DoubleAlley() {
    }

    @Override
    /* Block until car no. may enter alley */
    public synchronized void enter(int no) throws InterruptedException {
        if (no <= 2) {
            while (innerUp > 0) wait();
            down++;
        }
        else if (no <= 4) {
            while (up > 0) wait();
            down++;
        }
        else {
            while (down > 0) wait();
            up++;
            innerUp++;
        }
    }

    @Override
    /* Register that car no. has left the alley */
    public synchronized void leave(int no) {
        if (no < 5) {
            down--;
            notify();
        }
        if (no >= 5) {
            up--;
            notify();
        }
    }
    
    @Override
    /* Register that car no. has left the inner alley */
    public synchronized void leaveInner(int no) {
        if (no >= 5) {
            innerUp--;
            notifyAll();
        }
    }

}
