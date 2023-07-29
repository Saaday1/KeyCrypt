package com.proiconics.keycrypt;


public class AppItem {
    private String title;
    private String email;
    private String password;
    private String userId;
    private int iconId; // Update the data type to Long

    public AppItem() {
        // Default constructor required for Firestore
    }

    public AppItem(String title, String email, String password, String userId, int iconId) {
        this.title = title;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public int getIconId() {
        return iconId;
    }
    // Method to get the drawable resource ID based on the iconId
    public int getIconDrawableResourceId() {
        // You can define a mapping or logic here to determine the drawable resource ID
        // based on the iconId. For example, you can use a switch statement or a HashMap.
        // Here, we are assuming that the iconId corresponds to a drawable resource ID.
        return (int) iconId;
    }
}

