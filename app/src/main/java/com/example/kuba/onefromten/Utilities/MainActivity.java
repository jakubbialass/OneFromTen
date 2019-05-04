package com.example.kuba.onefromten.Utilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuba.onefromten.Fragments.SettingsFragment;
import com.example.kuba.onefromten.Fragments.SoloGame;
import com.example.kuba.onefromten.Fragments.ThreeMenGame;
import com.example.kuba.onefromten.R;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private TextView settingsButton;
    private Fragment settingsFragment, soloFragment, threeMenFragment;
    private String selectedFragment;
    private Button soloGameButton, threeMenButton;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiate();






    }






    private void openSettings(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder_main, settingsFragment, selectedFragment).addToBackStack("Settings");
        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder_main, fragment, selectedFragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(String fragmentName){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentName);
        fragmentTransaction.remove(fragment);
    }

    private void initiate(){
        selectedFragment = "";
        settingsFragment = new SettingsFragment();
        soloFragment = new SoloGame();
        threeMenFragment = new ThreeMenGame();
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });
        soloGameButton = findViewById(R.id.solo_button);
        soloGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = "Solo";
                replaceFragment(soloFragment);
            }
        });
        myDb = new DatabaseHelper(this);
        threeMenButton = findViewById(R.id.three_button);
        threeMenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // myDb.insertData("Jakie miasto jest stolicą Polski ?", "Nie wiem", "Kraków", "Wrocław", "Żary");
                selectedFragment = "Three";
                replaceFragment(threeMenFragment);
            }
        });
        FirebaseApp.initializeApp(this);



    }



}
