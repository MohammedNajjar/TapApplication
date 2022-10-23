package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.R;
import com.example.tabapplication.model.WallPaper;
import com.example.tabapplication.model.reserve;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperHolder> {

    private List<reserve> wallPaperList;
    WallpaperClickListener listener;

    public WallPaperAdapter(List<reserve> wallPaperList, WallpaperClickListener listener) {
        this.wallPaperList = wallPaperList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WallPaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_wallpaper, parent, false);
        return new WallPaperHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WallPaperHolder holder, int position) {
        reserve re = wallPaperList.get(position);

        holder.bind(wallPaperList.get(position));
        holder.textView.setText(String.valueOf(re.getName()));


    }

    @Override
    public int getItemCount() {

        return wallPaperList.size();
    }

    class WallPaperHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        reserve re;

        public WallPaperHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageWallPaper);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(reserve wallPaper) {
            this.re = wallPaper;
            Picasso.get().load(wallPaper.getImage()).placeholder(R.drawable.logo).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onWallpaperClicked(wallPaper);
                }
            });

        }
    }
    public interface WallpaperClickListener {
        void onWallpaperClicked(reserve wallPaper);
    }
}
