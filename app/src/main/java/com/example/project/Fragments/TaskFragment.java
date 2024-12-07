package com.example.project.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapters.TaskAdapter;
import com.example.project.R;

import com.example.project.TodoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Data.DatabaseHelper;
import Util.Tools;


public class TaskFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> getTasks = new ArrayList<>();
    ArrayList<String> getCategory = new ArrayList<>();
    ArrayList<String> getDetail = new ArrayList<>();
    FloatingActionButton addButton;
    Date c = Calendar.getInstance().getTime();
    String selectedCategory;
    DatabaseHelper db;
    TaskAdapter adapter;
    TextView tv1, tv2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // find recycleView等组件，设置存储数据库中数据的格式
        recyclerView = view.findViewById(R.id.recycleView);
        db = new DatabaseHelper(getActivity());
        db.getCategories();
        db.getTasks();
        // 提示文本
        tv1 = view.findViewById(R.id.a);
        tv2 = view.findViewById(R.id.b);
        // 添加按钮
        addButton = view.findViewById(R.id.add);
        addButton.bringToFront();
        // 判断现在是否有to-do项，若没有则显示提示文字
        if (Tools.taskData.size() != 0) {
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
        } else if (Tools.taskData.size() == 0) {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
        }

        // 点击添加按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 产生存储到数据库中的信息格式
                final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                final String formattedDate = df.format(c);
                // 出现对话框
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.add_task);
                // 设置对话框样式、格式
                WindowManager.LayoutParams lp = d.getWindow().getAttributes();
                lp.dimAmount = 0.9f;
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = d.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                d.getWindow().setAttributes(lp);

                // 设置对话框中的选择下拉菜单+显示提示文本
                final Spinner spinner = d.findViewById(R.id.spinner);
                final TextView txt = d.findViewById(R.id.txt);
                // 如果没有label时显示提示文本，若有则不显示
                if (Tools.categoryData.size() == 0) {
                    txt.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                } else {
                    txt.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }

                // adapter 用来显示下拉菜单中的各个labels
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Tools.categoryData);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

                // 点击某个下拉单中的选项时
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedCategory = adapterView.getItemAtPosition(i).toString();
                        ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                // 找到保存、标题、详情
                Button saveTaskButton = d.findViewById(R.id.saveTaskButton);
                final EditText habit = d.findViewById(R.id.habit);
                final EditText detail = d.findViewById(R.id.detail);

                // 点击保存按钮
                saveTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 设置标题、详情
                        String habit_text = habit.getText().toString();
                        String detail_text = detail.getText().toString();
                        // 存储到数据库中
                        db.addDataToTable("Tasks",Tools.taskData.size(), formattedDate, habit_text, detail_text, selectedCategory);
                        db.getTasks();
                        // 跳转回主页
                        Intent i = new Intent(getActivity(), TodoActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(0, 0);
                        d.dismiss();
                    }

                });
                spinner.setAdapter(arrayAdapter);
                d.show();
            }
        });
        setDataInList();
        return view;
    }

    // 将数据/todo列表显示到首页的recycle list中
    public void setDataInList() {
        for (int i = 0; i < Tools.taskData.size(); i++) {
            // 设置任务的内容
            getTasks.add(Tools.taskData.get(i)[2]);
            getDetail.add(Tools.taskData.get(i)[3]);
            getCategory.add(Tools.taskData.get(i)[4]);
        }
        // 存入recycle view中
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 允许滑动删除一个task
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        adapter = new TaskAdapter(getActivity(), TaskFragment.this, db, getTasks, getCategory, getDetail);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // 允许滑动删除任务项
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            // 当滑动任务时
            if (direction == 8) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // 询问是否确定删除
                builder.setMessage("Do you really want to delete this?");
                builder.setCancelable(true);

                // 点击确定删除任务
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int i = viewHolder.getAdapterPosition();
                        // 数据库中删除任务
                        db.deleteTask(i);
                        for (int j = i + 1; j < Tools.taskData.size(); j++) {
                            db.updateTaskId(Integer.parseInt(Tools.taskData.get(j)[0]));
                        }
                        // 回到任务界面
                        Intent intent = new Intent(getActivity(), TodoActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);
                        // 关闭对话框
                        dialog.cancel();
                    }
                });

                // 点击取消删除
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 回到任务界面
                        Intent i = new Intent(getActivity(), TodoActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(0, 0);
                        // 关闭对话框
                        dialog.cancel();
                    }
                });
                // 显示询问是否删除的对话框
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };
}