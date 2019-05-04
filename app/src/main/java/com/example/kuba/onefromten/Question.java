package com.example.kuba.onefromten;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Question {

    private String question, correctAnswer, answer2, answer3, answer4;

    private int id;



    public Question(int id, String question, String correctAnswer, String answer2, String answer3, String answer4) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    public Question(){

    }

    public String getId(){
        return String.valueOf(id);
    }

    public String getQuestion(){
        Log.v("anonswerpytanie", question);
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getOneAnswer(){
        Log.v("anonswerpytanie", String.valueOf(correctAnswer));
        return correctAnswer;
    }



    public ArrayList<String> getAnswers(){
        ArrayList<String> answers = new ArrayList<>(5);
        answers.add(correctAnswer);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        //Log.v("walnesetuhehehohoho ", answer_2);
        Collections.shuffle(answers);
        return answers;
    }

    public boolean isCorrectAnswer(String answer){
        if(answer.equals(correctAnswer))
            return true;
        else
            return false;
    }

    public String getWrongAnswer(){
        String wrongAnswer = "";
        ArrayList<String> answers = new ArrayList<>(3);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        Collections.shuffle(answers);
        wrongAnswer = answers.get(0);
        return wrongAnswer;
    }





}
