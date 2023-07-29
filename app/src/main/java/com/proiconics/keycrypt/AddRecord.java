package com.proiconics.keycrypt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ImageView iconFacebook, iconInstagram, iconTiktok, iconLinkedin, iconGithub, iconTwitter, iconGoogle;

    private FirebaseFirestore mFirestore;
    int selectedIconId = -1;

    @SuppressLint("MissingInflatedId")
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
        iconGoogle = dialogView.findViewById(R.id.iconGoogle);
        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();

        passwordTitleEditText.setEnabled(false);

        iconImageView.setImageResource(R.drawable.logo);
        selectedIconId = R.drawable.logo;
        iconFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Facebook");
                selectedIconId = R.drawable.facebook;
                iconImageView.setImageResource(R.drawable.facebook);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Instagram");
                selectedIconId = R.drawable.instagram;
                iconImageView.setImageResource(R.drawable.instagram);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordTitleEditText.setText("Google");
                selectedIconId = R.drawable.ic_google;
                iconImageView.setImageResource(R.drawable.ic_google);
                recordDialog.dismiss();
            }
        });
        iconTiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Tik Tok");
                selectedIconId = R.drawable.tik_tok;
                iconImageView.setImageResource(R.drawable.tik_tok);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Linked in");
                selectedIconId = R.drawable.linkedin;
                iconImageView.setImageResource(R.drawable.linkedin);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Github");
                selectedIconId = R.drawable.github;
                iconImageView.setImageResource(R.drawable.github);
                recordDialog.dismiss();  // Close the dialog if needed
            }
        });

        iconTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordTitleEditText.setText("Twitter");
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, get the user ID
            String userId = currentUser.getUid();

            String passwordTitle = passwordTitleEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Ensure all fields are filled before saving
            if (TextUtils.isEmpty(passwordTitle) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                showToast("Please fill all fields");
                return;
            }

            // Create a new user data map
            Map<String, Object> passwordData = new HashMap<>();
            passwordData.put("userId", userId);
            passwordData.put("title", passwordTitle);
            passwordData.put("email", username);
            passwordData.put("password", password);
            passwordData.put("iconId", selectedIconId);

            // Store password data in Firestore with a timestamp as the document name
            mFirestore.collection("passwords")
                    .add(passwordData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Password data stored in Firestore successfully
                            showToast("Password saved");
                            // Clear the EditText fields after saving
                            passwordTitleEditText.setText("");
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            iconImageView.setImageResource(R.drawable.logo);

                        } else {
                            // Failed to store password data in Firestore
                            // Handle the error, e.g., display a toast or dialog
                            showToast("Failed to save password record");
                        }
                    });
        } else {
            // User is not logged in or authenticated
            // Handle the case where the user ID is not available or prompt the user to log in
        }
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