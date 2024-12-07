package com.example.project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

import Data.DatabaseHelper;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {
    private ArrayList<String> categoryList=new ArrayList<>();
    Context con;
    DatabaseHelper db;

    public CategoryAdapter(Context con, DatabaseHelper db, ArrayList<String> categoryList) {
        this.categoryList = categoryList;
        this.con = con;
        this.db=db;

    }

    @NonNull
    @Override
    public CategoryAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycleview_category, viewGroup, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        // 设置category的标题
        holder.categoryName.setText(categoryList.get(position));
        int count = db.getCategoriesAmount(categoryList.get(position));
        if(count>1){
            holder.taskAmount.setText(""+count+ " tasks");
        }
        else{
            holder.taskAmount.setText(""+count+ " tasks");
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView taskAmount, categoryName;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_label);
            taskAmount = itemView.findViewById(R.id.tv_sum);
        }
    }
}
