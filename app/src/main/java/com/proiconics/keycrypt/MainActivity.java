package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {
    private Button getStartedButton, loginTextView;
    private ImageView btnGoogle;
    private RelativeLayout loginButtons;
    private static final int RC_SIGN_IN = 123;
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
            }
        });

    }

    // Handle the Google Sign-In result in onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity","onActivityResult, requestcode :"+requestCode);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, now you can use the account information
            // You can extract user information like display name, email, profile photo, etc.
            startActivity(new Intent(MainActivity.this,MainScreen.class));
        } catch (ApiException e) {
            // Sign-in failed
            // Handle the error, e.g., display a toast or dialog
        }
    }

}