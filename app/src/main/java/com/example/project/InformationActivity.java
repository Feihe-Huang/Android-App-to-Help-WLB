package com.example.project;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import Data.DatabaseHelper;
import Model.User;
import Util.Tools;

public class InformationActivity extends AppCompatActivity {

    // 初始化button，textview等组件
    TextView textViewName, textViewPassword, textViewEmail, textViewLogout;

    Button btnEditDetails, btnChangePassword;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    String username;
    String email;
    String password;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        // 找到各个组件
        textViewName = findViewById(R.id.textViewName);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewLogout = findViewById(R.id.textViewLogout);

        // 获取首页传来的id，找到对应用户
        final Bundle uId = getIntent().getExtras();
        userId = Integer.parseInt(uId.getString("userID"));
        DatabaseHelper db = new DatabaseHelper(InformationActivity.this);
        // 将用户数据存入名为userData的array中
        db.show_user(userId);

        // 得到用户的各个信息
        username = Tools.userData.get(0)[0];
        email = Tools.userData.get(0)[2];
        password = Tools.userData.get(0)[1];

        // 将各个信息写入个人信息栏
        textViewName.setText(username);
        textViewPassword.setText(password);
        textViewEmail.setText(email);

        // 退出登录
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, MainActivity.class));
            }
        });

        // 点击按钮编辑信息/更改密码
        btnEditDetails = findViewById(R.id.btnEditDetails);
        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDetails();
            }
        });

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    // 编辑用户信息
    public void editUserDetails() {
        // 出现对话框
        alertDialogBuilder = new AlertDialog.Builder(InformationActivity.this);
        inflater = LayoutInflater.from(InformationActivity.this);
        // 弹出更改信息的界面（xml）
        final View view = inflater.inflate(R.layout.popup, null);
        final EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        // 出现对话框
        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();
        // 更改用户信息
        final User user = new User();
        user.setId(userId);
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        // 赋值
        editTextUsername.setText(user.getUserName());
        editTextEmail.setText(user.getEmail());

        // 连接数据库
        final DatabaseHelper db = new DatabaseHelper(InformationActivity.this);

        Button saveButton = view.findViewById(R.id.saveButton);
        // 点击保存时
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更改数据库用户信息
                user.setUserName(editTextUsername.getText().toString());
                user.setEmail(editTextEmail.getText().toString());
                Log.d("TAG" ,editTextUsername.getText().toString() );


                if (!editTextUsername.getText().toString().isEmpty()
                        && !editTextEmail.getText().toString().isEmpty()) {
                    // 更新数据库信息
                    db.updateUser(user);
                    Snackbar.make(v, "Details Saved!", Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(InformationActivity.this, MainActivity.class));
                } else {
                    Snackbar.make(view, "Can't save empty information", Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();

            }
        });
    }

    public void clickBack(View view) {
        finish();
    }

    // 更改密码
    public void changePassword() {
        // 出现对话框
        alertDialogBuilder = new AlertDialog.Builder(InformationActivity.this);
        inflater = LayoutInflater.from(InformationActivity.this);
        // 弹出更改框
        final View view = inflater.inflate(R.layout.popup_password, null);
        final EditText editTextPasswordPopup = view.findViewById(R.id.editTextPasswordPopup);
        final EditText editTextConfPasswordPopup = view.findViewById(R.id.editTextConfPasswordPopup);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        // 创建新用户为以后更新用户信息作准备
        final User user = new User();
        // 转存用户信息
        user.setId(userId);
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);

        // 连接数据库
        final DatabaseHelper db = new DatabaseHelper(InformationActivity.this);

        Button saveButtonPassword = view.findViewById(R.id.saveButtonPassword);
        // save the changed password
        saveButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check", editTextConfPasswordPopup.getText().toString());
                Log.d("check", editTextPasswordPopup.getText().toString());
                // 若两次密码一致
                if (Integer.parseInt(editTextPasswordPopup.getText().toString()) == Integer.parseInt(editTextPasswordPopup.getText().toString()) ) {
                    user.setPassword(editTextPasswordPopup.getText().toString());
                    // 更新用户信息
                    db.updateUser(user);
                    Toast.makeText(InformationActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Details Saved!", Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(InformationActivity.this, MainActivity.class));
                } else{
                    Snackbar.make(view, "Can't save empty content ", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(InformationActivity.this, "Can't save empty content", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

    }
}
