package com.proiconics.keycrypt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppItemAdapter extends RecyclerView.Adapter<AppItemAdapter.AppItemViewHolder> {

    private List<AppItem> appItemList;

    public AppItemAdapter(List<AppItem> appItemList) {
        this.appItemList = appItemList;
    }

    @NonNull
    @Override
    public AppItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.security_check_item, parent, false);
        return new AppItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppItemViewHolder holder, int position) {
        AppItem appItem = appItemList.get(position);
        holder.bind(appItem);
    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }

    public class AppItemViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView emailTextView;
        // Add more views for other fields, e.g., icon, password, etc.

        public AppItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            // Initialize other views here, if present.
        }

        public void bind(AppItem appItem) {
            titleTextView.setText(appItem.getTitle());
            emailTextView.setText(appItem.getEmail());
            // Bind other data to the views, e.g., icon, password, etc.
        }
    }
}

