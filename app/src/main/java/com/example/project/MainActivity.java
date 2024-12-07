package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import Data.DatabaseHelper;
import Model.User;

public class MainActivity extends AppCompatActivity {
    // 初始化button，textview等组件
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //得到videoView
        VideoView myVideoView = (VideoView) findViewById(R.id.video_bg);
        final String videoPath = Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.sea).toString();
        //设置视频路径
        myVideoView.setVideoPath(videoPath);
        //开始播放
        myVideoView.start();
        //设置监听是否准备好
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }});
        //设置监听是否播放完
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myVideoView.setVideoPath(videoPath);
                myVideoView.start();
            }
        });


        // 找到各个组件和数据库
        db = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // 点击跳转注册界面
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        // 点击提交登录信息
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // 检查是否有匹配的账户密码
                Boolean account = db.checkUser(email, password);
//                User user = new User();
//                Log.d("IDdetails", String.valueOf(user.getId()));

                // 如果账号密码匹配
                if (account == true) {
                    // 跳转主页面
                    Intent HomePage = new Intent(MainActivity.this, Homepage.class);

                    // 传输用户信息
                    Bundle b = new Bundle();
                    b.putString("textViewEmail", editTextEmail.getText().toString());
                    b.putString("textViewPassword", editTextPassword.getText().toString());

                    String y = db.selectOneUserSendUserName(email, password);
                    // 用户ID
                    int x = db.selectOneUserSendId(email,password);
                    // 用户信息
                    b.putString("textViewUsername",y);
                    b.putString("userID", String.valueOf(x));

                    HomePage.putExtras(b);
                    startActivity(HomePage);
                } else {
                    Toast.makeText(MainActivity.this, "The account or password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
