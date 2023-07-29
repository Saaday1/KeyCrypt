package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Button getStartedButton, loginTextView;
    private ImageView btnGoogle;
    private RelativeLayout loginButtons;
    private static final int RC_SIGN_IN = 123; // Request code for Google Sign-In
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private FirebaseFirestore mFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the navigation bar and make it full-screen
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);

        getStartedButton = findViewById(R.id.getStartedButton);
        loginTextView = findViewById(R.id.loginTextView);
        loginButtons = findViewById(R.id.loginButtons);
        btnGoogle = loginButtons.findViewById(R.id.btnGoogle);
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBarMain);

        progressBar.setVisibility(View.INVISIBLE);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Get Started" button click
                // Start the registration or sign-up activity
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle "Log In" text click
                // Start the login activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        // Initialize GoogleSignInOptions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build the GoogleSignInClient with the options above.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                Log.d("MainActivity","onActivityResult");
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    // Handle the Google Sign-In result in onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Sign-in failed
            // Handle the error, e.g., display a toast or dialog
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, user authenticated with Firebase
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Get the user's name
                        String userName = user.getDisplayName();

                        // Get the user's email address
                        String userEmail = user.getEmail();

                        // Get the user's profile photo URL (if available)
                        Uri userProfilePhotoUrl = user.getPhotoUrl();

                        // Check if the user already exists in Firebase Authentication
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();

                        if (isNewUser) {
                            // User is new, create a new user in Firestore and store the data
                            createUserInFirestore(user.getUid(), userName, userEmail, userProfilePhotoUrl);
                        } else {
                            // User already exists, update the user's profile data
                            updateUserDataInFirestore(user.getUid(), userName, userEmail, userProfilePhotoUrl);
                        }
                        showToast("Welcome to Key Crypt");
                        // Proceed to the main part of your app
                        startActivity(new Intent(MainActivity.this, MainScreen.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    } else {
                        // Sign-in failed
                        // Handle the error, e.g., display a toast or dialog
                    }
                });
    }

    private void createUserInFirestore(String userId, String userName, String userEmail, Uri userProfilePhotoUrl) {
        // Create a new user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", userName);
        userData.put("email", userEmail);

        // If the user has a profile photo URL, store it in Firestore
        if (userProfilePhotoUrl != null) {
            userData.put("profilePhotoUrl", userProfilePhotoUrl.toString());
        }

        // Store user data in Firestore
        mFirestore.collection("Users").document(userId)
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User data stored in Firestore successfully
                        // You can add any additional logic here, if needed
                    } else {
                        // Failed to store user data in Firestore
                        // Handle the error, e.g., display a toast or dialog
                    }
                });
    }

    private void updateUserDataInFirestore(String userId, String userName, String userEmail, Uri userProfilePhotoUrl) {
        // Create a new user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", userName);
        userData.put("email", userEmail);

        // If the user has a profile photo URL, update it in Firestore
        if (userProfilePhotoUrl != null) {
            userData.put("profilePhotoUrl", userProfilePhotoUrl.toString());
        }

        // Update user data in Firestore
        mFirestore.collection("Users").document(userId)
                .update(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User data updated in Firestore successfully
                        // You can add any additional logic here, if needed
                    } else {
                        // Failed to update user data in Firestore
                        // Handle the error, e.g., display a toast or dialog
                    }
                });
    }


    public void showToast(String value){
        // Java
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(value);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}