package com.example.kalyani.mindquizzapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends Activity {


    Button bt1,bt2;
    TextView myscore,thankew,performance;

    String s="";
    int score1=0;
    private int high_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
       thankew=findViewById(R.id.thanks);
       thankew.setShadowLayer(1,3,3, Color.WHITE);
        
        bt1=findViewById(R.id.button4);
        bt2=findViewById(R.id.button5);
        myscore=findViewById(R.id.myscore);
        performance=findViewById(R.id.perf);

        s=getIntent().getStringExtra("highscore");
        score1=Integer.parseInt(s);
            myscore.setText("Your score is : " + score1);

        if (score1>=7){
            performance.setText("Excellent performance");
            performance.setTextColor(Color.MAGENTA);
            performance.setTextSize(35);
        }
        else if (score1>3){
            performance.setText("Good performance");
            performance.setTextColor(Color.BLUE);
            performance.setTextSize(35);
        }else {
            performance.setText("Poor performance");
            performance.setTextColor(Color.RED);
            performance.setTextSize(35);
        }

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Finish.this,First.class));
            }
        });
    }





    @Override
    public void onBackPressed() {
        if (!shouldAllowBack()){
            doSomething();
        }else {
            super.onBackPressed();
        }
    }
    public void doSomething(){

    }
    public boolean shouldAllowBack(){
        return false;
    }
}
