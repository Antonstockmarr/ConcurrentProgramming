//Prototype implementation of Field class
//Mandatory assignment 2
//Course 02158 Concurrent Programming, DTU, Fall 2021

//Hans Henrik Lovengreen     Sep 29, 2021

import java.util.HashMap;
import java.util.Map;

// Solution to Problem 2: Avoid Bumping
public class Field {

    private final Map<Pos, Semaphore> positionStatus;

    public Field() {
        positionStatus = new HashMap<>();
        for (int i=0; i <= Layout.COLS; i++){
            for (int j=0; j<= Layout.ROWS; j++) {
                Pos pos = new Pos(i,j);
                Semaphore semaphore = new Semaphore(1);
                positionStatus.put(pos, semaphore);
            }
        }
    }

    /* Block until car no. may safely enter tile at pos */
    public void enter(int no, Pos pos) throws InterruptedException {
        Semaphore s = positionStatus.get(pos);
        s.P();
    }

    /* Release tile at position pos */
    public void leave(Pos pos) {
        Semaphore s = positionStatus.get(pos);
        s.V();
    }

}
