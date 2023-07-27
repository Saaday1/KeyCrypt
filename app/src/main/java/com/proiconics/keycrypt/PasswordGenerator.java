package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.proiconics.keycrypt.R;
import com.google.android.material.slider.RangeSlider;

public class PasswordGenerator extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);

        // Get the capsSeekBar reference
        RangeSlider capsSeekBar = findViewById(R.id.capsSeekBar);
        RangeSlider digitsSeekBar = findViewById(R.id.digitsSeekBar);
        RangeSlider specialSeekBar = findViewById(R.id.specialSeekBar);


        // Set the default value of 50
        capsSeekBar.setValues(50f);
        digitsSeekBar.setValues(50f);
        specialSeekBar.setValues(50f);
    }
}