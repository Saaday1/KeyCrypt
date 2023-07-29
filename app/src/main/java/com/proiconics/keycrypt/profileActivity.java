package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proiconics.keycrypt.R;

public class profileActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Button btnSignout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnSignout = findViewById(R.id.SignoutButton);

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
        mAuth = FirebaseAuth.getInstance();

        // Configure Google sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your web client ID
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

    }
    public void SignOut() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            String providerId = currentUser.getProviderId();

            if (providerId.equals("firebase")) {
                // User is signed in with email/password
                signOutWithEmailAndPassword();
            } else if (providerId.equals("google.com")) {
                // User is signed in with Google
                signOutWithGoogle();
            }
        } else {
            // User is not signed in
            // Handle the situation where no user is logged in (e.g., show a message)
        }
    }

    private void signOutWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            // Sign-out is complete
            // Proceed with any additional actions after sign-out (e.g., navigate to the login screen)
            startActivity(new Intent(profileActivity.this,MainActivity.class));
            showToast("Signed Out!");
        });
    }

    private void signOutWithEmailAndPassword() {
        mAuth.signOut();
        // Sign-out is complete
        // Proceed with any additional actions after sign-out (e.g., navigate to the login screen)
        startActivity(new Intent(profileActivity.this,MainActivity.class));
        showToast("Signed Out!");
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