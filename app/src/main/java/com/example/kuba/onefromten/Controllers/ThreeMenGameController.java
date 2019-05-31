package com.example.kuba.onefromten.Controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Fragments.ThreeMenGame;
import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.Utilities.ArtificialIntelligence;

import org.w3c.dom.Text;

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
    private TextView[][] playersTextViews;
    private TextView narrator;
    private String narratorText;



    public ThreeMenGameController(Context context, ArrayList<Question> questions, Button[] answerButtons, Button[] playersButtons,
                                  TextView questionTextView, LinearLayout gameOverLinearLayout, Activity myActivity, TextView[][] playersTextViews, TextView timer, TextView narrator){

        this.myActivity = myActivity;
        this.gameOverLinearLayout = gameOverLinearLayout;
        this.questionTextView = questionTextView;
        this.answerButtons = answerButtons;
        this.playersTextViews = playersTextViews;
        this.timer = timer;
        this.narrator = narrator;

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
            TextView[] textView = new TextView[3];
            textView = playersTextViews[i];
            ais[i] = new ArtificialIntelligence(playersList.get(i), ArtificialIntelligence.Difficulty.HARD, playersList, myActivity, textView);
            ais[i].start();
        }




    }



    public void run(){
        try {
            while (!isGameOver()) {

                Log.v("ajajajaj ", "1");
                if(lastQuestion) {
                    setGameOver(true);
                    for (ArtificialIntelligence ai : ais)
                        ai.isLastQuestion(true);
                }
                Log.v("ajajajaj ", "2");
                if (currentPlayer.equals("Jakub"))
                    myTurn = true;
                clearText(questionTextView);

                narratorText = "Pytanie " + (numberOfCurrentQuestion+1) + "/" + questions.size();
                changeNarratorText(narratorText);

                showQuestion(questionTextView);

                Log.v("ajajajaj ", "3");

                showAnswers();

                setButtonsClickable(true, true);

                for (ArtificialIntelligence ai : ais) {
                    ai.setCurrentQuestion(getCurrentQuestion());
                    if (currentPlayer.equals(ai.getAiName()))
                        ai.setMyTurn(true);
                    ai.setCanAnswer(true);
                }

                narratorText = "Odpowiada " + currentPlayer;
                changeNarratorText(narratorText);

                Log.v("ajajajaj ", "4");
                if (myTurn) {
                    Log.v("ajajajaj ", "4a");
                    waitForAnswer();
                }
                else
                    waitForAiAnswer();
                Log.v("ajajajaj ", "5");
                if(myTurn || clickedWhileNotMyTurn)
                    highlightAnsweredButton();
                Log.v("ajajajaj ", "6");
                if (!isAnswerCorrect(getAnswer())) {
                    if (myTurn)
                        setGameOver(true);
                    Log.v("ajajajaj ", "7a");
                }
                else {
                    Log.v("ajajajaj ", "7b");
                    if (!lastQuestion) {
                        if (myTurn) {
                            narratorText = "Wskazuje " + currentPlayer;
                            changeNarratorText(narratorText);
                            pointing = true;
                            pointAt();
                            Thread.sleep(1500);
                        }
                        setNextQuestion();
                    }
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
                Thread.sleep(200);
            }
        } catch (InterruptedException e){}
    }

    public void pointedAt(int player){
        pointing = false;
        narratorText = "Gracz " + currentPlayer + " wskazał gracza " + playersButtons[player].getText().toString();
        changeNarratorText(narratorText);
        currentPlayer = playersButtons[player].getText().toString();
        if(!currentPlayer.equals(myName))
            myTurn = false;
    }



    public void waitForAiAnswer(){
        try {
            if (currentPlayer.equals(ais[0].getAiName())) {
                for (int i = 0; i < 4; i++) {
                    answerButtons[i].setClickable(true);
                }
                while (ais[0].getCanAnswer()) {
                    //nothing, just wait
                    Log.v("ajajajaj ", "cananswer");
                }
                //zablokowac przyciski
                if (!myTurn && !clickedWhileNotMyTurn)
                    setButtonsClickable(false, true);

                boolean doItOnce = true;
                while (ais[0].isPointing()) {
                    //nothing, just wait
                    if(doItOnce){
                        narratorText = "Wskazuje " + currentPlayer ;
                        changeNarratorText(narratorText);
                        doItOnce = false;
                    }
                    Log.v("ajajajaj ", "is pointing");
                }
                Thread.sleep(300);
                Log.v("ajajajaj ", "getPointedAt " + ais[0].getPointedAt());
                if (ais[0].getPointedAt() != null) {
                    if(!doItOnce){
                        narratorText = "Gracz " + currentPlayer + " wskazał gracza " + ais[0].getPointedAt();
                        changeNarratorText(narratorText);
                        Thread.sleep(1500);
                    }
                    currentPlayer = ais[0].getPointedAt();
                } else {
                    currentPlayer = "Jakub";

                }


            } else if (currentPlayer.equals(ais[1].getAiName())) {
                for (int i = 0; i < 4; i++) {
                    answerButtons[i].setClickable(true);
                }
                while (ais[1].getCanAnswer()) {
                    //nothing, just wait
                }
                //zablokowac przyciski
                if (!myTurn && !clickedWhileNotMyTurn)
                    setButtonsClickable(false, true);
                boolean doItOnce = true;
                while (ais[1].isPointing()) {
                    //nothing, just wait
                    if(doItOnce){
                        narratorText = "Wskazuje " + currentPlayer ;
                        changeNarratorText(narratorText);
                        doItOnce = false;
                    }
                }
                Thread.sleep(300);
                Log.v("ajajajaj ", "getPointedAt " + ais[1].getPointedAt());
                if (ais[1].getPointedAt() != null) {
                    narratorText = "Gracz " + currentPlayer + " wskazał gracza " + ais[1].getPointedAt();
                    changeNarratorText(narratorText);
                    Thread.sleep(1500);
                    currentPlayer = ais[1].getPointedAt();
                } else {
                    currentPlayer = "Jakub";
                }

            }
        }catch (Exception e){}
    }








    private void changeText(final TextView textView, final String text){
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    private void changeNarratorText(final String text){
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                narrator.setText(text);
            }
        });
    }

    public ArrayList<String> getPlayersList(){
        return playersList;
    }


    public TextView[] getPlayersTextViews(String playerName){
        TextView[] textView = new TextView[playersList.size()];
        int playerNumber=0;
        for(String player : playersList){
            if(!playerName.equals(player))
                playerNumber++;
        }
        for(int i=0; i<3; i++){
            textView[i] = playersTextViews[playerNumber][i];
        }

        return textView;
    }

    public boolean isMyTurn(){
        return myTurn;
    }







}
