package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        // Delay the splash screen for 2 seconds (adjust the delay as needed)
        new Handler().postDelayed(() -> {
            // Check user authentication status
            checkUserAuthentication();
        }, 2000); // 2-second delay

    }
    private void checkUserAuthentication() {
        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to MainScreen
            startActivity(new Intent(SplashScreen.this, MainScreen.class));
            finish();
        } else {
            // User is not logged in, navigate to SignUpActivity
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}