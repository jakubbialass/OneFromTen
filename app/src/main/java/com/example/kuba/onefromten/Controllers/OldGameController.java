package com.example.kuba.onefromten.Controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Fragments.SoloGame;
import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.R;

import java.util.ArrayList;

public class OldGameController extends Thread {

    private ArrayList<Question> questions;
    private Button[] answerButtons;
    private TextView questionTextView;
    protected boolean isQuestionShowed, isAnswered, isWaitingForAnswer, isGameOver;
    private Question currentQuestion;
    private int numberOfCurrentQuestion;
    private int score;
    private String answer;
    private Context context;
    private int usedRevives;
    private int numberOfExtraQuestions;
    private boolean refusedAd;
    private int clickedButtonNumber;
    private LinearLayout gameOverLinearLayout;
    private boolean timeOut;
    private Activity myActivity;
    private boolean isSoundOn;

    private static final int SETTING_ANSWER_TIME =800;
    private static final int SETTING_QUESTION_TIME=2000;
    private static final int CHECKING_ANSWER_TIME =1000;
    private static final int TIME_FOR_ANSWER_IN_SECONDS =10;

    public OldGameController(Context context, ArrayList<Question> questions, Button[] answerButtons, TextView questionTextView, LinearLayout gameOverLinearLayout, Activity myActivity){

        this.context = context;
        this.questions = questions;
        this.answerButtons = answerButtons;
        this.questionTextView = questionTextView;
        this.gameOverLinearLayout = gameOverLinearLayout;
        this.myActivity = myActivity;
        //this.gameOverLinearLayout.setVisibility(View.VISIBLE);
        isQuestionShowed = false;
        isAnswered = false;
        isWaitingForAnswer = false;
        score = 0;
        answer = "";
        numberOfCurrentQuestion = 0;
        currentQuestion = questions.get(numberOfCurrentQuestion);
        isGameOver = false;
        usedRevives = 0;
        timeOut=false;
        isSoundOn = true;

        for(int i=0; i<4; i++){
            answerButtons[i].setClickable(false);
            answerButtons[i].setText("");
        }
        questionTextView.setText("");


    }

    public OldGameController(){

    }


