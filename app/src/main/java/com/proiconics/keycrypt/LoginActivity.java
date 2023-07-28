package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proiconics.keycrypt.R;

public class LoginActivity extends AppCompatActivity {

    Button btnSignUp, btnLogin;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = findViewById(R.id.signUpButton);
        btnLogin = findViewById(R.id.logInButton);
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);

        progressBar = findViewById(R.id.progressBarLogin);

        progressBar.setVisibility(View.INVISIBLE);



        // Hide the navigation bar and make it full-screen
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword();
            }
        });


    }
    private void signInWithEmailAndPassword() {
        progressBar.setVisibility(View.VISIBLE);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Check if email or password is empty
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, user authenticated
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null && user.isEmailVerified()) {
                            // Proceed to the main part of your app
                            startActivity(new Intent(LoginActivity.this, MainScreen.class));
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        } else {
                            // Email not verified, show a toast or alert dialog
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Sign-in failed
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("LoginActivity", "Login Failed");
                        Toast.makeText(this, "Email or Password is incorrect.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}