package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Emailverification extends AppCompatActivity {

    Button signInButton;
    Button gmailButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        signInButton = findViewById(R.id.signInButton);
        gmailButton = findViewById(R.id.gmailButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Emailverification.this,LoginActivity.class));
            }
        });
        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGmailInbox();
            }
        });

    }
    private void openGmailInbox() {

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");

        // Verify that there is an activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to open the Gmail inbox
            startActivity(intent);
        } else {
            // If there is no app that can handle the intent, show an error message
            Toast.makeText(this, "Gmail app not installed", Toast.LENGTH_SHORT).show();
        }
    }




}