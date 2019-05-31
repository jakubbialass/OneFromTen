package com.example.kuba.onefromten.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Controllers.OldGameController;
import com.example.kuba.onefromten.Controllers.ThreeMenGameController;
import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ThreeMenGame extends Fragment {

    private Fragment thisFragment;
    private ArrayList<Question> questions;
    private TextView questionTextView;
    private DatabaseReference mQuestionsReference;
    private Button mainMenuButton, playAgainButton;
    private Button answer1, answer2, answer3, answer4;
    private Button[] buttons, playersButtons;
    private TextView[][] playersTextViews;
    private LinearLayout gameOverLinearLayout;
    private ThreeMenGameController threeMenGameController;
    private TextView timer;
    private TextView narrator;

    private static final int QUESTIONS_QUANTITY_THIS_MODE = 8;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_three_men_game, container, false);
        thisFragment = this;

        initialize(view);

        getQuestions(QUESTIONS_QUANTITY_THIS_MODE, view);


        return view;
    }



    public ThreeMenGame(){

    }



    private void initialize(View view){

        questions = new ArrayList<>(QUESTIONS_QUANTITY_THIS_MODE);
        questionTextView = view.findViewById(R.id.question);
        answer1 = view.findViewById(R.id.answer_a_button);
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.setAnswer(answer1.getText().toString(), 0, threeMenGameController.isMyTurn());
            }
        });
        answer2 = view.findViewById(R.id.answer_b_button);
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.setAnswer(answer2.getText().toString(), 1, threeMenGameController.isMyTurn());
            }
        });
        answer3 = view.findViewById(R.id.answer_c_button);
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.setAnswer(answer3.getText().toString(), 2, threeMenGameController.isMyTurn());
            }
        });
        answer4 = view.findViewById(R.id.answer_d_button);
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.setAnswer(answer4.getText().toString(), 3, threeMenGameController.isMyTurn());
            }
        });
        buttons = new Button[4];
        buttons[0] = answer1;
        buttons[1] = answer2;
        buttons[2] = answer3;
        buttons[3] = answer4;
        playersButtons = new Button[3];
        playersButtons[0] = view.findViewById(R.id.player1);
        playersButtons[1] = view.findViewById(R.id.player2);
        playersButtons[2] = view.findViewById(R.id.player3);
        playersButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.pointedAt(0);
            }
        });
        playersButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.pointedAt(1);
            }
        });
        playersButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeMenGameController.pointedAt(2);
            }
        });
        timer = view.findViewById(R.id.seconds_left);


        narrator = view.findViewById(R.id.narrator);


        for(int i=0; i<4; i++) {
            buttons[i].setText("");
        }
        //myDb = new DatabaseHelper(getContext());

        //----firebase----
        FirebaseApp.initializeApp(view.getContext());
        mQuestionsReference = FirebaseDatabase.getInstance().getReference().child("questions");
        gameOverLinearLayout = view.findViewById(R.id.game_over);

        mainMenuButton = view.findViewById(R.id.main_menu_button);
        playAgainButton = view.findViewById(R.id.play_again_button);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(thisFragment).commit();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameOverLinearLayout.setVisibility(View.GONE);
                Fragment newGame = new ThreeMenGame();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_placeholder_main, newGame, "Three");
                fragmentTransaction.commit();
            }
        });

        playersTextViews = new TextView[3][3];

       // for(int i=0; i<3; i++){
        //    playersTextViews[i] = new TextView[3];
       // }
        playersTextViews[0][0] = view.findViewById(R.id.player1_name);
        playersTextViews[0][1] = view.findViewById(R.id.player1_score);
        playersTextViews[0][2] = view.findViewById(R.id.player1_action);

        playersTextViews[1][0] = view.findViewById(R.id.player2_name);
        playersTextViews[1][1] = view.findViewById(R.id.player2_score);
        playersTextViews[1][2] = view.findViewById(R.id.player2_action);

        playersTextViews[2][0] = view.findViewById(R.id.player3_name);
        playersTextViews[2][1] = view.findViewById(R.id.player3_score);
        playersTextViews[2][2] = view.findViewById(R.id.player3_action);

    }


    private void getQuestions(final int questionsQuantity, View view){
        questions.clear();
        clearText(view);
        mQuestionsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Random random = new Random();
                int questionsCount = (int) snapshot.getChildrenCount();
                Set<Integer> numbers = new HashSet<>();

                while(numbers.size()!=questionsQuantity){
                    numbers.add(random.nextInt(questionsCount)+1);
                }

                for (Integer number : numbers) {
                    Question question = snapshot.child(number.toString()).getValue(Question.class);
                    questions.add(question);
                }

                threeMenGameController = new ThreeMenGameController(getContext(), questions, buttons, playersButtons, questionTextView, gameOverLinearLayout, getActivity(), playersTextViews, timer, narrator);
                threeMenGameController.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void clearText(View view){
        questionTextView.setText("");
        for(Button button : buttons){
            button.setText("");
        }
        for(Button button : playersButtons){
            button.setText("");
        }
        timer.setText("");
        narrator.setText("Ładuję pytania");

    }






}
