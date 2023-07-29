package com.proiconics.keycrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SecurityCheck extends AppCompatActivity {

    private RecyclerView passwordsRecyclerView;
    private PasswordsAdapterS passwordsAdapter;
    private List<PasswordItem> passwordList;

    private TextView securityScoreValueTextView;
    private ProgressBar securityScoreProgressBar;
    private List<AppSecurityInfo> appSecurityInfoList;
    // Firestore instance
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Initialize views
        securityScoreValueTextView = findViewById(R.id.securityScoreValueTextView);
        securityScoreProgressBar = findViewById(R.id.securityScoreProgressBar);
        passwordsRecyclerView = findViewById(R.id.passwordsRecyclerView);

        // Initialize the RecyclerView and its adapter
        passwordList = new ArrayList<>();
        passwordsAdapter = new PasswordsAdapterS(passwordList);
        passwordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        passwordsRecyclerView.setAdapter(passwordsAdapter);


        // Fetch and populate data for the RecyclerView (app security information)
        fetchPasswordRecords();

    }
    private void fetchPasswordRecords() {
        // Get the current user's ID from your authentication system (e.g., Firebase Authentication)
        String userId = getCurrentUserId();

        // Query Firestore to get all password records for the current user
        mFirestore.collection("passwords")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        passwordList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PasswordItem passwordItem = document.toObject(PasswordItem.class);
                            passwordList.add(passwordItem);
                        }
                        // Notify the adapter that data has changed
                        passwordsAdapter.notifyDataSetChanged();
                        // Calculate the security score
                        int overallSecurityScore = calculateOverallSecurityScore(passwordList);

                        // Set the security score to the TextView
                        securityScoreValueTextView.setText(String.valueOf(overallSecurityScore));
                        // Set the security score to the ProgressBar
                        securityScoreProgressBar.setProgress(overallSecurityScore);
                        // Set the progress bar color based on the security score
                        int progressBarColor = getProgressBarColor(overallSecurityScore);
                        securityScoreProgressBar.setProgressTintList(ColorStateList.valueOf(progressBarColor));


                    } else {
                        // Handle the error if data fetching fails
                        // For example, you can show an error message or log the error
                    }
                });
    }
    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            // User is not signed in or there is an error, handle it accordingly
            // For example, you can return an empty string or show an error message
            return "";
        }
    }
    private int calculateOverallSecurityScore(List<PasswordItem> passwordList) {
        int totalSecurityScore = 0;
        int numberOfApps = passwordList.size();

        for (PasswordItem passwordItem : passwordList) {
            String password = passwordItem.getPassword();
            // Prompt the user to enter the encryption key (you can use a dialog for this)
            String encryptionKey = "keycryptencryption"; // Replace with the user-entered encryption key

            // Decrypt the password
            String decryptedPassword = passwordItem.getDecryptedPassword(encryptionKey);
            int passwordStrength = analyzePasswordStrength(decryptedPassword);
            totalSecurityScore += passwordStrength;
        }

        // Calculate the average security score
        int overallSecurityScore = (int) Math.round(totalSecurityScore / (double) numberOfApps);
        return overallSecurityScore;
    }
    private int analyzePasswordStrength(String password) {
        // Initialize variables to keep track of password criteria
        Log.d("SecurityCheck","Passwrod: "+password);
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSymbol = false;

        // Check the password length
        int passwordLength = password.length();

        // Iterate through each character in the password to check for criteria
        for (int i = 0; i < passwordLength; i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSymbol = true;
            }
        }

        // Calculate the password strength based on the criteria
        int strength = 0;

        // Length contributes 30% to the strength
        int lengthStrength = (int) (30.0 * (passwordLength / 8.0)); // Assuming 8 characters is a strong password length
        strength += lengthStrength;

        // Presence of uppercase, lowercase, number, and symbol each contributes 20% to the strength
        int criteriaStrength = 0;
        if (hasUppercase) criteriaStrength += 20;
        if (hasLowercase) criteriaStrength += 20;
        if (hasNumber) criteriaStrength += 20;
        if (hasSymbol) criteriaStrength += 20;

        strength += criteriaStrength;

        // Check for specific criteria to classify password strength
        if (passwordLength >= 12 && hasUppercase && hasLowercase && hasNumber && hasSymbol) {
            // Very Strong password: Password length is 12 or more and contains uppercase, lowercase, numbers, and symbols.
            strength = 100;
        } else if (passwordLength >= 10 && hasUppercase && hasLowercase && (hasNumber || hasSymbol)) {
            // Strong password: Password length is 10 or more and contains uppercase, lowercase, and either number or symbol.
            strength = 90;
        } else if ((passwordLength >= 8 && passwordLength < 10) && (hasUppercase || hasLowercase) && (hasNumber || hasSymbol)) {
            // Medium password: Password length is between 8 and 9 and contains either uppercase/lowercase and either number/symbol.
            strength = 70;
        } else if ((passwordLength >= 6 && passwordLength < 8) && (hasUppercase || hasLowercase) && hasNumber && hasSymbol) {
            // Fair password: Password length is between 6 and 7 and contains either uppercase/lowercase, number, and symbol.
            strength = 40;
        } else {
            // Weak password: Password length is less than 6 or lacks certain criteria.
            strength = 20;
        }
        Log.d("SecurityCheck","Strength: "+strength);
        return strength;
    }



    private int getProgressBarColor(int securityScore) {
        if (securityScore >= 90) {
            return ContextCompat.getColor(this, R.color.progress_bar_green_color);
        } else if (securityScore >= 60) {
            return ContextCompat.getColor(this, R.color.progress_bar_yellow_color);
        } else {
            return ContextCompat.getColor(this, R.color.progress_bar_red_color);
        }
    }
    public void showToast(String value) {
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
