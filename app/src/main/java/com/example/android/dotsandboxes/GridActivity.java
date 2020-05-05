package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GridActivity extends AppCompatActivity {

    private String P1name;
    private String P2name;
    private int Size;
    private TextView Check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Check=findViewById(R.id.checkText);

        Intent gridActivity=getIntent();
        P1name=gridActivity.getStringExtra("p1");
        P2name=gridActivity.getStringExtra("p2");
        Size=gridActivity.getIntExtra("Size",5);
        Check.setText(P1name+"   "+P2name+"  "+Size);
    }
}
