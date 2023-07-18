package com.example.keycrypt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainScreen extends AppCompatActivity {

    ImageView btnProfile, btnPassGeneration, btnSecurityCheck, btnHome, btnNotification;
    LinearLayout bottomNavigationBar;
    FrameLayout floatingButton;

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
}