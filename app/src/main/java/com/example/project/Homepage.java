package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Entertainment.GameMenuActivity;
import com.example.project.Note.NoteActivity;

public class Homepage extends AppCompatActivity {

//    Button workBtn, entertainmentBtn, profileBtn;
    View profileBtn, todoCard, focusCard, gameCard, musicCard;
    int textViewID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        todoCard = findViewById(R.id.todo_card);
        focusCard = findViewById(R.id.focus_card);
        gameCard = findViewById(R.id.game_card);
        profileBtn = findViewById(R.id.logo);
        musicCard = findViewById(R.id.music_card);

        // 跳转协助工作界面
        todoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, TodoActivity.class);
                startActivity(intent);
            }
        });

        focusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        // login 处传来的用户ID
        final Bundle a = getIntent().getExtras();
        // 获取传数过来的值
        if (a != null){
            textViewID = Integer.parseInt(a.getString("userID"));
        }

        // 点击用户信息时传值+跳转
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Homepage.this, InformationActivity.class);

                // 传输用户信息
                Bundle userInfo = new Bundle();

                // 用户ID
                int id = textViewID;
                userInfo.putString("userID", String.valueOf(id));

                intent.putExtras(userInfo);
                startActivity(intent);
            }
        });

        gameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Homepage.this, GameMenuActivity.class);
                startActivity(intent);
            }
        });

        musicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, MusicActivity.class);
                startActivity(intent);
            }
        });

    }
}