//Attempted implementation of Alley class with multiple cars
//Mandatory assignment 2
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Sep 29, 2021

public class SafeAlley extends Alley {

    int up, down;
    Semaphore upSem, downSem, enterSem;

    protected SafeAlley() {
        up = 0;   down = 0;
        upSem   = new Semaphore(1);
        downSem = new Semaphore(1);
        enterSem = new Semaphore(1);
    }

    /* Block until car no. may enter alley */
    public void enter(int no) throws InterruptedException {
        enterSem.P();
        if (no < 5) {
            downSem.P();
            if (down == 0) upSem.P();    // block for up-going cars
            down++;
            downSem.V();
        } else {
            upSem.P();
            if (up == 0) downSem.P();    // block for down-going cars
            up++;
            upSem.V();
        }
        enterSem.V();
    }

    /* Register that car no. has left the alley */
    public void leave(int no) throws InterruptedException {
        if (no < 5) {
            downSem.P();
            down--;
            if (down == 0) upSem.V();    // enable up-going cars again
            downSem.V();
        } else {
            upSem.P();
            up--;
            if (up == 0) downSem.V();    // enable down-going cars again
            upSem.V();
        }
    }

}
