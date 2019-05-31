package com.example.kuba.onefromten.Utilities;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.kuba.onefromten.Fragments.ThreeMenGame;
import com.example.kuba.onefromten.Question;

import java.util.ArrayList;
import java.util.Random;

public class ArtificialIntelligence extends Thread {

    public enum Difficulty {EASY, MEDIUM, HARD};


    private String name;
    private int score;
    private Difficulty difficulty;
    private String answer;
    private boolean isMyTurn;
    private Question currentQuestion;
    private int lives;
    private boolean canAnswer;
    private ArrayList<String> playersList;
    private String pointedAt;
    private boolean pointing;
    private Activity myActivity;
    private TextView[] playerTextView;
    private boolean lastQuestion;
    private boolean gameOver;




    public ArtificialIntelligence(String name, Difficulty difficulty, ArrayList<String> playersList, Activity myActivity, TextView[] playerTextView){

        this.name = name;
        this.difficulty = difficulty;
        this.playersList = playersList;
        this.myActivity = myActivity;
        this.playerTextView = playerTextView;
        initializePlayerTextViews();
        this.answer = "";
        this.isMyTurn = false;
        this.lives = 3;
        this.canAnswer = false;
        this.score = 0;
        this.pointing = false;
        this.pointedAt = null;
        this.gameOver = false;

    }


    public void run(){
        try{
            changeText(playerTextView[0], name);
            while(lives >0 && !gameOver){

                if(canAnswer) {
                    if (lastQuestion) {
                        gameOver = true;
                        Log.v("ajajajaj ", "zakonczylem");
                    }
                    Log.v("ajajajaj", name + " is my turn? " + isMyTurn);
                    Log.v("ajajajaj", name + " bedzie odpowiadal");
                    answerTheQuestion(currentQuestion);
                    Log.v("ajajajaj", name + " odpowiadal");

                    if (currentQuestion.isCorrectAnswer(answer)){
                        score++;
                        Log.v("ajajajaj ", "isMyTurn " + isMyTurn + "; isLastQuestion " + lastQuestion + "; isCanAnswer " + canAnswer);
                        if (isMyTurn && !lastQuestion) {
                            changeText(playerTextView[2], "pointing");
                            Thread.sleep(2000);
                            pointedAt = pointSomeoneToAnswer();
                            Log.v("ajajajaj ", "pointedAt " + pointedAt);
                        }
                        Log.v("ajajajaj ", name + " last prostaaa isCanAnswer " + canAnswer);
                    }
                    pointing = false;

                }

            }
        } catch (Exception e){}
    }




    public void answerTheQuestion(Question question){
        Random generator = new Random();
        boolean isCorrectAnswer = false;
        int drawnNumber = generator.nextInt(10) +1;
        try {
            if(isMyTurn)
                changeText(playerTextView[2], "answering");
            else
                changeText(playerTextView[2], "playing");
            if (difficulty == Difficulty.EASY) {
                int timeForAnswerInSeconds = generator.nextInt(10) +3;
                if (lastQuestion)
                    timeForAnswerInSeconds = 4;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber > 7)
                    isCorrectAnswer = true;
            } else if (difficulty == Difficulty.MEDIUM) {
                int timeForAnswerInSeconds = generator.nextInt(10) +2;
                if (lastQuestion)
                    timeForAnswerInSeconds = 4;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber > 5)
                    isCorrectAnswer = true;
            } else {
                int timeForAnswerInSeconds = generator.nextInt(10) +1;
                if (lastQuestion)
                    timeForAnswerInSeconds = 4;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber >= 0)
                    isCorrectAnswer = true;
            }
            if(isCorrectAnswer) {
                answer = question.getCorrectAnswer();
                if(isMyTurn) {
                    pointing = true;
                    Log.v("jestemai", " ustawilem pointing na true !!!");
                }
            }
            else {
                answer = question.getWrongAnswer();
                isMyTurn = false;
                changeText(playerTextView[2], "playing");
            }
            Log.v("-----jestemai " + name, " odpowiedz jest " + String.valueOf(isCorrectAnswer) + ", is my turn? " + isMyTurn);
            Log.v("ajajajaj ", "buuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
            pointedAt = null;
            canAnswer = false;
            Thread.sleep(500);

        } catch (InterruptedException e){}

    }




    private String pointSomeoneToAnswer(){
        Random generator = new Random();
        String playername = playersList.get(generator.nextInt(playersList.size()));
        Log.v("-----jestemai", " ai " + name + " wskazala " + playername);
        if(!playername.equals(name)) {
            changeText(playerTextView[2], "playing");
        }
        else
            changeText(playerTextView[2], "answering");
        isMyTurn = false;
        Log.v("ajajajaj ", playername);
        return playername;
    }











    public int getScore() {
        return score;
    }

    public void addScore(int scoreToAdd){
        this.score+=scoreToAdd;
    }

    public String getAnswer(){
        return answer;
    }


    public void setMyTurn(boolean is){

        isMyTurn = is;
        canAnswer = true;
    }

    public void setCurrentQuestion(Question question){
        currentQuestion = question;
    }

    public void setCanAnswer(boolean can){
        canAnswer = can;
    }

    public boolean getCanAnswer(){
        return canAnswer;
    }



    public String getPointedAt() {
        return pointedAt;
    }

    public boolean isPointing(){
        return pointing;
    }

    public String getAiName(){
        return name;
    }

    private void changeText(final TextView textView, final String text){
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    private void initializePlayerTextViews(){
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerTextView[0].setText(name);
                playerTextView[1].setText("0");
                playerTextView[2].setText("playing");
            }
        });
    }

    public void isLastQuestion(boolean lastQuestion){
        this.lastQuestion = lastQuestion;
    }


}
