package com.example.android.dotsandboxes;

public class Box {
    private boolean[] sides=new boolean[4];
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
        done=true;
        for(int i=0;i<4;i++){
            if(!sides[i])
                done=false;
        }
        return done;
    }

    public int getWin() {

        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }
    public void setSide1(boolean side1){
        sides[0]=side1;
    }
    public void setSide2(boolean side2){
        sides[1]=side2;
    }
    public void setSide3(boolean side3){
        sides[2]=side3;
    }
    public void setSide4(boolean side4){
        sides[3]=side4;
    }
}
