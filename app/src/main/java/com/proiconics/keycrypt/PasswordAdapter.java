package com.proiconics.keycrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private List<PasswordItem> passwordList;
    private Context context;

    public PasswordAdapter(List<PasswordItem> passwordList, Context context) {
        this.passwordList = passwordList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordItem passwordItem = passwordList.get(position);
        holder.titleTextView.setText(passwordItem.getTitle());
        holder.emailTextView.setText(passwordItem.getEmail());
        holder.iconImageView.setImageResource(passwordItem.getIconId());

        // Set a click listener on the password item
        holder.itemView.setOnClickListener(v -> {
            // Get the encrypted password of the clicked item
            String encryptedPassword = passwordItem.getPassword();

            // Prompt the user to enter the encryption key (you can use a dialog for this)
            String encryptionKey = "keycryptencryption"; // Replace with the user-entered encryption key

            // Decrypt the password
            String decryptedPassword = passwordItem.getDecryptedPassword(encryptionKey);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView emailTextView;
        ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }

    // Helper method to copy text to clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("password", text);
            clipboardManager.setPrimaryClip(clipData);
        }
    }
}
