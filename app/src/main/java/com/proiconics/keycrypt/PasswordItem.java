package com.proiconics.keycrypt;

import android.util.Base64;

import org.cryptonode.jncryptor.AES256JNCryptor;

public class PasswordItem {
    private String title;
    private String email;
    private String password;
    private int iconId;
    private String appName;

    // Constant for the default icon
    private static final int DEFAULT_ICON_ID = R.drawable.logo;

    public PasswordItem() {
        // Default constructor required for Firestore serialization
    }

    public PasswordItem(String title, String email, String password, int iconId) {
        // Check and set default values for empty fields
        this.title = title.isEmpty() ? "Default Title" : title;
        this.email = email.isEmpty() ? "default@example.com" : email;
        this.password = password.isEmpty() ? "default_password" : password;

        // Check if the provided iconId is valid, otherwise set the default icon
        if (isValidIcon(iconId)) {
            this.iconId = iconId;
        } else {
            this.iconId = DEFAULT_ICON_ID;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    // Helper method to check if the provided iconId is valid
    private boolean isValidIcon(int iconId) {
        return iconId != R.drawable.logo; // Assuming that the default icon ID is 0 or not provided
    }
    // Decrypt the password and return the plaintext
    public String getDecryptedPassword(String encryptionKey) {
        try {
            byte[] encryptedPasswordBytes = Base64.decode(password, Base64.DEFAULT);
            AES256JNCryptor cryptor = new AES256JNCryptor();
            byte[] decryptedPasswordBytes = cryptor.decryptData(encryptedPasswordBytes, encryptionKey.toCharArray());
            return new String(decryptedPasswordBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle decryption error
            return "Error decrypting password";
        }
    }
    // Method to analyze the password strength and return a String representing the strength
    public String getPasswordStrength() {
        int passwordLength = password.length();
        if (passwordLength < 6) {
            return "Weak";
        } else if (passwordLength >= 6 && passwordLength <= 8) {
            return "Medium";
        } else {
            return "Strong";
        }
    }
}
