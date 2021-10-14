//Attempted implementation of Alley class with multiple cars
//Mandatory assignment 2
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Sep 29, 2021

public class BatonAlley extends Alley {

    int up, down, delayedUp, delayedDown;
    Semaphore upSem, downSem, enterSem;

    protected BatonAlley() {
        up = 0;   down = 0;     delayedDown = 0;    delayedUp = 0;
        upSem   = new Semaphore(0);
        downSem = new Semaphore(0);
        enterSem = new Semaphore(1);
    }

    /* Block until car no. may enter alley */
    public void enter(int no) throws InterruptedException {
        enterSem.P();
        if (no < 5) {
            if (down > 0) {
                delayedUp++;
                enterSem.V();
                upSem.P();
            }
            up++;
            if (delayedUp > 0) {
                delayedUp--;
                upSem.V();
            }
            else {
                enterSem.V();
            }
        } else {
            if (up > 0) {
                delayedDown++;
                enterSem.V();
                downSem.P();
            }
            down++;
            if (delayedDown > 0) {
                delayedDown--;
                downSem.V();
            }
            else {
                enterSem.V();
            }
        }
    }

    /* Register that car no. has left the alley */
    public void leave(int no) throws InterruptedException {
        enterSem.P();
        if (no < 5) {
            up--;
            if (up == 0 && delayedDown > 0) {
                delayedDown--;
                downSem.V();
            }
            else {
                enterSem.V();
            }
        } else {
            down--;
            if (down == 0 && delayedUp > 0) {
                delayedUp--;
                upSem.V();
            }
            else {
                enterSem.V();
            }
        }
    }

}
