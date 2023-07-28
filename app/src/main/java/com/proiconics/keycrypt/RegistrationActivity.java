package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proiconics.keycrypt.R;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    Button btnLogin, signup;
    private EditText etEmail, etPassword, etName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnLogin = findViewById(R.id.signUpButton);
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etName = findViewById(R.id.nameEditText);
        Button btnSignUp = findViewById(R.id.createAccountButton);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);


        // Hide the navigation bar and make it full-screen
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpWithEmailAndPassword();
            }
        });
    }
    private void signUpWithEmailAndPassword() {
        progressBar.setVisibility(View.VISIBLE);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        // Check for valid email address
        if (!isValidEmail(email)) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for strong password (minimum 6 characters)
        if (!isValidPassword(password)) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up success, user account created
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Get the user ID (UID)
                            String userId = user.getUid();

                            // Store additional user data in Firestore
                            storeUserDataInFirestore(userId, email,name);

                            // Send email verification
                            sendEmailVerification(user);
                        }
                    } else {
                        // Sign-up failed
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("SignUpActivity", "Sign-up Failed: " + task.getException());
                        Toast.makeText(this, "Sign-up failed. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(CharSequence email) {
        // Use Android's built-in email pattern matcher
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Check if password is at least 6 characters long
        return password.length() >= 6;
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Email verification sent successfully
                        // Notify the user to check their email for verification link
                        startActivity(new Intent(RegistrationActivity.this, Emailverification.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    } else {
                        // Failed to send verification email
                        // Handle the error, e.g., display a toast or dialog
                    }
                });
    }
    private void storeUserDataInFirestore(String userId, String email,String name) {
        // Create a new user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name",name);

        // Store user data in Firestore
        mFirestore.collection("Users").document(userId)
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User data stored in Firestore successfully
                        // Proceed to the main part of your app
                    } else {
                        // Failed to store user data in Firestore
                        // Handle the error, e.g., display a toast or dialog
                    }
                });
    }
}