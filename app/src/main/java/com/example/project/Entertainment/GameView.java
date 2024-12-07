package com.example.project.Entertainment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.R;

import java.util.ArrayList;
import java.util.Random;

import Model.Block;

public class GameView extends View {

    private Block block;
    private int points = 0;
    private final int timerInterval = 30;
    private int frequencyNum;
    private int velocity;
    private int penaltyPoints;
    private int difficulty;
    private int counter = 0;

    // private MenuCallback menuCallback;
    GameViewActivity gameViewActivity;
    MediaPlayer mediaPlayer;


    // 创建一个array 来存放所有block
    private ArrayList<Block> blockArray = new ArrayList<Block>();

    public GameView(Context context, int difficulty, GameViewActivity gameViewActivity, MediaPlayer mediaPlayer) {
        super(context);
        this.gameViewActivity = gameViewActivity;
        this.difficulty = difficulty;
        this.mediaPlayer = mediaPlayer;

        // 设定block 出现频率、移动速度、得分数
        if (difficulty == 1){
            frequencyNum = 11;
            velocity = 800;
            penaltyPoints = 2;
        } else if (difficulty == 2){
            frequencyNum = 9;
            velocity = 1100;
            penaltyPoints = 3;
        } else {
            frequencyNum = 7;
            velocity = 1500;
            penaltyPoints = 5;
        }

        // 设定timer
        Timer timer = new Timer();
        timer.start();
    }

    protected void createNewBlock(Context context) {
        // 在四个列中随机一列生成方块
        Random random = new Random();
        block  = new Block(context, 0, 0, velocity, random.nextInt(4));
        blockArray.add(block);
    }


    protected void update() {
        // 依据设定的频率判断什么时候创建新的block
        if (counter < frequencyNum) {
            counter++;
        } else {
            createNewBlock(getContext());
            counter =0;
        }
        for (Block block : blockArray){
            // 判断block是否到达底部若达到则停止继续向下
            if (block.getY() <= 2505) {
                block.update(timerInterval);
            } else if (block.isScored() == false) {
                // 当用户没有点击block，减去分数
                points -= penaltyPoints;
                block.setScored(true);
            }
        }
        // 不断重新绘制下落的block
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画界面背景
        canvas.drawRGB(241,243,206);
        // 画出各个文本以及按钮
        Paint pointsText = new Paint();
        pointsText.setAntiAlias(true);
        pointsText.setSubpixelText(true);
        int pointsTextColor = getResources().getColor(R.color.black);
        pointsText.setColor(pointsTextColor);

        // music text
        Paint musicText = new Paint();
        musicText.setAntiAlias(true);
        musicText.setSubpixelText(true);
        int musicTextColor = getResources().getColor(R.color.colorAccent);
        musicText.setColor(musicTextColor);

        // music button
        Paint buttonMusic = new Paint();
        buttonMusic.setAntiAlias(true);
        int musicButtonColor = getResources().getColor(R.color.gameColor);
        buttonMusic.setColor(musicButtonColor);

        // text back
        Paint backText = new Paint();
        backText.setAntiAlias(true);
        backText.setSubpixelText(true);
        int backTextColor = getResources().getColor(R.color.black);
        backText.setColor(backTextColor);

        pointsText.setTextSize(105.0f);
        backText.setTextSize(85.0f);
        musicText.setTextSize(65.0f);

        // back button
        Paint backButton = new Paint();
        backButton.setAntiAlias(true);
        int backButtonColor = getResources().getColor(R.color.gameColor);
        backButton.setColor(backButtonColor);

        canvas.drawRect(800, 0, 1150, 160, backButton);
        canvas.drawText("Back", 890, 100, backText);

        canvas.drawRect(500, 0, 750, 160, backButton);
        canvas.drawText("Music", 537, 100, musicText);

        // 画出得分、失分数
        canvas.drawText("Points:" + points, 15, 80, pointsText);
        // 画出blocks
        for (Block block : blockArray) {
            block.draw(canvas);
        }
    }

    // 设置block的点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {

            for (Block block : blockArray) {
                if (block.getY() <= 1920) { //1200 ||| 1920
                    // 判断是否点击到了block，并相应的加分
                    if (event.getX() >= block.getX() + block.getLineToDraw() * 300 && event.getX() <= block.getX() + 300 + block.getLineToDraw() * 300 && //200 200 200 ||| 300 300 300
                            event.getY() >= block.getY() && event.getY() <= block.getY() + 384 && !block.isScored()) {
                        points++;
                        block.setScored(true);
                    }
                }
            }
            // 判断点击区域是否是关闭按钮的区域，若是则结束游戏
            if (event.getX() >= 800 && event.getY() >= 0 && event.getX() <= 1150 && event.getY() <= 160) {
                gameViewActivity.endGame();
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            } else if (event.getX() >= 500 && event.getY() >= 0 && event.getX() <= 750 && event.getY() <= 160){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();//开始播放
                }
            }
        }
        return true;
    }





    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {
        }
    }
}
