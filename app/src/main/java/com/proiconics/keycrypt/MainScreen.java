package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.proiconics.keycrypt.R;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    ImageView btnProfile, btnPassGeneration, btnSecurityCheck, btnHome, btnNotification;
    LinearLayout bottomNavigationBar;
    FrameLayout floatingButton;
    private FirebaseFirestore mFirestore;
    private List<PasswordItem> passwordList;
    private RecyclerView recyclerView;
    private PasswordAdapter passwordAdapter;
    private CollectionReference passwordsCollection;

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

        // Initialize the RecyclerView and its adapter
        recyclerView = findViewById(R.id.passwordsRecyclerView);
        passwordList = new ArrayList<>();
        passwordAdapter = new PasswordAdapter(passwordList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordAdapter);

        // Query Firestore for password records
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