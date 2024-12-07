package com.example.project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.project.R;
import Data.DatabaseHelper;
import com.example.project.Fragments.DoneFragment;


public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.RecyclerViewHolder> {
    private ArrayList<String> todoTask =new ArrayList<>();
    private ArrayList<String> todoCategory = new ArrayList<>();
    private ArrayList<String> todoDetail = new ArrayList<>();
    DoneFragment ft;
    Context con;
    DatabaseHelper db;

    public DoneAdapter(Context con, DoneFragment ft, DatabaseHelper db, ArrayList<String> todoTask, ArrayList<String> todoCategory, ArrayList<String> todoDetail) {
        this.todoTask = todoTask;
        this.todoCategory = todoCategory;
        this.todoDetail = todoDetail;
        this.con = con;
        this.ft=ft;
        this.db=db;

    }

    @NonNull
    @Override
    public DoneAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recyclerview_layout, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        db.getDoneTasks();
        String dTask= todoTask.get(position);
        String dCategory = todoCategory.get(position).replace("''", "'");
        String dDetail = todoDetail.get(position).replace("''", "'");
        holder.task.setText(dTask);
        holder.taskDetail.setText(dDetail);
        holder.taskCategory.setText(dCategory);
    }

    @Override
    public int getItemCount() {
        return todoTask.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView task, taskDetail, taskCategory;
        Button ic_done;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            taskDetail = itemView.findViewById(R.id.task_detail);
            taskCategory = itemView.findViewById(R.id.task_category);
            ic_done = itemView.findViewById(R.id.addTo);
            ic_done.setBackgroundResource(R.drawable.done);

        }
    }
}
