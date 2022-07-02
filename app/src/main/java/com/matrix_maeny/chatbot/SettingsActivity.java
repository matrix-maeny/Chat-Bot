package com.matrix_maeny.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private EditText userChatBotName;
    private AppCompatButton saveBtn;

    String name = "ChatBot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        userChatBotName = findViewById(R.id.userChatBotName);
        saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            if (checkName()) {
                saveName();
            }
        });
    }

    private void saveName() {
        ChatBotDataBase dataBase = new ChatBotDataBase(SettingsActivity.this);

        if(dataBase.updateData(name)){
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Some error occurred:S", Toast.LENGTH_SHORT).show();
        }

        dataBase.close();
    }

    private boolean checkName() {

        String tempName;

        try {
            tempName = userChatBotName.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tempName.equals("")) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;

        }

        this.name = tempName;
        return true;


    }
}