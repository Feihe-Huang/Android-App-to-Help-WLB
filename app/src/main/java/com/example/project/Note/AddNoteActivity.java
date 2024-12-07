package com.example.project.Note;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.sdk.utils.GeneralSimpleResultParser;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.project.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Data.DatabaseHelper;

public class AddNoteActivity extends AppCompatActivity
{
    private EditText mEditText;
    private DatabaseHelper mNoteDB;
    private SQLiteDatabase db;
    private Button saveButton;
    private ImageView ocrBtn;


    // Universal text recognition (high precision) request code (ocr)
    private static final int REQUEST_CODE_ACCURATE_BASIC = 101;

    // ocr应用apiKey
    private String apiKey = "lmGrEWBdeeMR6a6wl2T6oEQ9";

    // ocr应用secretKey
    private String secretKey = "7omPKoOvSMQGGQSEtt79BOL5RIQQVu0i";

    // pop-up dialog
    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mEditText = (EditText) this.findViewById(R.id.text);
        mNoteDB = new DatabaseHelper(this);
        db = mNoteDB.getWritableDatabase();

        ocrBtn = this.findViewById(R.id.ocr);

        //实例化dialog
        mDialog = new AlertDialog.Builder(this);
        initTextSDK();

        saveButton = this.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DbAdd();
                finish();
            }
        });
    }

    public void cancle(View v){
        mEditText.setText("");
        finish();
    }

    private void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put("Content",mEditText.getText().toString());
        cv.put("Time",getTime());
        db.insert("Notes",null,cv);
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    // initialize
    private void initTextSDK() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                Log.d("result-->", "成功！" + token);
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.d("result-->", "失败！" + error.getMessage());
            }
        }, getApplicationContext(), apiKey, secretKey);
    }


    // 获取保存文件
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }


    // 文字识别 （高精度）
    public void highPrecision(View view) {
        Intent intent = new Intent(AddNoteActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
    }

    // activity call-back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 识别成功回调，文字识别
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurateBasic(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {

                            AddNoteActivity.this.onResult(result);
                        }
                    });
        }
    }

    // get identified result
    public void onResult(String result){
        String res = "";
        try {
            GeneralResult jsonResult = new GeneralSimpleResultParser().parse(result);
            List<? extends WordSimple> wordList = jsonResult.getWordList();
            for (WordSimple ws : wordList) {
                res += ws.getWords() + " ";
            }
        }catch (OCRError e){
            e.getMessage();
        } finally {
            // 提示识别成功
            Toast.makeText(this, "Congratulations! Text recognition successfully", Toast.LENGTH_SHORT).show();
            String finalRes = res;
            mDialog.setMessage(res);
            mDialog.setPositiveButton("Use This!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mEditText.append(finalRes);
                }
            });
            mDialog.setNegativeButton("Close", null);
            mDialog.show();
        }
    }
}
