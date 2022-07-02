package com.matrix_maeny.chatbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ChatBotDataBase extends SQLiteOpenHelper {


    public ChatBotDataBase(@Nullable Context context) {
        super(context, "ChatBot.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Chatbot(name TEXT primary key, botName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Chatbot");
    }

    public void insertData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name","name");
        cv.put("botName","ChatBot");

        db.insert("Chatbot",null,cv);
    }

    public boolean updateData(String botName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("botName",botName);
        long result = db.update("Chatbot",cv,"name=?",new String[]{"name"});

        return result != -1;

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from Chatbot",null,null);
    }
}
