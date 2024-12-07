package com.example.project.Entertainment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class GameViewActivity extends AppCompatActivity {

    // music
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int difficulty = getIntent().getIntExtra("Difficulty",1);

        // music
        mediaPlayer = MediaPlayer.create(this, R.raw.piano_music);

        // 跳转画出game view
        setContentView(new GameView(this, difficulty, this, mediaPlayer));
    }

    public void endGame() {
        finish();
    }

    // if back
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        finish();
    }
}