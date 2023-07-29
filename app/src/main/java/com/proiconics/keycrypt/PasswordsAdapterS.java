package com.proiconics.keycrypt;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordsAdapterS extends RecyclerView.Adapter<PasswordsAdapterS.PasswordViewHolder> {

    private List<PasswordItem> passwordList;
    private Context context;

    public PasswordsAdapterS(List<PasswordItem> passwordList) {
        this.passwordList = passwordList;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.security_check_item, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordItem passwordItem = passwordList.get(position);
        holder.titleTextView.setText(passwordItem.getTitle());
        holder.emailTextView.setText(passwordItem.getEmail());

        // Set the app icon based on the iconId
        int iconId = passwordItem.getIconId();
        holder.iconImageView.setImageResource(iconId);

        // Decrypt the password
        String encryptedPassword = passwordItem.getPassword();
        String encryptionKey = "keycryptencryption"; // Replace with the user-entered encryption key
        String decryptedPassword = passwordItem.getDecryptedPassword(encryptionKey);

        // Analyze password strength and display it in securityTextView
        int passwordStrengthLevel = analyzePasswordStrengthS(decryptedPassword);
        String strengthText = getSecurityLevelText(passwordStrengthLevel);
        holder.securityTextView.setText(strengthText);
        setTextColorBasedOnStrength(holder.securityTextView, passwordStrengthLevel);

        // Set a click listener on the password item
        holder.itemView.setOnClickListener(v -> {
            // Copy the decrypted password to the clipboard
            copyToClipboard(decryptedPassword);

            // Show a toast to indicate the password is copied
            Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public class PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView emailTextView;
        ImageView iconImageView;
        TextView securityTextView;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            securityTextView = itemView.findViewById(R.id.securityTextView);

            // Get the context from the itemView
            context = itemView.getContext();
        }
    }

    // Helper method to analyze password strength and return the strength level
    private int analyzePasswordStrength(String password) {
        int passwordLength = password.length();
        if (passwordLength < 6) {
            return 0; // Weak
        } else if (passwordLength >= 6 && passwordLength <= 8) {
            return 1; // Medium
        } else {
            return 2; // Strong
        }
    }

    // Helper method to get security level text based on the strength level
    private String getSecurityLevelText(int strengthLevel) {

        if(strengthLevel<40){
            return "Weak";}
        else if(strengthLevel > 40 && strengthLevel < 70){
            return "Medium";
        }else {
            return "Strong";
        }

    }

    // Helper method to set text color based on password strength
    private void setTextColorBasedOnStrength(TextView textView, int strengthLevel) {
        int colorResId;

        if(strengthLevel<40){
            colorResId = R.color.red;
        }
        else if(strengthLevel > 40 && strengthLevel < 70){
            colorResId = R.color.yellow;
        }else if(strengthLevel > 70){
            colorResId = R.color.green;
        }else {
            colorResId = R.color.black;
        }

        int color = ContextCompat.getColor(context, colorResId);
        textView.setTextColor(color);
    }

    // Helper method to copy text to clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("password", text);
            clipboardManager.setPrimaryClip(clipData);
        }
    }
    private int analyzePasswordStrengthS(String password) {
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
}

