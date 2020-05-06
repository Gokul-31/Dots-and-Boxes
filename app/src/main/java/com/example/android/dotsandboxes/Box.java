package com.example.android.dotsandboxes;

public class Box {
    public boolean[] sides=new boolean[4];
    private boolean done;
    private int win;  //1 for 1st player 2 for 2nd

    public Box() {
        for (int i=0;i<4;i++){
            sides[i]=false;
        }
        done=false;
        win=0;
    }

    public boolean[] getSides() {
        return sides;
    }

    public boolean isDone() {
        return done;
    }

    public int getWin() {
        return win;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setWin(int win) {
        this.win = win;
    }
}
