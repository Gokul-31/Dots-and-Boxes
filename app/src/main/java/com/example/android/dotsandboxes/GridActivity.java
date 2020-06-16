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
import android.widget.TextView;

import java.util.ArrayList;

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
    //Paint
    private Paint mPaintBlack = new Paint();  //for black dots
    private Paint mPaintWhite = new Paint();  //for undo function
//    private Paint[] mPaintLine = new Paint[2];
//    private Paint[] mPaintBox = new Paint[2];
    private ArrayList<Paint> mPaintLine = new ArrayList<Paint>();
    private ArrayList<Paint> mPaintBox = new ArrayList<Paint>();
    private final int MUL=200;
    //grid
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
    private int xBCI;
    private int yBCI;
    private int pTurn = 1;
    private Box[][] boxes;
    private boolean checkValue;
    private boolean flagChangePlayer = true;
    private ConstraintLayout ll;
    private int totalBoxes;
    private ArrayList<Inte> winner = new ArrayList<>();
    private TextView turnTextView;
    private Button undoBtn;
    private ArrayList<Inte> boxCount=new ArrayList<>();
    private int[] colors =new int[6];
    //undo
    private int[] undoSideI = new int[2];
    private int[] undoSideJ = new int[2];
    private int[] undoSideIndex = new int[2];
    private int[] undoX = new int[2];
    private int[] undoY = new int[2];
    private boolean undoChanged = true;
    private int lastPlayer=0;
    //players supp
    private static int num;
    private ArrayList<String> names= new ArrayList<String>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        final Intent gridActivity = getIntent();
        Size = gridActivity.getIntExtra("Size", 5);
        num=gridActivity.getIntExtra("Num",3);

        //setColor
        colors[0]=R.color.color1;
        colors[1]=R.color.color2;
        colors[2]=R.color.color3;
        colors[3]=R.color.color4;
        colors[4]=R.color.color5;
        colors[5]=R.color.color6;

        //save names and paint
        for (int i=0;i<num;i++){
            names.add("player "+ (i+1));
            mPaintLine.add(new Paint());
            mPaintBox.add(new Paint());
            mPaintLine.get(i).setColor(getResources().getColor(colors[i]));
            mPaintLine.get(i).setStrokeWidth(15);
            mPaintBox.get(i).setColor(getResources().getColor(colors[i]));
            boxCount.add(new Inte(0));
        }


        //save for the views
        boxes = new Box[Size - 1][Size - 1];
        for (int i = 0; i < Size - 1; i++)
            for (int j = 0; j < Size - 1; j++) {
                boxes[i][j] = new Box();
            }

        ll = findViewById(R.id.root);
        turnTextView = findViewById(R.id.turnText);
        gridLayout = findViewById(R.id.gridLayout);
        start = findViewById(R.id.start);
        imageView = findViewById(R.id.gridImageView);
        turnsView = findViewById(R.id.turns);
        turnsView.setText(P1name);
        undoBtn = findViewById(R.id.undo);

        totalBoxes = (int) Math.pow(Size - 1, 2);

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
                mPaintWhite.setColor(getResources().getColor(R.color.white));
                mPaintWhite.setStrokeWidth(15);

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

                mCanvas.drawRect(0, 0, viewWidth, yMarginSpacing - 35, mPaintLine.get(0));
                mCanvas.drawRect(0, yPoints[Size - 1] + 35, viewWidth, viewHeight, mPaintLine.get(0));

                for (int i = 0; i < Size; i++) {
                    Log.i("MainActivity", "onClick: " + xPoints[i]);
                }

                drawGrid();

                //drawing done
                pTurn = 0;
                turnsView.setText(names.get(pTurn));
                gridLayout.setBackgroundColor(getResources().getColor(R.color.color1));
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
                            yBCI = yCheck(Y);
                            if (yBCI != -1) {
                                xBCI = xFind(X);      //CHECK WHETHER INDEXES CAN BE -VE
                                if (xBCI != -1) {
                                    if (isValidIndex(xBCI, yBCI - 1))
                                        checkValue = boxes[xBCI][yBCI - 1].getSides()[2];
                                    else {
                                        checkValue = boxes[xBCI][yBCI].getSides()[0];
                                    }
                                    if (!checkValue) {
                                        undoChanged = false;
                                        //change the 3rd one of yBCI-1 to true if it already isnt
                                        if (isValidIndex(xBCI, yBCI - 1)) {
                                            undoSideI[0] = xBCI;
                                            undoSideJ[0] = yBCI - 1;
                                            undoSideIndex[0] = 2;
                                            boxes[xBCI][yBCI - 1].setSide3(true);
                                        } else {
                                            undoSideI[0] = -1;
                                            undoSideJ[0] = -1;
                                            undoSideIndex[0] = -1;
                                        }
                                        //change the 1st one of yBCI to true          all are indexes
                                        if (isValidIndex(xBCI, yBCI)) {
                                            undoSideI[1] = xBCI;
                                            undoSideJ[1] = yBCI;
                                            undoSideIndex[1] = 0;
                                            boxes[xBCI][yBCI].setSide1(true);
                                        } else {
                                            undoSideI[1] = -1;
                                            undoSideJ[1] = -1;
                                            undoSideIndex[1] = -1;
                                        }
                                        //draw line from xBCI to xBCI+1 and Y being yBCI
                                        mCanvas.drawLine(xPoints[xBCI], yPoints[yBCI], xPoints[xBCI + 1], yPoints[yBCI], mPaintLine.get(pTurn));
                                        undoX[0] = xPoints[xBCI];
                                        undoX[1] = xPoints[xBCI + 1];
                                        undoY[0] = yPoints[yBCI];
                                        undoY[1] = yPoints[yBCI];
                                        //check whether the box is done and then set win
                                        flagChangePlayer = true;
                                        for (int i = yBCI; i >= yBCI - 1; i--) {
                                            if (isValidIndex(xBCI, i)) {
                                                if (checkNSetBox(boxes[xBCI][i])) {
                                                    mCanvas.drawRect(xPoints[xBCI] + 9, yPoints[i] + 9, xPoints[xBCI + 1] - 9, yPoints[i + 1] - 9, mPaintBox.get(pTurn));
                                                }
                                            }
                                        }
                                        //change the playerturn int to the other value
                                        if (flagChangePlayer) {
                                            lastPlayer=pTurn;
                                            changePTurn();
                                        }
                                        drawGrid();
                                    }
                                }
                            } else {
                                xBCI = xCheck(X);
                                if (xBCI != -1) {
                                    yBCI = yFind(Y);
                                    if (yBCI != -1) {
                                        if (isValidIndex(xBCI - 1, yBCI))
                                            checkValue = boxes[xBCI - 1][yBCI].getSides()[1];
                                        else {
                                            checkValue = boxes[xBCI][yBCI].getSides()[3];
                                        }
                                        if (!checkValue) {
                                            undoChanged = false;
                                            //change the 2nd one of xBCI-1 to true
                                            if (isValidIndex(xBCI - 1, yBCI)) {
                                                boxes[xBCI - 1][yBCI].setSide2(true);
                                                undoSideI[0] = xBCI - 1;
                                                undoSideJ[0] = yBCI;
                                                undoSideIndex[0] = 1;
                                            } else {
                                                undoSideI[0] = -1;
                                                undoSideJ[0] = -1;
                                                undoSideIndex[0] = -1;
                                            }
                                            //change the 4th one of xBCI to true          all are indexes
                                            if (isValidIndex(xBCI, yBCI)) {
                                                boxes[xBCI][yBCI].setSide4(true);
                                                undoSideI[1] = xBCI;
                                                undoSideJ[1] = yBCI;
                                                undoSideIndex[1] = 3;
                                            } else {
                                                undoSideI[1] = -1;
                                                undoSideJ[1] = -1;
                                                undoSideIndex[1] = -1;
                                            }
                                            //draw line from yBCI to yBCI+1 and X being xBCI
                                            mCanvas.drawLine(xPoints[xBCI], yPoints[yBCI], xPoints[xBCI], yPoints[yBCI + 1], mPaintLine.get(pTurn));
                                            undoX[0] = xPoints[xBCI];
                                            undoX[1] = xPoints[xBCI];
                                            undoY[0] = yPoints[yBCI];
                                            undoY[1] = yPoints[yBCI + 1];
                                            //check whether the box is done and then set win
                                            flagChangePlayer = true;
                                            for (int i = xBCI; i >= xBCI - 1; i--) {
                                                if (isValidIndex(i, yBCI)) {
                                                    if (checkNSetBox(boxes[i][yBCI])) {
                                                        mCanvas.drawRect(xPoints[i] + 9, yPoints[yBCI] + 9, xPoints[i + 1] - 9, yPoints[yBCI + 1] - 9, mPaintBox.get(pTurn));
                                                        flagChangePlayer = false;
                                                    }
                                                }
                                            }
                                            //change the playerturn int to other value
                                            if (flagChangePlayer) {
                                                lastPlayer=pTurn;
                                                changePTurn();
                                            }
                                            drawGrid();
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

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoChanged) {
                    undoChanged = true;
                    for (int i=0;i<2;i++){
                        if (undoSideI[0] != -1) {
                            if (boxes[undoSideI[0]][undoSideJ[0]].isDone()) { //make it white
                                boxes[undoSideI[0]][undoSideJ[0]].setWin(0);
                                mCanvas.drawRect(xPoints[undoSideI[0]] + 9, yPoints[undoSideJ[0]] + 9, xPoints[undoSideI[0] + 1] - 9, yPoints[undoSideJ[0] + 1] - 9, mPaintWhite);
                                boxCount.get(pTurn).decN();
                                lastPlayer=pTurn;
                            }
                            boxes[undoSideI[0]][undoSideJ[0]].setSideFalse(undoSideIndex[0]);
                        }
                    }

                    //check for completed boxes also// decrease while box is refrained from completion
                    mCanvas.drawLine(undoX[0], undoY[0], undoX[1], undoY[1], mPaintWhite);
                    mCanvas.drawCircle(undoX[0], undoY[0], 14, mPaintBlack);
                    mCanvas.drawCircle(undoX[1], undoY[1], 14, mPaintBlack);
                    //deal with the player chance
                    undoPlayer();
                }
            }
        });
    }

    private void undoPlayer() {
        if(lastPlayer==0){
            pTurn=num-1;
        }
        else
        {
            pTurn--;
        }
        changePTurn();
    }

    private boolean checkNSetBox(Box box) {
        if (box.getWin() != 0 && box.isDone()) {
            flagChangePlayer = true;
            return false;
        }
        if (box.isDone() && box.getWin() == 0) {
            box.setWin(pTurn);
            flagChangePlayer = false;
            boxCount.get(pTurn).incN();
            //checkFinish
            checkFinish();
            return true;
        }
        return false;
    }

    private void checkFinish() {
        int sum=0;
        for(int i=0;i<num;i++){
            sum+=boxCount.get(i).getN();
        }
        if(sum==totalBoxes){
            int max=0;
            for(int i=0;i<num;i++){
                max= Math.max(max, boxCount.get(i).getN());
            }
            for(int i=0;i<num;i++){
                if(max==boxCount.get(i).getN()){
                    winner.add(new Inte(i));
                }
            }
            afterFinish();
        }
    }

    @SuppressLint("SetTextI18n")
    private void afterFinish() {
        String resultText;
        if(winner.size()==1){
            resultText="WINNER:\n"+names.get(winner.get(0).getN());
        }
        else {
              resultText="WINNER: \n";
            for (int i = 0; i < winner.size(); i++) {
                resultText+="\n"+names.get(winner.get(i).getN());
            }
        }

        Intent resultIntent = new Intent(getApplicationContext(),result.class);
        resultIntent.putExtra("result",resultText);
        startActivity(resultIntent);
    }


    private int yFind(int y) {
        for (int i = 0; i < Size - 1; i++) {
            if (y > (yPoints[i] + 30) && y < (yPoints[i + 1] - 30))
                return i;
        }
        return -1;
    }

    private int xCheck(int x) {
        for (int i = 0; i < Size; i++) {
            if (x < (xPoints[i] + 25) && x > (xPoints[i] - 25))
                return i;
        }
        return -1;
    }

    private int xFind(int x) {
        for (int i = 0; i < Size - 1; i++) {
            if (x > (xPoints[i] + 30) && x < (xPoints[i + 1] - 30))
                return i;
        }
        return -1;
    }

    private int yCheck(int y) {
        for (int i = 0; i < Size; i++) {
            if (y < (yPoints[i] + 25) && y > (yPoints[i] - 25))
                return i;
        }
        return -1;
    }

    private void changePTurn() {
        flagChangePlayer = true;

        if(pTurn==num-1){
            pTurn=0;
        }
        else
            pTurn++;
        turnsView.setText(names.get(pTurn));
        ll.setBackgroundColor(getResources().getColor(colors[pTurn]));
        gridLayout.setBackgroundColor(getResources().getColor(colors[pTurn]));

        mCanvas.drawRect(0, 0, viewWidth, yMarginSpacing - 35, mPaintLine.get(pTurn));
        mCanvas.drawRect(0, yPoints[Size - 1] + 35, viewWidth, viewHeight, mPaintLine.get(pTurn));
    }

    private boolean isValidIndex(int a, int b) {
        return (a < (Size - 1) && a > -1 && b < (Size - 1) && b > -1);
    }

    private void drawGrid() {
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                mCanvas.drawCircle(xPoints[i], yPoints[j], 14, mPaintBlack);
            }
        }
    }
}
