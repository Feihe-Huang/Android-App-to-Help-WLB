package com.example.project.Note;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

import Data.DatabaseHelper;


public class ReadNoteActivity extends AppCompatActivity {
    private TextView mTextview;
    private TextView time;
    private DatabaseHelper mDb;
    private SQLiteDatabase mSql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        mTextview = (TextView)this.findViewById(R.id.showtext);
        time = (TextView)this.findViewById(R.id.showtime);
        mDb = new DatabaseHelper(this);
        mSql = mDb.getWritableDatabase();
        mTextview.setText(getIntent().getStringExtra("Content"));
        time.setText(getIntent().getStringExtra("Timer"));
    }
    public void delete(View v) {
        int id = getIntent().getIntExtra("id",0);
        mSql.delete("Notes"," id = " + id,null);
        finish();

    }
    public void goBack(View v) {
        finish();
    }
}
