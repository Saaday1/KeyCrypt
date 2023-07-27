package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.proiconics.keycrypt.R;

public class profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create an instance of the GeneralInfoFragment
        GeneralInfoFragment generalInfoFragment = new GeneralInfoFragment();

        // Add the GeneralInfoFragment to the fragmentContainer
        fragmentTransaction.add(R.id.fragmentContainer, generalInfoFragment);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();


        RadioGroup switchButtonGroup = findViewById(R.id.buttonSliderGroup);
        switchButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.generalButton) {
                    // Show the General Info fragment
                    GeneralInfoFragment generalInfoFragment = new GeneralInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, generalInfoFragment)
                            .commit();
                } else if (checkedId == R.id.personalButton) {
                    // Show the Personal Info fragment
                    PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, personalInfoFragment)
                            .commit();
                }
            }
        });

    }
}