    public void run(){
        Log.v("womtek", "zaczynam " + String.valueOf(isGameOver));

        while(!isGameOver) {
            Log.v("womtek", "elo 1 " + String.valueOf(isGameOver));
            showQuestion();
            Log.v("womtek", "elo 2 " + String.valueOf(isGameOver));
            showAnswers();
            Log.v("womtek", "elo 3 " + String.valueOf(isGameOver));
            waitForAnswer();
            Log.v("womtek", "elo 4 " + String.valueOf(isGameOver));
            checkAnswer();
            Log.v("womtek", "elo 5 " + String.valueOf(isGameOver));
        }
        Log.v("womtek", "koncze " + String.valueOf(isGameOver));

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameOverLinearLayout.setVisibility(View.VISIBLE);
            }
        });


    }


    /**
     * Żeby pytanie pojawiało się po literce w czasie całkowitym @param settingQuestionTime
     * można podzielić @param settingQuestionTime przez długość stringa = @param timeForLetter i w pętli for
     * w wątku wyświetlać po literze i usypiać na @param timeForLetter
     */

    private synchronized void showQuestion(){

        try{
            String text = currentQuestion.getQuestion();
            int timeForLetter = SETTING_QUESTION_TIME/text.length();
            SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            int soundId = soundPool.load(context, R.raw.horn_typewriter, 1);
            for(int i=0; i<text.length(); i++){
                char letter = text.charAt(i);
                questionTextView.append(String.valueOf(letter));
                if(isSoundOn )
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                Thread.sleep(timeForLetter);
            }
            soundPool.release();
            soundPool = null;
            isQuestionShowed = true;

            //wait();
        } catch (Exception e){}


    }


    private synchronized void showAnswers(){

        ArrayList<String> answers = currentQuestion.getAnswers();

        try{
            if(isQuestionShowed) {
                for(int i=0; i<4; i++) {
                    answerButtons[i].setText(answers.get(i));
                    Thread.sleep(SETTING_ANSWER_TIME);
                }
                isWaitingForAnswer = true;
                for(int i=0; i<4; i++){
                    answerButtons[i].setClickable(true);
                }
                //wait();
            }
        } catch (Exception e){}


    }

    private synchronized void waitForAnswer(){


        try{
            if (isWaitingForAnswer) {
                int i=0;
                while (!isAnswered && i < TIME_FOR_ANSWER_IN_SECONDS) {
                    Thread.sleep(1000);
                    i++;
                }
                if(i == TIME_FOR_ANSWER_IN_SECONDS) {
                    timeOut = true;
                    isAnswered = true;
                }
                
                isWaitingForAnswer = false;
            }

        } catch (Exception e){}
    }

    private synchronized void checkAnswer(){

        try {
            if(isAnswered) {
                for(int i=0; i<4; i++){
                    answerButtons[i].setClickable(false);
                }
                Thread.sleep(CHECKING_ANSWER_TIME);
                if(timeOut)
                    refusedAd=true;
                else if(currentQuestion.isCorrectAnswer(answer)) {

                    if(answerButtons[clickedButtonNumber].getText().toString().equals(answer)) {
                        answerButtons[clickedButtonNumber].setBackgroundColor(Color.GREEN);
                        Thread.sleep(2000);
                        answerButtons[clickedButtonNumber].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
                    }
                } else {

                    if(answerButtons[clickedButtonNumber].getText().toString().equals(answer)) {
                        answerButtons[clickedButtonNumber].setBackgroundColor(Color.RED);
                        Thread.sleep(2000);
                        answerButtons[clickedButtonNumber].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
                        refusedAd = true;
                    }
                }

                if(refusedAd ||
                questions.size() - numberOfExtraQuestions + usedRevives == numberOfCurrentQuestion){
                    isGameOver = true;
                }
                else{
                    for(int i=0; i<4; i++)
                        answerButtons[i].setText("");
                    questionTextView.setText("");

                    isAnswered = false;
                    numberOfCurrentQuestion++;
                    currentQuestion = questions.get(numberOfCurrentQuestion);
                }


                //notifyAll();
            }
        } catch (Exception e){}




    }



    private synchronized void endGame(){
        answerButtons[0].setVisibility(View.GONE);
        Log.v("womtek", "ustawilem na buttona 1");
        answerButtons[1].setVisibility(View.GONE);
        Log.v("womtek", "ustawilem na buttona 2");
        answerButtons[2].setVisibility(View.GONE);
        Log.v("womtek", "ustawilem na buttona 3");
        Log.v("womtek", "bede ustawial visible " + String.valueOf(gameOverLinearLayout.getVisibility()));
        isGameOver = true;

        gameOverLinearLayout.setVisibility(View.VISIBLE);
        Log.v("womtek", "ustawilem visible " + String.valueOf(gameOverLinearLayout.getVisibility()));

    }




    public void setAnswer(String answerFromUser, int clickedButtonNumber){
        answer = answerFromUser;
        isAnswered = true;
        this.clickedButtonNumber = clickedButtonNumber;
    }


}

/*
to jest wczesniejszy kodzik, zanim zaczalem kombinowac z wrypaniem wszystkiego do public run




    private synchronized void showQuestion(){

        try{
            questionTextView.setText(currentQuestion.getQuestion());
            Thread.sleep(settingQuestionTime);
            wait();
        } catch (Exception e){}


    }


    private synchronized void showAnswers(){

        ArrayList<String> answers = currentQuestion.getAnswers();

        try{
            if(isQuestionShowed) {
                for (int i = 0; i < 4; i++) {
                    answerButtons[i].setText(answers.get(i));
                    Thread.sleep(SETTING_ANSWER_TIME);
                }
                isWaitForAnswer = true;
                wait();
            }
        } catch (Exception e){}


    }

    private void waitForAnswer(){


        try{
            if (isWaitForAnswer) {
                while (!isAnswered) {
                    Thread.sleep(1000);
                }
                isWaitForAnswer = false;
            }

        } catch (Exception e){}
    }

    private void checkAnswer(String answer){

        try {
            if (isAnswered) {
                if (currentQuestion.isCorrectAnswer(answer)) {
                    for (int i = 0; i < 4; i++) {
                        if (answerButtons[i].getText().toString().equals(answer)) {
                            answerButtons[i].setBackgroundColor(Color.GREEN);
                            Thread.sleep(2000);
                            answerButtons[i].setBackgroundColor(Color.YELLOW);
                        }
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        if (answerButtons[i].getText().toString().equals(answer)) {
                            answerButtons[i].setBackgroundColor(Color.RED);
                            Thread.sleep(2000);
                            answerButtons[i].setBackgroundColor(Color.YELLOW);
                        }
                    }
                }

                isAnswered = false;
                numberOfCurrentQuestion++;
                currentQuestion = questions.get(numberOfCurrentQuestion);
                notifyAll();
            }
        } catch (Exception e){}




    }



 */