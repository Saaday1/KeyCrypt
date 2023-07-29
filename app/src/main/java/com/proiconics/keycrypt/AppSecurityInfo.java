package com.proiconics.keycrypt;

public class AppSecurityInfo {
    private String appName;
    private int passwordStrength; // You can use an integer value to represent password strength.

    public AppSecurityInfo() {
        // Empty constructor required for Firestore
    }

    public AppSecurityInfo(String appName, int passwordStrength) {
        this.appName = appName;
        this.passwordStrength = passwordStrength;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getPasswordStrength() {
        return passwordStrength;
    }

    public void setPasswordStrength(int passwordStrength) {
        this.passwordStrength = passwordStrength;
    }
}
