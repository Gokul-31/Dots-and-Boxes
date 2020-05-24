package com.example.android.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    //Declaration:
    private EditText name1;
    private EditText name2;
    private EditText gridSize;
    private Button goNext;
    private EditText playerET;
    private String p1Name;
    private String p2Name;
    private int Size=5;
    private int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //getting Views:
        gridSize=findViewById(R.id.grid_num);
        goNext=findViewById(R.id.Next);
        playerET=findViewById(R.id.players);

        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using the Views:
                Size=Integer.parseInt(gridSize.getText().toString());
                num=Integer.parseInt(playerET.getText().toString());

                //border case check
                if(Size<3||Size>15){
                    Toast.makeText(getApplicationContext(),"Enter size between 3 and 15",Toast.LENGTH_SHORT).show();
                }
                else if(num<2||num>6){
                    Toast.makeText(getApplicationContext(),"Enter players between 2 and 6",Toast.LENGTH_SHORT).show();
                }
                else {
                    //pass through intent
                    Intent gridActivity = new Intent(Main2Activity.this, GridActivity.class);
                    gridActivity.putExtra("Num",num);
                    gridActivity.putExtra("Size", Size);
                    startActivity(gridActivity);
                }
            }
        });

    }
}
