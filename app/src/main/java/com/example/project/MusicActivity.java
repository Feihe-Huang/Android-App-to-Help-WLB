package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapters.MusicAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.LocalMusic;


public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    // 声明各个组件
    ImageView nextIv,playIv,prevIv, DVDImage;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    Animation animation;

    List<LocalMusic> mData;//数据源

    private MusicAdapter adapter;//适配器

    MediaPlayer mediaPlayer;//媒体

    int currentPlayPosition = -1; //初始化播放状态为-1

    int currentPausePosition;//初始化暂停状态

    LinearLayoutManager layoutManager;//设置布局管理器


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        transparentStatusBar();//透明状态栏方法
        setContentView(R.layout.activity_music);
        // find
        initView();
        // 初始化player
        mediaPlayer = new MediaPlayer();
        mData = new ArrayList<>();
        //创建适配器
        adapter = new MusicAdapter(this, mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据原
        loadLoalMusicData();
        //设置每一项的点击事件
//        setEventListener();

        animation = AnimationUtils.loadAnimation(this, R.animator.rotate);


        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPlayPosition = position;
                LocalMusic musicBean = mData.get(position);
                playMusicInBean(musicBean);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 当用户点击前一首音乐
            case R.id.local_music_bottom_iv_prev:
                if (currentPlayPosition == 0) {
                    //已经是第一首
                    Toast.makeText(this,"This is already the first music",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    // 切换为前一首歌曲
                    currentPlayPosition = currentPlayPosition - 1;
                    LocalMusic lastBean = mData.get(currentPlayPosition);
                    playMusicInBean(lastBean);
                    break;
                }
                // 点击播放音乐
            case R.id.local_music_bottom_iv_play:
                if (currentPlayPosition == -1) {
                    //并没有选中音乐，从第一首歌开始播放
                    currentPlayPosition = currentPlayPosition + 1;
                    LocalMusic nextBean = mData.get(currentPlayPosition);
                    playMusicInBean(nextBean);
                    return;
                    // 当正在播放时，暂停
                }else if (mediaPlayer.isPlaying()){
                    //暂停音乐
                    pauseMusic();
                    //暂停渲染
//                    visualizerManager.pause();
                }else{
                    //从暂停位置开始播放音乐
                    playMusic();
                    //恢复渲染
//                    visualizerManager.resume();
                }
                break;
            // 点击next下一首时
            case R.id.local_music_bottom_iv_next:
                if (currentPlayPosition == mData.size()-1) {
                    //已经是最后一首
                    Toast.makeText(this,"No more music here",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    // 切换到下一首
                    currentPlayPosition = currentPlayPosition + 1;
                    LocalMusic nextBean = mData.get(currentPlayPosition);
                    playMusicInBean(nextBean);
                    break;
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    public void playMusicInBean(LocalMusic musicBean) {

        //底部同步显示歌曲信息
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        stopMusic();
        //重置播放器
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /***
     * 播放音乐按钮的方法
     */
    private void playMusic() {
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            DVDImage.startAnimation(animation);
            //等于0（之前没有播放过）重新开始播放
            if (currentPausePosition == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch (IOException e){
                    e.printStackTrace();
                }
                // 播放按钮变成暂停按钮
                playIv.setImageResource(R.drawable.ic_pause);
            }else {
                //从暂停的位置开始播放
                mediaPlayer.seekTo(currentPausePosition);
                mediaPlayer.start();
                // 播放按钮变成暂停按钮
                playIv.setImageResource(R.drawable.ic_pause);
            }
        }
    }

    /***
     * 暂停音乐按钮的方法
     */
    private void pauseMusic() {
        // 正字啊播放时
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            currentPausePosition = mediaPlayer.getCurrentPosition();
            // 暂停
            mediaPlayer.pause();
            // 暂停键换成播放键
            playIv.setImageResource(R.drawable.ic_play);
        }
        DVDImage.clearAnimation();
    }

    // 停止并结束音乐，用于销毁
    private void stopMusic() {
        if (mediaPlayer != null){
            currentPausePosition = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.drawable.ic_play);
        }
        DVDImage.clearAnimation();
    }

    /**
     * 加载本地mp3数据
     */
    private void loadLoalMusicData() {
        //获取contentResolver
        ContentResolver resolver = getContentResolver();
        //获取本地音乐的uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null,null);
        //遍历cursor
        int id = 0;
        while (cursor.moveToNext()){
            // 遍历获取音乐的各个信息
            @SuppressLint("Range") String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            @SuppressLint("Range") String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            @SuppressLint("Range") Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            //将一行的数据封装到对象
            LocalMusic bean = new LocalMusic(sid, song, singer, album, time, path);
            mData.add(bean);
        }
        //数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }

    private void initView(){
        checkPermission();
        //初始化控件
        DVDImage = findViewById(R.id.DVDImage);
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        prevIv = findViewById(R.id.local_music_bottom_iv_prev);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        prevIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        nextIv.setOnClickListener(this);
    }

    /**
     * 获取权限
     */
    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 写sd卡权限
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            // 读取存储信息权限
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            // 录制音频权限
            if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            // 如果全部允许
            if (!isGranted) {
                this.requestPermissions(new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        },
                        102);
            }
        }
    }
}