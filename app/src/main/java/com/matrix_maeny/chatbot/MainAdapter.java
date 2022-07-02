package com.matrix_maeny.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class MainAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModel> list;



    public MainAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.robot_sender_layout, parent, false);
                return new RobotViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel model = list.get(position);

        switch (model.getSender()) {
            case "user":
                ((UserViewHolder) holder).userTextView.setText(model.getMessage());
                break;
            case "robot":
                ((RobotViewHolder) holder).robotTextView.setText(model.getMessage());
                break;
        }

        try {
            ((RobotViewHolder) holder).cardView.setOnClickListener(v -> {

                Toast.makeText(context, "Haha..Hahahaa.. I'm getting ticklish", Toast.LENGTH_LONG).show();
            });
        } catch (Exception ignored) {}

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getSender()) {
            case "user":
                return 0;
            case "robot":
                return 1;
            default:
                return -1;
        }

    }

    public static class RobotViewHolder extends RecyclerView.ViewHolder {

        TextView robotTextView;
        CardView cardView;

        public RobotViewHolder(@NonNull View itemView) {
            super(itemView);

            robotTextView = itemView.findViewById(R.id.robotTextView);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.userTextView);

        }
    }
}
