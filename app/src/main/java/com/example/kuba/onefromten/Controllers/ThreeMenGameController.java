package com.example.kuba.onefromten.Controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.Utilities.ArtificialIntelligence;

import java.util.ArrayList;

public class ThreeMenGameController extends GameController {

    private Activity myActivity;
    private LinearLayout gameOverLinearLayout;
    private TextView questionTextView;
    private boolean myTurn;
    private String myName;
    private ArrayList<String> playersList;
    private String currentPlayer;
    private ArtificialIntelligence[] ais;
    private Button[] answerButtons, playersButtons;
    private boolean pointing;


    public ThreeMenGameController(Context context, ArrayList<Question> questions, Button[] answerButtons, Button[] playersButtons, TextView questionTextView, LinearLayout gameOverLinearLayout, Activity myActivity){

        this.myActivity = myActivity;
        this.gameOverLinearLayout = gameOverLinearLayout;
        this.questionTextView = questionTextView;
        this.answerButtons = answerButtons;
        initializeVariables(context, questions, answerButtons, 1500, 300, 10, 700, myActivity);

        myTurn = true;
        myName = "Jakub";
        currentPlayer = myName;
        pointing = false;

        this.playersButtons = playersButtons;

        playersList = new ArrayList<>(3);
        playersList.add("Zbyszek");
        playersList.add("Kasia");
        playersList.add( myName);


        for(int i=0; i<3; i++) {
            playersButtons[i].setText(playersList.get(i));
        }

        ais = new ArtificialIntelligence[2];
        for(int i = 0; i<2; i++){
            ais[i] = new ArtificialIntelligence(playersList.get(i), ArtificialIntelligence.Difficulty.HARD, playersList);
            ais[i].start();
        }




    }



    public void run(){
        //Log.v("womtek", "zaczynam " + String.valueOf(isGameOver));
        try {
            while (!isGameOver()) {
                Log.v("Jestemtu", "1");
                if (currentPlayer.equals("Jakub"))
                    myTurn = true;
                clearText(questionTextView);

                showQuestion(questionTextView);

                showAnswers();


                for (ArtificialIntelligence ai : ais) {

                    ai.setCurrentQuestion(getCurrentQuestion());
                    ai.setCanAnswer(true);

                    //Log.v("Jestemtu", "na imie mi " + ai.getAiName());
                    if (currentPlayer.equals(ai.getAiName())) {

                        ai.setMyTurn(true);
                       // Log.v("Jestemtu", "drugie setnal");
                    }
                }

                if (myTurn) {
                    waitForAnswer();
                    Log.v("Jestemtu", "3");
                }
                else
                    waitForAiAnswer();

                highlightAnsweredButton();

                if (!isAnswerCorrect(getAnswer())) {
                    if (myTurn)
                        setGameOver(true);
                }
                else {
                    if(myTurn) {
                        Log.v("Jestemtu", "4");
                        pointing = true;
                        pointAt();
                    }
                    setNextQuestion();
                    Log.v("Jestemtu", "5");
                }

            }

            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameOverLinearLayout.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e){}

    }
    

    private void pointAt(){
        try {

            for(int i=0; i<3; i++){
                playersButtons[i].setClickable(true);
            }

            while (pointing) {
                Log.v("Jestemtu", "pointing");
                Thread.sleep(200);
            }
        } catch (InterruptedException e){}
    }

    public void pointedAt(int player){
        pointing = false;
        currentPlayer = playersButtons[player].getText().toString();
        if(!currentPlayer.equals(myName))
            myTurn = false;
        Log.v("Jestemtu", "current player: " + currentPlayer);

    }


    /* to jest w miare git*/
    public void waitForAiAnswer(){
        Log.v("-----jestemai", " wszedlem waitForAiAnswer ");
        //Log.v("-----jestemai", currentPlayer + " " + ais[0].getAiName());
        if (currentPlayer.equals(ais[0].getAiName())){
            Log.v("-----jestemai", " curent " + currentPlayer);
            for(int i=0; i<4; i++){
                answerButtons[i].setClickable(true);
            }
            while(ais[0].getCanAnswer()){
                //nothing, just wait
            }
            //zablokowac przyciski
            for(int i=0; i<4; i++){
                answerButtons[i].setClickable(false);
            }
            while(ais[0].isPointing()){
                //nothing, just wait
            }

            // tutaj bedzie trzeba zmienic zeby wybieralo losowo playera albo kolejnego czy cos
            if (ais[0].getPointedAt()!=null) {
                currentPlayer = ais[0].getPointedAt();
               // if(currentPlayer.equals("Zbyszek"))
               //     ais[1].setMyTurn(true);
                Log.v("-----jestemai", " cos tu jest nie tak 1 " + currentPlayer);
            }
            else {
                currentPlayer = "Jakub";
                //ais[0].setMyTurn(false);
                Log.v("-----jestemai " , " teraz ruch Jakuba");
                Log.v("-----jestemai", " cos tu jest nie tak 1 " + currentPlayer);
            }


        }else if(currentPlayer.equals(ais[1].getAiName())) {
            Log.v("-----jestemai", " curent " + currentPlayer);
            for(int i=0; i<4; i++){
                answerButtons[i].setClickable(true);
            }
            while(ais[1].getCanAnswer()){
                //nothing, just wait
            }
            //zablokowac przyciski
            for(int i=0; i<4; i++){
                answerButtons[i].setClickable(false);
            }
            while(ais[1].isPointing()){
                //nothing, just wait
            }
            //Log.v("-----jestemai", " przed " + currentPlayer);
            if (ais[1].getPointedAt()!=null) {
                currentPlayer = ais[1].getPointedAt();
                //if(currentPlayer.equals("Kasia"))
                //    ais[0].setMyTurn(true);
            }
            else {
                //ais[1].setMyTurn(false);
                currentPlayer = "Jakub";
                Log.v("-----jestemai " , " teraz ruch Jakuba");
            }

        }
    }















}
