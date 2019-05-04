package com.example.kuba.onefromten.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kuba.onefromten.Controllers.OldGameController;
import com.example.kuba.onefromten.Controllers.SoloGameController;
import com.example.kuba.onefromten.Question;
import com.example.kuba.onefromten.R;
import com.example.kuba.onefromten.Utilities.DatabaseHelper;
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

public class SoloGame extends Fragment {

    DatabaseHelper myDb;
    Button answer1, answer2, answer3, answer4;
    Button[] buttons;
    private Button mainMenuButton, playAgainButton;
    TextView questionTextView;
    LinearLayout gameOverLinearLayout;
    ArrayList<Question> questions;
    DatabaseReference mQuestionsReference;
    OldGameController oldGameController;
    SoloGameController soloGameController;
    private Fragment thisFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_solo_game, container, false);

        thisFragment = this;
        initialize(view);
        //showQuestion();
        getQuestions(6);
        //oldGameController = new OldGameController(questions, buttons, questionTextView);
        //oldGameController.start();


        return view;
    }






    public void showQuestion(){
        Cursor result = myDb.getQuestions();
        if(result.getCount() == 0){
            Log.v("SoloGameFragment", "no data in database");
            return;
        }

        result.moveToPosition(1);
        questionTextView.setText(result.getString(1));
        answer1.setText(result.getString(2));
        answer2.setText(result.getString(3));
        answer3.setText(result.getString(4));
        answer4.setText(result.getString(5));


        /*
        StringBuffer buffer = new StringBuffer();
        while(result.moveToNext()){
            buffer.append("Id: " + result.getString(0)+ "\n");
        }
        */
    }

    private void initialize(View view){
        questionTextView = view.findViewById(R.id.question);
        answer1 = view.findViewById(R.id.answer_a_button);
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloGameController.setAnswer(answer1.getText().toString(), 0);
            }
        });
        answer2 = view.findViewById(R.id.answer_b_button);
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloGameController.setAnswer(answer2.getText().toString(), 1);
            }
        });
        answer3 = view.findViewById(R.id.answer_c_button);
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloGameController.setAnswer(answer3.getText().toString(), 2);
            }
        });
        answer4 = view.findViewById(R.id.answer_d_button);
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloGameController.setAnswer(answer4.getText().toString(), 3);
            }
        });
        buttons = new Button[4];
        buttons[0] = answer1;
        buttons[1] = answer2;
        buttons[2] = answer3;
        buttons[3] = answer4;
        for(int i=0; i<4; i++) {
            buttons[i].setText("");
        }
        myDb = new DatabaseHelper(getContext());
        //----firebase----
        FirebaseApp.initializeApp(view.getContext());
        mQuestionsReference = FirebaseDatabase.getInstance().getReference().child("questions");
        questions = new ArrayList<>();
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
                //getActivity().getSupportFragmentManager().beginTransaction().remove(thisFragment).commit();
                gameOverLinearLayout.setVisibility(View.GONE);
                Fragment newGame = new SoloGame();
                replaceFragment(newGame, "Solo");
            }
        });



        //mQuestionsReference.addListenerForSingleValueEvent();
    }


    private void getQuestions(final int questionsQuantity){
        questions.clear();
        questionTextView.setText("");

        Log.v("warmungs " , " da ");
        mQuestionsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                Log.v("warmungs " , " d ");
                Random random = new Random();
                int questionsCount = (int) snapshot.getChildrenCount();
                Set<Integer> numbers = new HashSet<>();
                while(numbers.size()!=questionsQuantity){
                    numbers.add(random.nextInt(questionsCount)+1);
                    Log.v("lugola " , numbers.toString());
                }
                for (Integer number : numbers) {
                    Log.v("warmung " , number.toString());
                    Log.v("warmungs " , " d ");
                    Question question = snapshot.child(number.toString()).getValue(Question.class);
                    questions.add(question);
                }
                for(int i=0; i<questionsQuantity; i++){
                    //Log.v("myfuckinlogs ", "qQantity"+questionsQuantity+"numbers"+numbers.size()+"questions"+questions.size()+"IIi="+i+"number[0]="+numbers.iterator().next() );
                    Log.v("Question " + i + ": ", questions.get(i).getQuestion());
                    Log.v("anonswer " + i + ": ", String.valueOf(questions.get(i)));
                    Log.v("anonswer " + i + ": ", String.valueOf(questions.get(i).getId()));
                    Log.v("anonswer " + i + ": ", String.valueOf(questions.get(i).getOneAnswer()));
                }
                //oldGameController = new OldGameController(getContext(), questions, buttons, questionTextView, gameOverLinearLayout, getActivity());
                //oldGameController.start();
                soloGameController = new SoloGameController(getContext(), questions, buttons, questionTextView, gameOverLinearLayout, getActivity());
                soloGameController.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void replaceFragment(Fragment fragment, String fragmentName){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder_main, fragment, fragmentName);
        fragmentTransaction.commit();
    }





}
