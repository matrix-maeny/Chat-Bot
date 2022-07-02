package com.matrix_maeny.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView sendBtn;
    EditText userInputMsg;

    private final String USER_KEY = "user";
    private final String ROBOT_KEY = "robot";

    String message = null;

    RequestQueue requestQueue;

    MainAdapter adapter;
    ArrayList<MessageModel> list;


    final Handler handler = new Handler();
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatBotDataBase dataBase = new ChatBotDataBase(MainActivity.this);
        dataBase.insertData();
        dataBase.close();

        setToolbarTitle();
        Objects.requireNonNull(getSupportActionBar()).setElevation(10);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.sender2);

        recyclerView = findViewById(R.id.recyclerView);
        sendBtn = findViewById(R.id.sendBtn);
        userInputMsg = findViewById(R.id.userInputMsg);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        sendBtn.setOnClickListener(sendBtnListener);


        list = new ArrayList<>();
        adapter = new MainAdapter(MainActivity.this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }

    private void setToolbarTitle() {

        ChatBotDataBase dataBase = new ChatBotDataBase(MainActivity.this);
        Cursor cursor = dataBase.getData();
        String name = "ChatBot";
        while (cursor.moveToNext()) {
            name = cursor.getString(1);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("  " + name);

        dataBase.close();
    }

    View.OnClickListener sendBtnListener = v -> {

        if (checkMsg()) {
            // send message

            new Thread() {
                public void run() {
                    sendMessage(message);
                }

            }.start();
            userInputMsg.setText("");
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage(String message) {

        list.add(new MessageModel(message, USER_KEY));
        handler.post(() -> recyclerView.smoothScrollToPosition(list.size() - 1));

//        adapter.notifyDataSetChanged();
        handler.post(() -> adapter.notifyDataSetChanged());

        requestQueue.getCache().clear();

        String url = "http://api.brainshop.ai/get?bid=165038&key=G5VRevX6xZxY9blF&uid=[uid]&msg=" + message;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            try {
                String robotResponse = response.getString("cnt");
                list.add(new MessageModel(robotResponse, ROBOT_KEY));

            } catch (JSONException e) {

                list.add(new MessageModel("No response", ROBOT_KEY));

            }
            handler.post(() -> Objects.requireNonNull(getSupportActionBar()).setSubtitle("   typing..."));
            handler.postDelayed(() -> {

                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(list.size() - 1);
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("");

            }, 1000);
        }, error -> {

            list.add(new MessageModel("I want to talk with you.\nCan't you enable Internet for me..!?", ROBOT_KEY));
            String myError = error + "";
            if (myError.contains("NoConnectionError")) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Some Error occurred: contact matrix", Toast.LENGTH_SHORT).show();
            }

            handler.post(() -> Objects.requireNonNull(getSupportActionBar()).setSubtitle("   typing..."));
            handler.postDelayed(() -> {

                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(list.size() - 1);
                Objects.requireNonNull(getSupportActionBar()).setSubtitle("");

            }, 1000);

        });

//        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//            list.clear();
//
//            try {
//                JSONArray array = response.getJSONArray("items");
//
//                JSONObject bookObject, volumeObj = null;//, saleInfoObj = null, retailPriceObj, imageLinks, accessInfoObj;
//              String title = null;
//                for (int i = 0; i <=0; i++) {
//
//
//                    // taking each book , if it is caught by exception then need not to take volume obj
//                    try {
//                        bookObject = array.getJSONObject(i);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        continue;
//                    }
//
//
//                    try {
//                        if (bookObject != null) {
//                            volumeObj = bookObject.getJSONObject("volumeInfo");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (volumeObj != null) {
//                        title = volumeObj.optString("title");
//                        list.add(new MessageModel(title,ROBOT_KEY));
//
//
//                    } // getting volume info
//
//
//
//                }
//
//                handler.post(()->adapter.notifyDataSetChanged());
//
//
//            } catch (Exception e) {
//                // do something worthy
//                e.printStackTrace();
//
//            }
//
//        }, error -> {
//
//            String myError = error+"";
//            if(myError.contains("NoConnectionError")){
//                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this, "Some Error occurred: contact matrix", Toast.LENGTH_SHORT).show();
//            }
//
//
//
//
//        });


        queue.add(objectRequest);


    }

    private boolean checkMsg() {

        try {
            message = userInputMsg.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (message.equals("")) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about_app:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.settings_app:
                // go to settings
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle();
    }
}
