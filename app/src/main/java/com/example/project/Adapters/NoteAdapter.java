package com.example.project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project.R;


public class NoteAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;
    public NoteAdapter(Context mContext,Cursor mCursor){
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mCursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycleview_note,null);
        TextView content = (TextView) view.findViewById(R.id.list_content);
        TextView time = (TextView) view.findViewById(R.id.list_time);
        mCursor.moveToPosition(position);
        @SuppressLint("Range") String dbcontext = mCursor.getString(mCursor.getColumnIndex("Content"));
        @SuppressLint("Range") String dbtime = mCursor.getString(mCursor.getColumnIndex("Time"));
        content.setText(dbcontext);
        time.setText(dbtime);
        return view;
    }
}
