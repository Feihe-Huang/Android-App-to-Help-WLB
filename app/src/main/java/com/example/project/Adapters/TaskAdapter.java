package com.example.project.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Fragments.TaskFragment;
import com.example.project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Data.DatabaseHelper;
import Util.Tools;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.RecyclerViewHolder> {
    // 初始化
    private ArrayList<String> todoTask = new ArrayList<>();
    private ArrayList<String> todoCategory = new ArrayList<>();
    private ArrayList<String> todoDetail = new ArrayList<>();
    TaskFragment ft;
    Context con;
    Date c = Calendar.getInstance().getTime();
    // 设置数据库存储的数据格式
    DatabaseHelper db;
    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // constructor
    public TaskAdapter(Context con, TaskFragment ft, DatabaseHelper db, ArrayList<String> todoTask, ArrayList<String> todoCategory, ArrayList<String> todoDetail) {
        this.todoTask = todoTask;
        this.todoCategory = todoCategory;
        this.todoDetail = todoDetail;
        this.con = con;
        this.ft = ft;
        this.db = db;
    }

    @NonNull
    @Override
    public TaskAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycleview_tasks, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String dTask = todoTask.get(position);
        try {
            String dCategory = todoCategory.get(position).replace("''", "'");
            String dDetail = todoDetail.get(position).replace("''", "'");
            // 设置task的位置、分类
            holder.task.setText(dTask);
            holder.taskDetail.setText(dDetail);
            holder.taskCategory.setText(dCategory);
            // 后续将加入task的点击事件

        } catch (Exception exception){
            System.out.println(exception);
        }

        // 当用户点击了叉号-》avoid
        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.isTodaySelected == true) {
                    db.getTasks();
                    db.getAvoidedTasks();
                    Log.d("size", "" + Tools.avoidData.size());
                    int count = 0;
                    // 若存在avoid的数据
                    if (Tools.avoidData.size() != 0) {
                        // 与已经存入的数据一一对比
                        for (int i = 0; i < Tools.avoidData.size(); i++) {
                            // 获取task信息
                            String getDate = Tools.avoidData.get(i)[1];
                            String getTask = Tools.avoidData.get(i)[2];
                            // 比对任务及事件判断是否已经将任务加入avoid
                            if ((Tools.taskData.get(position)[2].equals(getTask)) && (getDate.equals(df.format(c)))) {
                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                // 只要有已经存在的，就对count + 1
                                count++;
                            }
                        }
                        // 若count仍然为0
                        if (count == 0) {
                            // 将数据存入数据库avoid的table
                            db.addDataToTable("Avoid", Tools.avoidData.size() + 1, df.format(c), Tools.taskData.get(position)[2],
                                    Tools.taskData.get(position)[3], Tools.taskData.get(position)[4]);
                            Toast.makeText(con, "    Added To Avoided    ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 若不存在avoid的数据，直接存入avoid中
                        db.addDataToTable("Avoid", Tools.avoidData.size() + 1, df.format(c), Tools.taskData.get(position)[2],
                                Tools.taskData.get(position)[3], Tools.taskData.get(position)[4]);
                        Toast.makeText(con, "    Added To Avoided    ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 点击对号，加入done中
        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.isTodaySelected == true) {
                    db.getTasks();
                    db.getDoneTasks();
                    int count = 0;
                    // 若存在done的数据
                    if (Tools.doneData.size() != 0) {
                        // 与已经存入的数据一一对比
                        for (int i = 0; i < Tools.doneData.size(); i++) {
                            String getDate = Tools.doneData.get(i)[1];
                            String getTask = Tools.doneData.get(i)[2];
                            if ((Tools.taskData.get(position)[2].equals(getTask)) && (getDate.equals(df.format(c)))) {
                                Toast.makeText(con, "    Already Added    ", Toast.LENGTH_SHORT).show();
                                // 若对比完成，count+1
                                count++;
                            }
                        }
                        // count仍为0
                        if (count == 0) {
                            Log.d("id", "" + Tools.doneData.size());
                            // 加入done的table
                            db.addDataToTable("Done", Tools.doneData.size() + 1, df.format(c), Tools.taskData.get(position)[2],
                                    Tools.taskData.get(position)[3], Tools.taskData.get(position)[4]);
                            Toast.makeText(con, "    Added To Done    ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 若不存在done中的数据，直接加入数据库
                        Log.d("id", "" + Tools.doneData.size());
                        db.addDataToTable("Done", Tools.doneData.size() + 1, df.format(c), Tools.taskData.get(position)[2],
                                Tools.taskData.get(position)[3], Tools.taskData.get(position)[4]);
                        Toast.makeText(con, "    Added To Done    ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoTask.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        Button btn1, btn2;
        TextView task;
        TextView taskDetail, taskCategory;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // 找到个各个按钮
            btn1 = itemView.findViewById(R.id.addToAvoid);
            btn2 = itemView.findViewById(R.id.addToDone);
            task = itemView.findViewById(R.id.task);
            taskDetail = itemView.findViewById(R.id.task_detail);
            taskCategory = itemView.findViewById(R.id.task_category);
        }
    }
}
