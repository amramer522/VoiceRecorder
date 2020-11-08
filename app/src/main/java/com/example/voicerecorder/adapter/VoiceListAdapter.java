package com.example.voicerecorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voicerecorder.R;
import com.example.voicerecorder.helper.TimeAgo;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceListAdapter extends RecyclerView.Adapter<VoiceListAdapter.Hold>
{
    private File[] allFiles;
    private TimeAgo timeAgo;
    private onItemListClick onItemListClick;

    public VoiceListAdapter(File[] allFiles , onItemListClick onItemListClick)
    {
        this.allFiles =allFiles;
        this.onItemListClick= onItemListClick;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        timeAgo = new TimeAgo();
        return new Hold(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position)
    {
        holder.title.setText(allFiles[position].getName());
        holder.date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    class Hold extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView date;
        public Hold(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_list_img);
            title = itemView.findViewById(R.id.item_list_title);
            date = itemView.findViewById(R.id.item_list_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListClick.onClickListner(allFiles[getAdapterPosition()],getAdapterPosition());
        }
    }

    public interface onItemListClick
    {
        void onClickListner(File file,int position);
    }
}
