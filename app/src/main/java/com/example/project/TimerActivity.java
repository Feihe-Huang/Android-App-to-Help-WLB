package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import Model.Timer;

public class TimerActivity extends AppCompatActivity {

    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private Button countdownStartBtn;
    private Button positiveStartBtn;
    private Button pauseBtn;
    private Button cancelBtn;
    private ProgressBar progressBar;
    private TextView mTimeLeftTextView;
    private Handler mHandler;
    private Timer timerCountdown;
    private Timer timerPositive;
    Boolean positiveClicked;
    private TextView hourText;
    private TextView minutesText;
    private TextView secondText;
    // music
    ImageView musicImg;
    private MediaPlayer mediaPlayer, mediaPlayer2;
    Animation animation;



    // 当用户点击倒计时是使用
    private final Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            // 更新UI来显示剩余时间和进度
            mTimeLeftTextView.setText(timerCountdown.toString());
            int progress = timerCountdown.getProgressPercent();
            progressBar.setProgress(progress);

            // Only post Runnable if more time remains
            if (progress == 100) {
                timerCompleted();
            } else {
                // 设定进度条更新的速度（UI更新的速度）
                mHandler.postDelayed(this, 200);
            }
        }
    };

    // 当用户点击正计时时使用
    private final Runnable positiveRunnable = new Runnable() {
        @Override
        public void run() {
            // 更新UI来显示时间
            mTimeLeftTextView.setText(timerPositive.toString());
            mHandler.postDelayed(this, 200);
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // 设置音乐、动画
        mediaPlayer = MediaPlayer.create(this, R.raw.light_music);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.piano_music);
        animation = AnimationUtils.loadAnimation(this, R.animator.rotate);

        // 初次进入隐藏计时器和进度条。
        mTimeLeftTextView = findViewById(R.id.clock); // 计时器时钟
        mTimeLeftTextView.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progress_bar); // 进度条
        progressBar.setVisibility(View.INVISIBLE);
        // find 5个button
        countdownStartBtn = findViewById(R.id.countdownStart);
        positiveStartBtn = findViewById(R.id.positiveStart);
        pauseBtn = findViewById(R.id.pause_button);
        cancelBtn = findViewById(R.id.cancel_button);
        View todoBtn = findViewById(R.id.navTodo);
        musicImg = findViewById(R.id.music);

        // find text
        secondText = findViewById(R.id.seconds_text_view);
        minutesText = findViewById(R.id.minutes_text_view);
        hourText = findViewById(R.id.hours_text_view);

        // 计时器开始前隐藏暂停和取消按钮。
        pauseBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);

        // 在Number Picker中设置显示2位数字
        NumberPicker.Formatter numFormat = i -> new DecimalFormat("00").format(i);

        // 为所有picker中设置最小值和最大值
        hoursPicker = findViewById(R.id.hours_picker);
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(99);
        hoursPicker.setFormatter(numFormat);
        // minutes
        minutesPicker = findViewById(R.id.minutes_picker);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setFormatter(numFormat);
        // seconds
        secondsPicker = findViewById(R.id.seconds_picker);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(numFormat);

        // 初始化timer模型
        timerCountdown = new Timer(false);
        timerPositive = new Timer(true);

        // Instantiate Handler so Runnable can be posted to UI message queue.
        mHandler = new Handler(Looper.getMainLooper());

        // 长按切换音乐
        musicImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    mediaPlayer2.start();
                    Toast.makeText(TimerActivity.this, "Music Changed Successfully", Toast.LENGTH_LONG).show();
                } else if (mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();
                    mediaPlayer.start();
                    Toast.makeText(TimerActivity.this, "Music Changed Successfully", Toast.LENGTH_LONG).show();
                } else {
                    mediaPlayer2.start();
                    musicImg.startAnimation(animation);
                    Toast.makeText(TimerActivity.this, "Music Changed Successfully", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        // 跳转todo界面
        todoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 关闭音乐
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    musicImg.clearAnimation();
                }
                if (mediaPlayer2.isPlaying()) {
                    mediaPlayer2.pause();
                    musicImg.clearAnimation();
                }
                // 跳转
                Intent intent = new Intent(TimerActivity.this, TodoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 点击音乐播放图标，开始/暂停播放音乐
        musicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    musicImg.clearAnimation();
                } else if (mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();
                    musicImg.clearAnimation();
                } else {
                    mediaPlayer.start();//开始播放
                    musicImg.startAnimation(animation);
                }
            }
        });
    }

    // 点击返回按钮时
    public void clickBack(View view) {
        // 关闭音乐
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            // 结束动画
            musicImg.clearAnimation();
        }
        if (mediaPlayer2.isPlaying()){
            mediaPlayer2.pause();
            // 结束动画
            musicImg.clearAnimation();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // 关闭音乐
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            musicImg.clearAnimation();
        }
        if (mediaPlayer2.isPlaying()) {
            mediaPlayer2.pause();
            musicImg.clearAnimation();
        }
        finish();
    }

    // 当点击positive start时
    public void positiveStartBtn(View view){
        // 设定boolean的值为true
        positiveClicked = true;

        // 让进度条和时钟显示出来
        mTimeLeftTextView.setVisibility(View.VISIBLE);

        // 只显示暂停和取消按钮
        countdownStartBtn.setVisibility(View.GONE);
        positiveStartBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        pauseBtn.setText("Pause");
        cancelBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        secondsPicker.setVisibility(View.GONE);
        minutesPicker.setVisibility(View.GONE);
        hoursPicker.setVisibility(View.GONE);
        minutesText.setVisibility(View.GONE);
        hourText.setVisibility(View.GONE);
        secondText.setVisibility(View.GONE);


        // timer开始
        timerPositive.positiveStart();

        // Start sending Runnables to message queue.
        mHandler.post(positiveRunnable);
    }

    // 当用户点击开始倒计时
    public void startButtonClick(View view) {
        // boolean值设为负表明不是正计时
        positiveClicked = false;

        // 从NumberPickers中获取时间
        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();
        int seconds = secondsPicker.getValue();

        // 判断用户是否设定了时间
        if (hours + minutes + seconds > 0) {
            // 让进度条和时钟显示出来
            mTimeLeftTextView.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);

            // 只显示暂停和取消按钮
            countdownStartBtn.setVisibility(View.GONE);
            positiveStartBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            pauseBtn.setText("Pause");
            cancelBtn.setVisibility(View.VISIBLE);
            secondsPicker.setVisibility(View.GONE);
            minutesPicker.setVisibility(View.GONE);
            hoursPicker.setVisibility(View.GONE);
            minutesText.setVisibility(View.GONE);
            hourText.setVisibility(View.GONE);
            secondText.setVisibility(View.GONE);

            // timer开始
            timerCountdown.countdownStart(hours, minutes, seconds);

            // Start sending Runnable to message queue.
            mHandler.post(countdownRunnable);
        } else {
            Toast.makeText(this, "Please set the timer first", Toast.LENGTH_SHORT).show();
        }
    }

    // 正在倒计时时点击暂停，暂停后再次点击继续计时
    public void clickPause(View view) {
        if (timerCountdown.isRunning() || timerPositive.isRunning()) {
            // 点击暂停，判断现阶段是正计时还是倒计时
            if (!positiveClicked){
                timerCountdown.pause();
                mHandler.removeCallbacks(countdownRunnable);
            } else {
                timerPositive.pause();
                mHandler.removeCallbacks(positiveRunnable);
            }
            pauseBtn.setText("Resume");
        } else {
            // 继续计时并将恢复按钮更改为暂停按钮，同时判断当前计时状态
            if (!positiveClicked){
                timerCountdown.resume();
                mHandler.post(countdownRunnable);
            } else {
                timerPositive.resume();
                mHandler.post(positiveRunnable);
            }
            pauseBtn.setText("Pause");
        }
    }

    // 取消计时，回到初始状态
    public void cancelButtonClick(View view) {
        mTimeLeftTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        timerCompleted();
    }

    // 计时完成，回到初始状态
    private void timerCompleted() {

        // Determines the current timing status, and
        // Remove any remaining Runnable that may reside in UI message queue
        if (!positiveClicked){
            timerCountdown.stop();
            mHandler.removeCallbacks(countdownRunnable);
            Toast.makeText(TimerActivity.this, "Time Completed", Toast.LENGTH_SHORT).show();
        } else {
            timerPositive.stop();
            mHandler.removeCallbacks(positiveRunnable);
        }

        // Show only the start button
        countdownStartBtn.setVisibility(View.VISIBLE);
        positiveStartBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        // show the time picker
        secondsPicker.setVisibility(View.VISIBLE);
        minutesPicker.setVisibility(View.VISIBLE);
        hoursPicker.setVisibility(View.VISIBLE);
        minutesText.setVisibility(View.VISIBLE);
        hourText.setVisibility(View.VISIBLE);
        secondText.setVisibility(View.VISIBLE);
    }
}