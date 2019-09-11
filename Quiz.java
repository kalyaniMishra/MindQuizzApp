package com.example.kalyani.mindquizzapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz extends Activity {



    private static final long COUNTDOWN_IN_MILLIS=40000;
    private TextView textViewQuestion,textViewScore,textViewQuestionCount,textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1,rb2,rb3,rb4;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultcd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    private List<Question> questionList;
    private int questionCounter;
    private Question currentQuestion;

    private int score=0;
    private boolean answered;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textViewQuestion=findViewById(R.id.text_view_question);
        textViewCountDown=findViewById(R.id.text_view_countdown);
        textViewScore=findViewById(R.id.text_view_score);
        textViewQuestionCount=findViewById(R.id.text_view_question_countdown);
        rbGroup=findViewById(R.id.radioGroup);
        rb1=findViewById(R.id.radioButton);
        rb2=findViewById(R.id.radioButton2);
        rb3=findViewById(R.id.radioButton3);
        rb4=findViewById(R.id.radioButton4);
        buttonConfirmNext=findViewById(R.id.button2);


        textColorDefaultRb=rb1.getTextColors();
        getTextColorDefaultcd=textViewCountDown.getTextColors();

       QuizDbHelper dbHelper=new QuizDbHelper(this);
        questionList=dbHelper.getAllQuestions();

        Collections.shuffle(questionList);
        showNextQuestion();
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(Quiz.this,"Please select an answer",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter<10){
            currentQuestion=questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question : "+questionCounter+"/10");
            answered=false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            finishQuiz();
        }
    }
    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText(){
        int minutes=(int)(timeLeftInMillis/1000)/60;
        int seconds=(int)(timeLeftInMillis/1000)%60;

        String timeFormated=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormated);
        if (timeLeftInMillis<10000) {
            textViewCountDown.setTextColor(Color.RED);
        }
    }

    private void checkAnswer(){
        answered=true;
        countDownTimer.cancel();
        RadioButton rbSelected=findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr=rbGroup.indexOfChild(rbSelected)+1;
        if (answerNr==currentQuestion.getAnswerNr()) {
            Toast.makeText(this,"Well done",Toast.LENGTH_SHORT).show();
            score++;
            textViewScore.setText("Score : " + score);
        }
        else {
            Toast.makeText(this,"Wrong!!",Toast.LENGTH_SHORT).show();
        }
        showSolution();

    }
    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is Correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 2 is Correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 3 is Correct");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 4 is Correct");
                break;
        }
        if (questionCounter<10){
            buttonConfirmNext.setText("Next");
        }
        else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz(){
        Intent intent=new Intent(Quiz.this,Finish.class);
        intent.putExtra("highscore",""+score);

      startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime+2000>System.currentTimeMillis()){
            finishQuiz();
        }
        else {
            Toast.makeText(this,"Press back again to finish",Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
