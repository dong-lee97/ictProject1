package com.example.ictproject.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ictproject.R;
import com.example.ictproject.activity.Resume_main;
import com.example.ictproject.upload.Upload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public ImagesAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewExperience.setText(uploadCurrent.getExperience());
        holder.textViewRegion.setText(uploadCurrent.getRegion());
        holder.textViewDay.setText(uploadCurrent.getDay());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Resume_main.class);
                intent.putExtra("name", uploadCurrent.getName());
                intent.putExtra("imageUrl", uploadCurrent.getImageUrl());
                intent.putExtra("age", uploadCurrent.getAge());
                intent.putExtra("experience", uploadCurrent.getExperience());
                intent.putExtra("region", uploadCurrent.getRegion());
                intent.putExtra("day", uploadCurrent.getDay());
                intent.putExtra("ResumeUid", uploadCurrent.getUid());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewExperience, textViewRegion, textViewDay;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewExperience = itemView.findViewById(R.id.text_view_experience);
            textViewRegion = itemView.findViewById(R.id.text_view_region);
            textViewDay = itemView.findViewById(R.id.text_view_day);
        }
    }
}
