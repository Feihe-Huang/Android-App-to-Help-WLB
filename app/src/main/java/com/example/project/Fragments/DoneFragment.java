package com.example.project.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapters.DoneAdapter;
import com.example.project.R;

import java.util.ArrayList;

import Data.DatabaseHelper;
import Util.Tools;


public class DoneFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> getTasks = new ArrayList<>();
    ArrayList<String> getCategory = new ArrayList<>();
    ArrayList<String> getDetail = new ArrayList<>();
    DatabaseHelper db;
    DoneAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);
        // find recycleView等组件，设置存储数据库中数据的格式
        recyclerView = view.findViewById(R.id.recycleView);
        db = new DatabaseHelper(getActivity());
        db.getDoneTasks();
        setDataInList();
        return view;
    }

    // 将数据/todo列表显示到首页的recycle list中
    public void setDataInList() {
        // 设置任务的内容
        for (int i = 0; i < Tools.doneData.size(); i++) {
            getTasks.add(Tools.doneData.get(i)[2]);
            getCategory.add(Tools.doneData.get(i)[4]);
            getDetail.add(Tools.doneData.get(i)[3]);
        }
        // 存入recycle view中
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DoneAdapter(getActivity(), DoneFragment.this, db, getTasks, getCategory, getDetail);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}