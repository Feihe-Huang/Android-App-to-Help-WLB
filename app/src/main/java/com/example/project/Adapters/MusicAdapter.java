package com.example.project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

import Model.LocalMusic;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.LocalMusicViewHolder> {

    Context context;
    List<LocalMusic> mDatas;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    //通过接口回调的方式
    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);
    }


    public MusicAdapter(Context context, List<LocalMusic> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_music,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        return holder;
    }

    /***
     * 适配器从mdata数据中一一匹配到ui中
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LocalMusic musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.timeTv.setText(musicBean.getDuration());
        holder.albumTv.setText(musicBean.getAlbum());

        //可以通过itemiew获取每一项的歌曲
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder{

        TextView idTv,singerTv,albumTv,timeTv,songTv;

        public LocalMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            songTv = itemView.findViewById(R.id.item_local_music_song);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_durtion);
        }
    }
}
