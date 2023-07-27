package com.proiconics.keycrypt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.proiconics.keycrypt.R;

public class AddRecord extends AppCompatActivity {

    private TextView titleTextView;
    private TextView emailTextView;
    private EditText passwordTitleEditText;
    private EditText usernameEditText;
    private ImageView iconImageView;
    private Button btnSelectIcon;
    private AlertDialog recordDialog;
    private ImageView iconFacebook, iconInstagram, iconTiktok, iconLinkedin, iconGithub, iconTwitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        titleTextView = findViewById(R.id.titleTextView);
        emailTextView = findViewById(R.id.emailTextView);
        passwordTitleEditText = findViewById(R.id.passwordTitleEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        iconImageView = findViewById(R.id.iconImageView);
        btnSelectIcon = findViewById(R.id.iconSelectionButton);


        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(AddRecord.this);
        // inflate a layout for the dialog
        LayoutInflater inflater = AddRecord.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_record_layout, null);
        DialogBuilder.setView(dialogView);
        // create the AlertDialog instance
        recordDialog = DialogBuilder.create();
        // Get the alert dialog's window
        Window window = recordDialog.getWindow();
        // Set the background to a rounded drawable
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //
        iconFacebook = dialogView.findViewById(R.id.iconFacebook);
        iconInstagram = dialogView.findViewById(R.id.iconInstagram);
        iconTiktok = dialogView.findViewById(R.id.iconTiktok);
        iconLinkedin = dialogView.findViewById(R.id.iconLinkedin);
        iconGithub = dialogView.findViewById(R.id.iconGithub);
        iconTwitter = dialogView.findViewById(R.id.iconTwitter);

        iconFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for Facebook icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.facebook);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for Instagram icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.instagram);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconTiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for Tiktok icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.tik_tok);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for LinkedIn icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.linkedin);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for Github icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.github);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for Twitter icon
                // You can perform any actions or updates here
                // For example, you can set the selected icon to the above layout's icon
                iconImageView.setImageResource(R.drawable.twitter);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });




        passwordTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the title in the above layout
                titleTextView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the email in the above layout
                emailTextView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        btnSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            recordDialog.show();

            }
        });



    }
}