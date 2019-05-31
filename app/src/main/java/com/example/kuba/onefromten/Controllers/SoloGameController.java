package com.example.kuba.onefromten.Controllers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Question;

import java.util.ArrayList;

public class SoloGameController extends GameController {

    private TextView questionTextView;
    private Activity myActivity;
    private LinearLayout gameOverLinearLayout;

    public SoloGameController(Context context, ArrayList<Question> questions, Button[] answerButtons, TextView questionTextView, LinearLayout gameOverLinearLayout,  Activity myActivity, TextView timer){

        this.myActivity = myActivity;
        this.gameOverLinearLayout = gameOverLinearLayout;
        this.questionTextView = questionTextView;
        this.timer = timer;
        initializeVariables(context, questions, answerButtons, 1500, 300, 10, 700, myActivity);

    }



    public void run(){

        while(!isGameOver()) {

            clearText(questionTextView);

            showQuestion(questionTextView);

            showAnswers();

            waitForAnswer();

            highlightAnsweredButton();

            if(!isAnswerCorrect(getAnswer()))
                setGameOver(true);
            else
                setNextQuestion();

        }

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameOverLinearLayout.setVisibility(View.VISIBLE);
            }
        });

    }

}
