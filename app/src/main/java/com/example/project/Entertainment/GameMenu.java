package com.example.project.Entertainment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.R;

public class GameMenu extends View {
    GameMenuActivity gameMenuActivity;
    int difficulty;


    public GameMenu(Context context, GameMenuActivity gameMenuActivity) {
        super(context);
        difficulty = 1;
        this.gameMenuActivity = gameMenuActivity;
    }

    // 画布画出菜单界面
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置菜单界面的背景颜色
        canvas.drawRGB(247,239,226);

        // 画出start game button 的样式
        Paint buttonStartGame = new Paint();
        buttonStartGame.setAntiAlias(true);
        buttonStartGame.setStyle(Paint.Style.FILL_AND_STROKE);
        int buttonsColor = getResources().getColor(R.color.backgroundColor);
        buttonStartGame.setColor(buttonsColor);

        // 设定游戏名字的样式
        Paint textNameOfGame = new Paint();
        textNameOfGame.setAntiAlias(true);
        textNameOfGame.setSubpixelText(true);
        int textColor = getResources().getColor(R.color.gameColor);
        textNameOfGame.setColor(textColor);

        // 设定start 文字的样式
        Paint textStartGame = new Paint();
        textStartGame.setAntiAlias(true);
        textStartGame.setSubpixelText(true);
        textStartGame.setColor(textColor);

        // 设定difficulty 文字的样式
        Paint textSetDifficulty = new Paint();
        textSetDifficulty.setAntiAlias(true);
        textSetDifficulty.setSubpixelText(true);
        textSetDifficulty.setColor(textColor);

        Paint textNameOfDifficulty = new Paint();
        textNameOfDifficulty.setAntiAlias(true);
        textNameOfDifficulty.setSubpixelText(true);
        textNameOfDifficulty.setColor(textColor);

        Paint flagDifficulty1 = new Paint();
        flagDifficulty1.setAntiAlias(true);
        flagDifficulty1.setColor(buttonsColor);

        Paint flagDifficulty2 = new Paint();
        flagDifficulty2.setAntiAlias(true);
        flagDifficulty2.setColor(buttonsColor);

        Paint flagDifficulty3 = new Paint();
        flagDifficulty3.setAntiAlias(true);
        flagDifficulty3.setColor(buttonsColor);

        textNameOfGame.setTextSize(190.0f);
        textStartGame.setTextSize(120.0f);
        textSetDifficulty.setTextSize(90.0f);
        textNameOfDifficulty.setTextSize(60.0f);


        flagDifficulty1.setStyle(Paint.Style.FILL_AND_STROKE);
        flagDifficulty2.setStyle(Paint.Style.FILL_AND_STROKE);
        flagDifficulty3.setStyle(Paint.Style.FILL_AND_STROKE);

        // 画出来各个button（长方形）
        canvas.drawRect(200, 520, 1000, 840, buttonStartGame); // 200 520 1000 840
        canvas.drawRect(280, 1400, 408, 1528, flagDifficulty1); // 280 1200 408 1528
        canvas.drawRect(536, 1400, 664, 1528, flagDifficulty2); // 536 1200 664 1528
        canvas.drawRect(792, 1400, 920, 1528, flagDifficulty3); // 792 1200 920 1528
        canvas.drawText("Piano Block", 105, 300, textNameOfGame); // 105 300
        canvas.drawText("Start", 470, 715, textStartGame); // 475 715
        canvas.drawText("Choose Difficulty:", 220, 1120, textSetDifficulty); // 220 1120
        canvas.drawText("Easy", 240, 1320, textNameOfDifficulty); // 240 1320
        canvas.drawText("Middle", 496, 1320, textNameOfDifficulty); // 496 1320
        canvas.drawText("Hard", 752, 1320, textNameOfDifficulty); // 752 1320
    }

    // 点进开始游戏，跳转游戏界面
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            if (event.getX() >= 200 && event.getY() >= 520 && event.getX() <= 1000 && event.getY() <= 840) { //120 360 680 560 ||| 200 520 1000 840
//                    menuCallback.startGameViewActivity(difficulty);
                gameMenuActivity.startGameViewActivity(difficulty);
            }
            if (event.getX() >= 280 && event.getY() >= 1430 && event.getX() <= 408 && event.getY() <= 1558) { //200 800 280 880 ||| 280 1200 408 1328
                difficulty = 1;
                invalidate();
                gameMenuActivity.startGameViewActivity(difficulty);

            }
            if (event.getX() >= 536 && event.getY() >= 1430 && event.getX() <= 664 && event.getY() <= 1558) { //360 800 440 880 ||| 536 1200 664 1328
                difficulty = 2;
                invalidate();
                gameMenuActivity.startGameViewActivity(difficulty);

            }
            if (event.getX() >= 792 && event.getY() >= 1430 && event.getX() <= 920 && event.getY() <= 1558) { //520 800 600 880 ||| 792 1200 920 1328
                difficulty = 3;
                invalidate();
                gameMenuActivity.startGameViewActivity(difficulty);
            }
        }
        // 为true时将执行两次
        return false;
    }
}
