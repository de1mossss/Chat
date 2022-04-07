package com.illumo.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ChatMain extends AppCompatActivity {

    private String userid;
    private TextView useridText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        useridText = findViewById(R.id.userid);
        userid = getIntent().getStringExtra("LoginResult");
        useridText.setText(userid);
    }
}