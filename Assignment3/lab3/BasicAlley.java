//Basic implementation of Alley class (skeleton)
//CP Lab 3
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Oct 25, 2021

public class BasicAlley extends Alley {

    int up = 0;
    int down = 0;

    BasicAlley() {
    }

    @Override
    /* Block until car no. may enter alley */
    public synchronized void enter(int no) throws InterruptedException {
        if (no < 5) {
            while (up > 0) wait();
            down++;
        }
        if (no >= 5) {
            while (down > 0) wait();
            up++;
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
    
 
}
