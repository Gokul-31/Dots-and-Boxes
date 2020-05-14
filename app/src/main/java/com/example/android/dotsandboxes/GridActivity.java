package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

public class GridActivity extends AppCompatActivity {

    private String P1name;
    private String P2name;
    private int Size;
    private ViewGroup gridLayout;
    private Button start;
    private ImageView imageView;
    private TextView turnsView;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Paint[] mPaintLine = new Paint[2];
    private Paint[] mPaintBox= new Paint[2];
    private int viewWidth;
    private int viewHeight;
    private final int xMarginSpacing = 35;
    private int yMarginSpacing;
    private int xyGrid;
    private int lineLength;
    private int X;
    private int Y;
    private boolean flag1 = false;   //whether the canvas has been created
    private int[] xPoints;
    private int[] yPoints;
    private int xBoxCheckIndex;
    private int yBoxCheckIndex;
    private int pTurn = 1;
    private Box[][] boxes;
    private boolean checkValue;
    private boolean flagChangePlayer=true;
    private ConstraintLayout ll;
    private int totalBoxes;
    private int[] boxCount=new int[2];
    private int winner=-1;
    private TextView turnTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        final Intent gridActivity = getIntent();
        P1name = gridActivity.getStringExtra("p1");
        P2name = gridActivity.getStringExtra("p2");
        Size = gridActivity.getIntExtra("Size", 5);

        //save for the views
        boxes = new Box[Size - 1][Size - 1];
        for (int i = 0; i < Size - 1; i++)
            for (int j = 0; j < Size - 1; j++) {
                boxes[i][j] = new Box();
            }

        ll=findViewById(R.id.root);
        turnTextView=findViewById(R.id.turnText);
        gridLayout = findViewById(R.id.gridLayout);
        start = findViewById(R.id.start);
        imageView = findViewById(R.id.gridImageView);
        turnsView = findViewById(R.id.turns);
        turnsView.setText(P1name);

        totalBoxes= (int) Math.pow(Size-1,2);
        boxCount[0]=0;
        boxCount[1]=0;

        xPoints = new int[Size];
        yPoints = new int[Size];

        //clickListener for the button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLayout.removeView(start);   //works
                //setGrid
                viewWidth = imageView.getWidth();
                viewHeight = imageView.getHeight();

                //Paint setup
                mPaintLine[0] = new Paint();
                mPaintLine[1] = new Paint();
                mPaintBox[0]=new Paint();
                mPaintBox[1]=new Paint();
                mPaintLine[0].setColor(getResources().getColor(R.color.player1));
                mPaintLine[1].setColor(getResources().getColor(R.color.player2));
                mPaintBox[0].setColor(getResources().getColor(R.color.player1Box));
                mPaintBox[1].setColor(getResources().getColor(R.color.player2Box));
                mPaintLine[0].setStrokeWidth(15);
                mPaintLine[1].setStrokeWidth(15);

                mBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
                imageView.setImageBitmap(mBitmap);
                mCanvas = new Canvas(mBitmap);
                flag1 = true;

                //draw grid points
                ////ready values
                xyGrid = viewWidth - 2 * xMarginSpacing;
                lineLength = xyGrid / (Size - 1);
                yMarginSpacing = (viewHeight - xyGrid) / 2;

                //draw
                xPoints[0] = xMarginSpacing;
                yPoints[0] = yMarginSpacing;
                for (int i = 1; i < Size; i++) {
                    xPoints[i] = xPoints[i - 1] + lineLength;
                    yPoints[i] = yPoints[i - 1] + lineLength;
                }

                mCanvas.drawRect(0,0,viewWidth,yMarginSpacing-35,mPaintLine[0]);
                mCanvas.drawRect(0,yPoints[Size-1]+35,viewWidth,viewHeight,mPaintLine[0]);

                for (int i = 0; i < Size; i++) {
                    Log.i("MainActivity", "onClick: " + xPoints[i]);
                }

                for (int i = 0; i < Size; i++) {
                    for (int j = 0; j < Size; j++) {
                        mCanvas.drawCircle(xPoints[i], yPoints[j], 14, mPaint);
                    }
                }

