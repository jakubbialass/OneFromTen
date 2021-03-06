package com.example.kuba.onefromten.Controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.R;

import java.util.ArrayList;

public class GameController extends Thread {

    private static int SETTING_QUESTION_TIME, SETTING_ANSWER_TIME;
    private static int TIME_FOR_ANSWER_IN_SECONDS, CHECKING_ANSWER_TIME;
    private Context context;
    private Button[] answerButtons;
    private String answer;
    private int clickedButtonNumber;
    private Question currentQuestion;
    protected int numberOfCurrentQuestion;
    protected ArrayList<Question> questions;
    private Activity myActivity;
    private boolean isClickableAnswerButtons;


    protected boolean isSoundOn;
    protected boolean isQuestionShowed;
    protected boolean isWaitingForAnswer;
    protected boolean isAnswered;
    protected boolean isTimeOut;
    protected boolean isGameOver;
    protected boolean clickedWhileNotMyTurn;
    protected TextView timer;
    protected boolean lastQuestion;



    public GameController(){

        this.isSoundOn = true;
        this.isQuestionShowed = false;
        this.isWaitingForAnswer = false;
        this.isAnswered = false;
        this.isTimeOut = false;
        this.answer = "";
        this.isGameOver = false;
        this.numberOfCurrentQuestion = 0;
        this.isClickableAnswerButtons = true;
        this.clickedWhileNotMyTurn = false;
        this.lastQuestion = false;


    }





    protected void initializeVariables(Context context, ArrayList<Question> questions, Button[] answerButtons,
      int SETTING_QUESTION_TIME, int SETTING_ANSWER_TIME, int TIME_FOR_ANSWER_IN_SECONDS, int CHECKING_ANSWER_TIME, Activity myActivity){

        this.context = context;
        this.answerButtons = answerButtons;
        this.questions = questions;
        this.currentQuestion = questions.get(numberOfCurrentQuestion);
        this.myActivity = myActivity;



        this.SETTING_QUESTION_TIME = SETTING_QUESTION_TIME;
        this.SETTING_ANSWER_TIME = SETTING_ANSWER_TIME;
        this.TIME_FOR_ANSWER_IN_SECONDS = TIME_FOR_ANSWER_IN_SECONDS;
        this.CHECKING_ANSWER_TIME = CHECKING_ANSWER_TIME;

    }







    protected synchronized void showQuestion(final TextView questionTextView){

        try{
            isAnswered = false;
            String text = currentQuestion.getQuestion();
            int timeForLetter = SETTING_QUESTION_TIME/text.length();
            SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            int soundId = soundPool.load(context, R.raw.horn_typewriter, 1);
            for(int i=0; i<text.length(); i++){
                final char letter = text.charAt(i);
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            questionTextView.append(String.valueOf(letter));
                        } catch (Exception e){}
                    }
                });

                if(isSoundOn )
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                Thread.sleep(timeForLetter);
            }
            soundPool.release();
            soundPool = null;

            isQuestionShowed = true;

            //wait();
        } catch (InterruptedException e){}


    }



    protected synchronized void showAnswers(){

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
            }
        } catch (Exception e){}

    }



    protected synchronized void waitForAnswer(){


        try{
            if (isWaitingForAnswer) {
                int i=0;
                int secondsLeft = TIME_FOR_ANSWER_IN_SECONDS;
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText(String.valueOf(TIME_FOR_ANSWER_IN_SECONDS));
                    }
                });
                while (!isAnswered && i < TIME_FOR_ANSWER_IN_SECONDS) {
                    Thread.sleep(1000);
                    secondsLeft--;
                    i++;
                    final int mSecondsLeft = secondsLeft;
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() { timer.setText(String.valueOf(mSecondsLeft));
                        }
                    });
                }
                if(i == TIME_FOR_ANSWER_IN_SECONDS) {
                    isTimeOut = true;
                }
                isAnswered = true;
                isWaitingForAnswer = false;
            }

        } catch (Exception e){}
    }




    protected boolean isAnswerCorrect(String answer){

        try{
            if(isAnswered){
                for(int i=0; i<4; i++){
                    answerButtons[i].setClickable(false);
                }
                if(isTimeOut)
                    return false;
                else if(currentQuestion.isCorrectAnswer(answer)){
                    return true;
                }
                else
                    return false;
            }

        } catch (Exception e){}

        return true;
    }

    protected synchronized void highlightAnsweredButton(){
        try {
            Log.v("klikowalne", "3");
            if(isTimeOut){
                for(int i=0; i<4; i++)
                    answerButtons[i].setBackgroundColor(Color.RED);
                Thread.sleep(1000);
                for(int i=0; i<4; i++)
                    answerButtons[i].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
            }
            else if (currentQuestion.isCorrectAnswer(answer)) {
                answerButtons[clickedButtonNumber].setBackgroundColor(Color.GREEN);
                Thread.sleep(1000);
                answerButtons[clickedButtonNumber].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
            }
            else{
                answerButtons[clickedButtonNumber].setBackgroundColor(Color.RED);
                Thread.sleep(1000);
                answerButtons[clickedButtonNumber].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
            }
            clickedWhileNotMyTurn = false;
        } catch (InterruptedException e){}

    }

    protected void setButtonsClickable(boolean isClickable, boolean changeColors){

        for(int i=0; i<4; i++)
            answerButtons[i].setClickable(isClickable);
        if(changeColors) {
            for (int i = 0; i < 4; i++) {
                if (isClickable)
                    answerButtons[i].setBackgroundColor(context.getResources().getColor(R.color.answerButton));
                else
                    answerButtons[i].setBackgroundColor(context.getResources().getColor(R.color.inactiveAnswerButton));
            }
        }
        isClickableAnswerButtons = isClickable;


    }

    protected void changeClickedButtonColor(){
        answerButtons[clickedButtonNumber].setBackgroundColor(context.getResources().getColor(R.color.inactiveAnswerButton));
    }


    public void setAnswer(String answerFromUser, int clickedButtonNumber, boolean isMyTurn){
        Log.v("klikowalne", "1");
        answer = answerFromUser;
        isAnswered = true;
        this.clickedButtonNumber = clickedButtonNumber;
        if(!isMyTurn) {
            Log.v("klikowalne", "2");
            clickedWhileNotMyTurn = true;
        }
        setButtonsClickable(false, false);
        changeClickedButtonColor();
    }

    public void clearText(final TextView questionTextView){
        for(int i=0; i<4; i++) {
            answerButtons[i].setText("");
        }
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    questionTextView.setText("");
                } catch (Exception e){}
            }
        });
    }




    protected String getAnswer(){
        return answer;
    }

    protected boolean isAnswered(){
        return isAnswered;
    }

    protected void setAnswered(boolean isAnswered){
        this.isAnswered = isAnswered;
    }

    protected void setGameOver(boolean isGameOver){
        this.isGameOver = isGameOver;
    }
    protected boolean isGameOver(){
        return isGameOver;
    }

    protected void setNextQuestion(){
        numberOfCurrentQuestion++;
        currentQuestion = questions.get(numberOfCurrentQuestion);
        Log.v("ajajajaj ", "size " + String.valueOf(questions.size()));
        if(numberOfCurrentQuestion == questions.size()-1)
            lastQuestion = true;
    }

    protected Question getCurrentQuestion(){
        return currentQuestion;
    }

    protected boolean isClickableAnswerButtons(){
        return isClickableAnswerButtons;
    }



}
