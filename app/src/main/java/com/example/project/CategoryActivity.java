package com.example.project;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapters.CategoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Data.DatabaseHelper;
import Util.Tools;


public class CategoryActivity extends AppCompatActivity {
    // 初始化button、db各个组件
    RecyclerView rv_labels;
    DatabaseHelper db;
    CategoryAdapter adapter;
    ArrayList<String> getLabels = new ArrayList<>();
    FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        // find 组件
        rv_labels = findViewById(R.id.rv_labels);
        addBtn = findViewById(R.id.label_floatingbtn);

        db = new DatabaseHelper(getApplicationContext());
        db.getCategories();

        // 存入categoryData array
        for (int i = 0; i < Tools.categoryData.size(); i++) {
            getLabels.add(Tools.categoryData.get(i));
        }

        rv_labels.setLayoutManager(new LinearLayoutManager(this));
        // 设置滑动删除
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rv_labels);
        adapter= new CategoryAdapter(this, db, getLabels);
        rv_labels.setAdapter(adapter);
        rv_labels.setLayoutManager(new LinearLayoutManager(this));

        // 点击add button 添加label
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出对话框
                final Dialog d = new Dialog(CategoryActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.add_category);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 找到保存按钮
                Button saveBtn=d.findViewById(R.id.saveLabelButton);
                final EditText editText = d.findViewById(R.id.label);
                WindowManager.LayoutParams lp = d.getWindow().getAttributes();
                lp.dimAmount=0.9f;
                d.getWindow().setAttributes(lp);
                // 点击保存后事件
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 存储信息到数据库
                        String entered_text = editText.getText().toString().replace("'","''");
                        db.addCategory(entered_text);
                        Tools.categoryData.add(entered_text);
                        db.getCategories();
                        // 回到label界面
                        Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                });d.show();
            }

        });
    }

    // 滑动删除一个label
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        // 滑动删除
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            // 如果滑动
            if (direction == 8) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                // 询问是否确定删除
                builder.setMessage("Do you really want to delete this?");
                builder.setCancelable(true);

                // 点击确定删除任务
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 在数据库中删除
                        int i = viewHolder.getAdapterPosition();
                        db.deleteCategory(getLabels.get(i));
                        // 回到label界面
                        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        dialog.cancel();
                    }
                });

                // 取消删除
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 回到label界面
                        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        dialog.dismiss();
                    }
                });
                // 显示询问是否删除的对话框
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    // 点击back返回todo界面
    public void clickBack(View view) {
        Intent intent = new Intent(CategoryActivity.this, TodoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoryActivity.this, TodoActivity.class);
        startActivity(intent);
        finish();
    }
}