                //drawing done
                pTurn=0;
                turnsView.setText(P1name);
                gridLayout.setBackgroundColor(getResources().getColor(R.color.player1));
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                X = (int) event.getX();
                Y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (flag1) {
                            //if it is equal to any y coordinate
                            yBoxCheckIndex = yCheck(Y);
                            if (yBoxCheckIndex != -1) {
                                xBoxCheckIndex = xFind(X);      //CHECK WHETHER INDEXES CAN BE -VE
                                if (xBoxCheckIndex != -1) {
                                    Log.i("Main", "onTouch: " + xBoxCheckIndex + "  " + yBoxCheckIndex);
                                    if (isValidIndex(xBoxCheckIndex, yBoxCheckIndex - 1))
                                        checkValue = boxes[xBoxCheckIndex][yBoxCheckIndex - 1].getSides()[2];
                                    else {
                                        checkValue = boxes[xBoxCheckIndex][yBoxCheckIndex].getSides()[0];
                                    }
                                    if (!checkValue) {
                                        //change the 3rd one of yBoxCheckIndex-1 to true if it already isnt
                                        if (isValidIndex(xBoxCheckIndex, yBoxCheckIndex - 1))
                                            boxes[xBoxCheckIndex][yBoxCheckIndex - 1].setSide3(true);
                                        //change the 1st one of yBoxCheckIndex to true          all are indexes
                                        if (isValidIndex(xBoxCheckIndex, yBoxCheckIndex))
                                            boxes[xBoxCheckIndex][yBoxCheckIndex].setSide1(true);
                                        //draw line from xBoxCheckIndex to xBoxCheckIndex+1 and Y being yBoxCheckIndex
                                        mCanvas.drawLine(xPoints[xBoxCheckIndex], yPoints[yBoxCheckIndex], xPoints[xBoxCheckIndex + 1], yPoints[yBoxCheckIndex], mPaintLine[pTurn]);
                                        //check whether the box is done and then set win
                                        flagChangePlayer=true;
                                        for (int i = yBoxCheckIndex; i >= yBoxCheckIndex - 1; i--) {
                                            if (isValidIndex(xBoxCheckIndex, i)) {
                                                if (checkNSetBox(boxes[xBoxCheckIndex][i])) {
                                                    mCanvas.drawRect(xPoints[xBoxCheckIndex] + 9, yPoints[i] + 9, xPoints[xBoxCheckIndex + 1] - 9, yPoints[i + 1] - 9, mPaintBox[pTurn]);

                                                }
                                            }
                                        }
                                        //change the playerturn int to the other value
                                        if(flagChangePlayer)
                                           changePTurn();
                                    }
                                }
                            } else {
                                xBoxCheckIndex = xCheck(X);
                                if (xBoxCheckIndex != -1) {
                                    yBoxCheckIndex = yFind(Y);
                                    if (yBoxCheckIndex != -1) {
                                        Log.i("Main", "onTouch: " + xBoxCheckIndex + "  " + yBoxCheckIndex);
                                        if (isValidIndex(xBoxCheckIndex - 1, yBoxCheckIndex))
                                            checkValue = boxes[xBoxCheckIndex - 1][yBoxCheckIndex].getSides()[1];
                                        else {
                                            checkValue = boxes[xBoxCheckIndex][yBoxCheckIndex].getSides()[3];
                                        }
                                        if (!checkValue) {
                                            //change the 2nd one of xBoxCheckIndex-1 to true
                                            if (isValidIndex(xBoxCheckIndex - 1, yBoxCheckIndex))
                                                boxes[xBoxCheckIndex - 1][yBoxCheckIndex].setSide2(true);
                                            //change the 4th one of xBoxCheckIndex to true          all are indexes
                                            if (isValidIndex(xBoxCheckIndex, yBoxCheckIndex))
                                                boxes[xBoxCheckIndex][yBoxCheckIndex].setSide4(true);
                                            //draw line from yBoxCheckIndex to yBoxCheckIndex+1 and X being xBoxCheckIndex
                                            mCanvas.drawLine(xPoints[xBoxCheckIndex], yPoints[yBoxCheckIndex], xPoints[xBoxCheckIndex], yPoints[yBoxCheckIndex + 1], mPaintLine[pTurn]);
                                            //check whether the box is done and then set win
                                            flagChangePlayer=true;
                                            for (int i = xBoxCheckIndex; i >= xBoxCheckIndex - 1; i--) {
                                                if (isValidIndex(i, yBoxCheckIndex)) {
                                                    if (checkNSetBox(boxes[i][yBoxCheckIndex])) {
                                                        mCanvas.drawRect(xPoints[i] + 9, yPoints[yBoxCheckIndex] + 9, xPoints[i + 1] - 9, yPoints[yBoxCheckIndex + 1] - 9, mPaintBox[pTurn]);
                                                        flagChangePlayer=false;
                                                    }
                                                }
                                            }
                                            //change the playerturn int to other value
                                            if(flagChangePlayer)
                                                changePTurn();
                                        }
                                    }
                                }
                            }
                        }  //if flag
                }//switch
                imageView.invalidate();
                return true;
            }
        });

    }

    private boolean checkNSetBox(Box box) {
        if(box.getWin()!=0&&box.isDone()) {
            flagChangePlayer = true;
            return false;
        }
        if (box.isDone()&&box.getWin()==0) {
            box.setWin(pTurn);
            flagChangePlayer=false;
            boxCount[pTurn]++;
            if(totalBoxes==boxCount[0]+boxCount[1]){
                if(boxCount[0]==boxCount[1])
                    winner=2;
                else if(boxCount[0]>boxCount[1])
                    winner=0;
                else
                    winner=1;
                afterFinish();
            }
            return true;
        }
        return false;
    }

    private void afterFinish() {
        gridLayout.removeView(imageView);
        turnTextView.setText(R.string.win);
        if(winner==0){
            turnsView.setText(P1name);
            ll.setBackgroundColor(getResources().getColor(R.color.player1));
        }
        else if(winner==1){
            turnsView.setText(P2name);
            ll.setBackgroundColor(getResources().getColor(R.color.player1));

        }
        else {
            turnsView.setText(R.string.tie);
            ll.setBackgroundColor(getResources().getColor(R.color.Gray));
        }
    }


    private int yFind(int y) {
        for (int i = 0; i < Size - 1; i++) {
            if (y > yPoints[i] && y < yPoints[i + 1])
                return i;
        }
        return -1;
    }

    private int xCheck(int x) {
        for (int i = 0; i < Size; i++) {
            if (x < (xPoints[i] + 15) && x > (xPoints[i] - 15))
                return i;
        }
        return -1;
    }

    private int xFind(int x) {
        for (int i = 0; i < Size - 1; i++) {
            if (x > xPoints[i] && x < xPoints[i + 1])
                return i;
        }
        return -1;
    }

    private int yCheck(int y) {
        for (int i = 0; i < Size; i++) {
            if (y < (yPoints[i] + 15) && y > (yPoints[i] - 15))
                return i;
        }
        return -1;
    }

    private void changePTurn() {
        flagChangePlayer=true;
        if (pTurn == 0) {
            pTurn = 1; //1 means 2nd player
            turnsView.setText(P2name);
            ll.setBackgroundColor(getResources().getColor(R.color.player2));
            gridLayout.setBackgroundColor(getResources().getColor(R.color.player2));

        } else {
            pTurn = 0;  //0 means 1st  player
            turnsView.setText(P1name);
            ll.setBackgroundColor(getResources().getColor(R.color.player1));
            gridLayout.setBackgroundColor(getResources().getColor(R.color.player1));

        }
        mCanvas.drawRect(0,0,viewWidth,yMarginSpacing-35,mPaintLine[pTurn]);
        mCanvas.drawRect(0,yPoints[Size-1]+35,viewWidth,viewHeight,mPaintLine[pTurn]);
    }

    private boolean isValidIndex(int a, int b) {
        return (a < (Size - 1) && a > -1 && b < (Size - 1) && b > -1);
    }
}
