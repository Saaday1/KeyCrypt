package com.proiconics.keycrypt;

public class PasswordItem {
    private String title;
    private String email;
    private String password;
    private int iconId;

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
}
