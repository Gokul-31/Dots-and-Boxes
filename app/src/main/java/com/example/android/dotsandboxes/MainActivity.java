package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declaration:
    private EditText name1;
    private EditText name2;
    private EditText gridSize;
    private Button goNext;
    private String p1Name;
    private String p2Name;
    private int Size=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting Views:
        name1=findViewById(R.id.player1_name);
        name2=findViewById(R.id.player2_name);
        gridSize=findViewById(R.id.grid_num);
        goNext=findViewById(R.id.Go);

        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using the Views:
                p1Name=name1.getText().toString();
                p2Name=name2.getText().toString();
                Size=Integer.parseInt(gridSize.getText().toString());

                //border case check
                if(Size<3||Size>15){
                    Toast.makeText(getApplicationContext(),"Enter number between 3 and 15",Toast.LENGTH_SHORT).show();
                }
                else {
                    //pass through intent
                    Intent gridActivity = new Intent(getApplicationContext(), GridActivity.class);
                    gridActivity.putExtra("p1", p1Name);
                    gridActivity.putExtra("p2", p2Name);
                    gridActivity.putExtra("Size", Size);
                    startActivity(gridActivity);
                }
            }
        });

    }
}
