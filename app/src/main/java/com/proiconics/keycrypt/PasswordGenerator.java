package com.proiconics.keycrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.RangeSlider;

import java.util.Random;

public class PasswordGenerator extends AppCompatActivity {

    private TextView characterCountTextView;
    private TextView generatedPasswordTextView;
    private Button generatePasswordButton;

    private int capsValue = 0;
    private int digitsValue = 0;
    private int specialValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);

        // Get the capsSeekBar reference
        RangeSlider capsSeekBar = findViewById(R.id.capsSeekBar);
        RangeSlider digitsSeekBar = findViewById(R.id.digitsSeekBar);
        RangeSlider specialSeekBar = findViewById(R.id.specialSeekBar);
        characterCountTextView = findViewById(R.id.characterCountTextView);
        generatedPasswordTextView = findViewById(R.id.generated_password);
        generatePasswordButton = findViewById(R.id.passwordGenerateionButton);

        // Set the default value of 50
        capsSeekBar.setValues((float) capsValue);
        digitsSeekBar.setValues((float) digitsValue);
        specialSeekBar.setValues((float) specialValue);

        capsSeekBar.addOnChangeListener((slider, value, fromUser) -> {
            capsValue = (int) value;
            updateCharacterCount();
        });

        digitsSeekBar.addOnChangeListener((slider, value, fromUser) -> {
            digitsValue = (int) value;
            updateCharacterCount();
        });

        specialSeekBar.addOnChangeListener((slider, value, fromUser) -> {
            specialValue = (int) value;
            updateCharacterCount();
        });

        generatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomPassword();
            }
        });

        // Update character count initially
        updateCharacterCount();

        findViewById(R.id.copyIcon).setOnClickListener(v -> copyGeneratedPasswordToClipboard());
    }

    private void updateCharacterCount() {
        int totalCharacters = capsValue + digitsValue + specialValue + new Random().nextInt(3) + 6;
        characterCountTextView.setText(String.valueOf(totalCharacters));
    }

    private void generateRandomPassword() {
        int totalCharacters = Integer.parseInt(characterCountTextView.getText().toString());

        StringBuilder passwordBuilder = new StringBuilder();
        String capsChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digitsChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:',.<>?";

        Random random = new Random();

        int charsAdded = 0;

        // Generate password without caps, digits, and special characters if all sliders are set to 0
        if (capsValue == 0 && digitsValue == 0 && specialValue == 0) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            for (int i = 0; i < totalCharacters; i++) {
                passwordBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            charsAdded = totalCharacters;
        } else {
            // Add the selected number of characters for each slider value
            for (int i = 0; i < capsValue; i++) {
                if (charsAdded < totalCharacters) {
                    passwordBuilder.append(capsChars.charAt(random.nextInt(capsChars.length())));
                    charsAdded++;
                }
            }
            for (int i = 0; i < digitsValue; i++) {
                if (charsAdded < totalCharacters) {
                    passwordBuilder.append(digitsChars.charAt(random.nextInt(digitsChars.length())));
                    charsAdded++;
                }
            }
            for (int i = 0; i < specialValue; i++) {
                if (charsAdded < totalCharacters) {
                    passwordBuilder.append(specialChars.charAt(random.nextInt(specialChars.length())));
                    charsAdded++;
                }
            }
        }

        // Fill remaining characters with lowercase alphabet if necessary
        while (charsAdded < totalCharacters) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            passwordBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            charsAdded++;
        }

        // Shuffle the password characters
        char[] passwordArray = passwordBuilder.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        String randomPassword = new String(passwordArray);
        generatedPasswordTextView.setText(randomPassword);
    }




    private void copyGeneratedPasswordToClipboard() {
        String password = generatedPasswordTextView.getText().toString();
        if (!TextUtils.isEmpty(password)) {
            // Get the ClipboardManager
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            // Create a ClipData object to hold the text to be copied
            ClipData clip = ClipData.newPlainText("Generated Password", password);

            // Copy the ClipData to the clipboard
            clipboard.setPrimaryClip(clip);

            // Show a toast to indicate that the password has been copied
            Toast.makeText(this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

}
