package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    ImageView btnProfile, btnPassGeneration, btnSecurityCheck, btnHome, btnNotification, profileImageView;
    private ProgressBar progressBar;
    private TextView nameTextView, totalAppsTextView;
    LinearLayout bottomNavigationBar;
    FrameLayout floatingButton;
    private FirebaseFirestore mFirestore;
    private List<PasswordItem> passwordList;
    private RecyclerView recyclerView;
    private PasswordAdapter passwordAdapter;
    private CollectionReference passwordsCollection;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
        btnProfile = bottomNavigationBar.findViewById(R.id.iconProfile);
        btnPassGeneration = bottomNavigationBar.findViewById(R.id.iconPassGeneration);
        btnSecurityCheck = bottomNavigationBar.findViewById(R.id.iconVerified);
        btnHome = bottomNavigationBar.findViewById(R.id.iconHome);
        floatingButton = findViewById(R.id.floatingButtonContainer);
        btnNotification = findViewById(R.id.notificationIcon);
        // Initialize Firestore instance and collection reference
        mFirestore = FirebaseFirestore.getInstance();
        passwordsCollection = mFirestore.collection("passwords");
        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        // Initialize the RecyclerView and its adapter
        recyclerView = findViewById(R.id.passwordsRecyclerView);
        passwordList = new ArrayList<>();
        passwordAdapter = new PasswordAdapter(passwordList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordAdapter);
        totalAppsTextView = findViewById(R.id.totalAppsTextView);
        progressBar = findViewById(R.id.progressBarMain);

        progressBar.setVisibility(View.VISIBLE);

        fetchUserProfileData();
        fetchPasswordRecords();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,profileActivity.class));
            }
        });
        btnPassGeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,PasswordGenerator.class));
            }
        });
        btnSecurityCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,SecurityCheck.class));
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainScreen.this,AddRecord.class));

            }
        });
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this,NotificationsScreen.class));
            }
        });



    }

    // Inside your MainScreen activity or wherever you are using this method
    private void fetchUserProfileData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Get the user's name
            String userName = currentUser.getDisplayName();

            // Set the user's name to the TextView
            nameTextView.setText(userName);

            // Get the user's profile photo URL (if available)
            Uri userProfilePhotoUrl = currentUser.getPhotoUrl();
            if (userProfilePhotoUrl != null) {
                // Use Picasso to load the profile photo into the CircleImageView
                Picasso.get().load(userProfilePhotoUrl).into(profileImageView);
            } else {
                // If the user does not have a profile photo, you can set a default image
                profileImageView.setImageResource(R.drawable.profile);
            }
        }
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
                        passwordAdapter.notifyDataSetChanged();

                        // Calculate and display the number of apps
                        int numberOfApps = passwordList.size();
                        totalAppsTextView.setText(numberOfApps+" Apps");
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        // Handle the error if data fetching fails
                        // For example, you can show an error message or log the error
                    }
                });
    }

    // Inside your activity or fragment
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

    @Override
    protected void onResume() {
        super.onResume();
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