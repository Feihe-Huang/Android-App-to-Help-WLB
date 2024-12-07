package com.example.project.Entertainment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class GameMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameMenu(this, this));
    }

    public void startGameViewActivity(int difficulty) {
        Intent gameViewActivity = new Intent(this, GameViewActivity.class);
        gameViewActivity.putExtra("Difficulty", difficulty);
        startActivity(gameViewActivity);
    }


}