package com.example.project;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project.Fragments.ButtonFragment;
import com.example.project.Fragments.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public final class TodoActivity extends AppCompatActivity {
    private Button categoryBtn, timerBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_todo);

        // find 两个button
        categoryBtn = findViewById(R.id.category);
        View timer = findViewById(R.id.navTimer);


        // 创建fragment，指定跳转首页内容
        ButtonFragment fragment = new ButtonFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.container, (Fragment)fragment, fragment.getClass().getSimpleName()).commit();

        // 写标题字体
        TextView title = findViewById(R.id.toolbartext);
        title.setText("TODO List");


        // 设置菜单栏背景
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.gradient_bg));
        }


        // 跳转timer界面
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( TodoActivity.this, TimerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 点击跳转category
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    // 点击返回主页面
    public void clickBack(View view) {
        Intent intent = new Intent(TodoActivity.this, Homepage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TodoActivity.this, Homepage.class);
        startActivity(intent);
        finish();
    }

}
