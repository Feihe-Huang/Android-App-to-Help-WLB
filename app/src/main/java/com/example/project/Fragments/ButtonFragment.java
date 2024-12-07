package com.example.project.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project.R;


public class ButtonFragment extends Fragment {

    // 初始化buttons
    Button avoided,done,habits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        // find各个按钮
        avoided=view.findViewById(R.id.avoided);
        done=view.findViewById(R.id.done);
        habits=view.findViewById(R.id.habits);

        // 初始界面
        // 切换fragment： task界面
        FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.taskFragment,new TaskFragment());
        fragmentTransaction.commit();

        // 点击Task按键后
        habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置三个按键样式
                habits.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                avoided.setBackgroundColor(Color.TRANSPARENT);
                done.setBackgroundColor(Color.TRANSPARENT);
                // 切换fragment： habit界面
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.taskFragment,new TaskFragment());
                ft.commit();
            }
        });

        // 点击avoid按键后
        avoided.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habits.setBackgroundColor(Color.TRANSPARENT);
                avoided.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                done.setBackgroundColor(Color.TRANSPARENT);
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.taskFragment,new AvoidFragment());
                ft.commit();
            }
        });
        // 点击done按键后
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habits.setBackgroundColor(Color.TRANSPARENT);
                avoided.setBackgroundColor(Color.TRANSPARENT);
                done.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.taskFragment,new DoneFragment());
                ft.commit();
            }
        });
        return view;
    }
}