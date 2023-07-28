package com.proiconics.keycrypt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.proiconics.keycrypt.R;

import java.util.HashMap;
import java.util.Map;

public class AddRecord extends AppCompatActivity {

    private TextView titleTextView;
    private TextView emailTextView;
    private EditText passwordTitleEditText, usernameEditText, passwordEditText;
    private ImageView iconImageView;
    private Button btnSelectIcon, btnSave;
    private AlertDialog recordDialog;
    private ImageView iconFacebook, iconInstagram, iconTiktok, iconLinkedin, iconGithub, iconTwitter;

    private FirebaseFirestore mFirestore;
    int selectedIconId = -1;

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
        passwordEditText = findViewById(R.id.passwordEditText);
        btnSave = findViewById(R.id.saveButton);


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
        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();

        iconImageView.setImageResource(R.drawable.logo);
        selectedIconId = R.drawable.logo;
        iconFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.facebook;
                iconImageView.setImageResource(R.drawable.facebook);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.instagram;
                iconImageView.setImageResource(R.drawable.instagram);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconTiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.tik_tok;
                iconImageView.setImageResource(R.drawable.tik_tok);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.linkedin;
                iconImageView.setImageResource(R.drawable.linkedin);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.github;
                iconImageView.setImageResource(R.drawable.github);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconId = R.drawable.twitter;
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePasswordRecord();
            }
        });



    }

    private void savePasswordRecord() {
        String passwordTitle = passwordTitleEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Ensure all fields are filled before saving
        if (TextUtils.isEmpty(passwordTitle) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user data map
        Map<String, Object> passwordData = new HashMap<>();
        passwordData.put("title", passwordTitle);
        passwordData.put("email", username);
        passwordData.put("password", password);
        passwordData.put("iconId", selectedIconId);

        // Store password data in Firestore
        mFirestore.collection("passwords").document(username)
                .set(passwordData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password data stored in Firestore successfully
                        Toast.makeText(this, "Password record saved", Toast.LENGTH_SHORT).show();
                        // Clear the EditText fields after saving
                        passwordTitleEditText.setText("");
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        iconImageView.setImageResource(R.drawable.logo);

                    } else {
                        // Failed to store password data in Firestore
                        // Handle the error, e.g., display a toast or dialog
                        Toast.makeText(this, "Failed to save password record", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setText(){

    }

}