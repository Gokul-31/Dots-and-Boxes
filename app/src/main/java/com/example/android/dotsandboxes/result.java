package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    private TextView resultView;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent recieveIntent = getIntent();
        resultView=findViewById(R.id.resultView);
        resultView.setText(recieveIntent.getStringExtra("result"));

        resetBtn=findViewById(R.id.button);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(reset);
            }
        });
    }
}
