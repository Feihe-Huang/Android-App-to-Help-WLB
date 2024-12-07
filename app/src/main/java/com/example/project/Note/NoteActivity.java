package com.example.project.Note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Adapters.NoteAdapter;
import com.example.project.R;

import Data.DatabaseHelper;

public class NoteActivity extends AppCompatActivity {
    private ImageView addBtn;
    private ListView mList;
    private Intent mIntent;
    private NoteAdapter mAdapter;
    private DatabaseHelper mNotedb;
    private Cursor cursor;
    private SQLiteDatabase dbreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mList = findViewById(R.id.list);
        addBtn = findViewById(R.id.add);
        mNotedb = new DatabaseHelper(this);

        dbreader = mNotedb.getReadableDatabase();
        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mIntent = new Intent(NoteActivity.this, AddNoteActivity.class);
                startActivity(mIntent);
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(NoteActivity.this, ReadNoteActivity.class);
                intent.putExtra("id",cursor.getInt(cursor.getColumnIndex("id")));
                intent.putExtra("Content",cursor.getString(cursor.getColumnIndex("Content")));
                intent.putExtra( "Time",cursor.getString(cursor.getColumnIndex("Time")));
                startActivity(intent);

            }
        });
    }
    public void add(View v){
        mIntent = new Intent(this, AddNoteActivity.class);
        startActivity(mIntent);
    }

    public void selectDb(){
        cursor = dbreader.query("Notes",null,null,null,null,null,null);
        mAdapter = new NoteAdapter(this,cursor);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        selectDb();
    }
}

