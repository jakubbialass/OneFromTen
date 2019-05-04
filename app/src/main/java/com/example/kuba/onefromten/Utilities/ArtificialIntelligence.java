package com.example.kuba.onefromten.Utilities;

import android.util.Log;

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




    public ArtificialIntelligence(String name, Difficulty difficulty, ArrayList<String> playersList){

        this.name = name;
        this.difficulty = difficulty;
        this.playersList = playersList;

        this.answer = "";
        this.isMyTurn = false;
        this.lives = 3;
        this.canAnswer = false;
        this.score = 0;
        this.pointing = false;
        this.pointedAt = null;
    }


    public void run(){
        try{
            while(lives >0){

                if(canAnswer) {
                    Log.v("-----jestemai " + name, " is my turn? " + isMyTurn);
                    answerTheQuestion(currentQuestion);

                    if (currentQuestion.isCorrectAnswer(answer)){
                        score++;

                        if (isMyTurn) {
                            Thread.sleep(2000);
                            pointedAt = pointSomeoneToAnswer();
                        }
                        //pointing = false;
                        //canAnswer = false;

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

            if (difficulty == Difficulty.EASY) {
                int timeForAnswerInSeconds = generator.nextInt(10) +3;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber > 7)
                    isCorrectAnswer = true;
            } else if (difficulty == Difficulty.MEDIUM) {
                int timeForAnswerInSeconds = generator.nextInt(10) +2;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber > 5)
                    isCorrectAnswer = true;
            } else {
                int timeForAnswerInSeconds = generator.nextInt(10) +1;
                if(isMyTurn)
                    Thread.sleep(timeForAnswerInSeconds*1000);
                if(drawnNumber > 3)
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
            }
            Log.v("-----jestemai " + name, " odpowiedz jest " + String.valueOf(isCorrectAnswer) + ", is my turn? " + isMyTurn);
            pointedAt = null;
            canAnswer = false;
            Thread.sleep(500);

        } catch (InterruptedException e){}

    }




    private String pointSomeoneToAnswer(){
        Random generator = new Random();
        String playername = playersList.get(generator.nextInt(playersList.size()-1));
        Log.v("-----jestemai", " szmaciura " + name + " wskazala " + playername);
        if(!playername.equals(name))
            setMyTurn(false);
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


}
