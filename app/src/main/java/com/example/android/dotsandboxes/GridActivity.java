package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

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

public class GridActivity extends AppCompatActivity {

    private String P1name;
    private String P2name;
    private int Size;
    private ViewGroup gridLayout;
    private Button start;
    private ImageView imageView;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint= new Paint();
    private int viewWidth;
    private int viewHeight;
    private final int xMarginSpacing=25;
    private int yMarginSpacing;
    private int xyGrid;
    private int lineLength;
    private int X;
    private int Y;
    private boolean flag1=false;   //whether the canvas has been created
    private int[] xPoints;
    private int[] yPoints;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        final Intent gridActivity=getIntent();
        P1name=gridActivity.getStringExtra("p1");
        P2name=gridActivity.getStringExtra("p2");
        Size=gridActivity.getIntExtra("Size",5);

        //save for the views
        gridLayout=findViewById(R.id.gridLayout);
        start=findViewById(R.id.start);
        imageView=findViewById(R.id.gridImageView);
        xPoints=new int[Size];
        yPoints=new int[Size];

        //clickListener for the button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLayout.removeView(start);   //works
                //setGrid
                viewWidth=imageView.getWidth();
                viewHeight=imageView.getHeight();

                mPaint.setColor(getResources().getColor(R.color.black));
                mBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.ARGB_8888);
                imageView.setImageBitmap(mBitmap);
                mCanvas=new Canvas(mBitmap);
                flag1=true;

                //draw grid points
                ////ready values
                xyGrid=viewWidth-2*xMarginSpacing;
                lineLength=xyGrid/(Size-1);
                yMarginSpacing=(viewHeight-xyGrid)/2;

//                Log.i("MainActivity", "onClick: "+xPoints.length);
//                Log.i("MainActivity", "onClick: "+viewWidth);
//                Log.i("MainActivity", "onClick: "+viewHeight);
//                Log.i("MainActivity", "onClick: "+xMarginSpacing);
//                Log.i("MainActivity", "onClick: "+yMarginSpacing);
//                Log.i("MainActivity", "onClick: "+xGrid);
//                Log.i("MainActivity", "onClick: "+yGrid);
//                Log.i("MainActivity", "onClick: "+lineLength);

                //draw
                xPoints[0]=xMarginSpacing;
                yPoints[0]=yMarginSpacing;
                for(int i=1;i<Size;i++){
                    xPoints[i]=xPoints[i-1]+lineLength;
                    yPoints[i]=yPoints[i-1]+lineLength;
                }

                for (int i=0;i<Size;i++){
                    Log.i("MainActivity", "onClick: "+xPoints[i]);
                }

                for(int i=0;i<Size;i++){
                    for(int j=0;j<Size;j++){
                        mCanvas.drawCircle(xPoints[i],yPoints[j],14,mPaint);
                    }
                }

                //drawing done
            }
        });

//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(flag1){
//                    X=event.getX()
//                }
//                return true;
//            }
//        });

    }
}
