package com.example.ictproject.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ictproject.R;
import com.example.ictproject.activity.Resume_main;
import com.example.ictproject.upload.Upload;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<Upload> unfilteredUploads;
    private ArrayList<Upload> filteredUploads;

    public ImagesAdapter(Context context, ArrayList<Upload> uploads) {
        mContext = context;
        unfilteredUploads = uploads;
        filteredUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final Upload uploadCurrent = filteredUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewExperience.setText(uploadCurrent.getExperience());
        holder.textViewRegion.setText(uploadCurrent.getRegion());
        holder.textViewDay.setText(uploadCurrent.getDay());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .override(110, 110)
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
                intent.putExtra("detail", uploadCurrent.getDetail());
                intent.putExtra("region", uploadCurrent.getRegion());
                intent.putExtra("day", uploadCurrent.getDay());
                intent.putExtra("ResumeUid", uploadCurrent.getUid());
                intent.putExtra("sex", uploadCurrent.getSex());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredUploads.size();
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

    void filter(ArrayList<String> experienceList, ArrayList<String> regionList,
                ArrayList<Integer> ageList, ArrayList<String> sexList) {

        String region = regionList.toString().replace("[", "").replace("]", "").trim();
        String age = ageList.toString().replace("[", "").replace("]", "").trim();
        String sex = sexList.toString().replace("[", "").replace("]", "").trim();

        if (experienceList.isEmpty()) {
            if (region.isEmpty()) {
                if (sex.isEmpty()) {
                    if (ageList.isEmpty()) {
                        filteredUploads = unfilteredUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (age.contains(upload.getAge().trim())) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                } else {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (upload.getSex().trim().equals(sex)) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (age.contains(upload.getAge().trim()) && upload.getSex().trim().equals(sex)) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                }
            } else {
                if (sex.isEmpty()) {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (upload.getRegion().trim().contains(region)) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (upload.getRegion().trim().contains(region) && age.contains(upload.getAge().trim())) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                } else {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (upload.getRegion().trim().contains(region) && upload.getSex().trim().equals(sex)) {
                                filteringUploads.add(upload);
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            if (upload.getRegion().trim().contains(region) && upload.getSex().trim().equals(sex)
                                    && age.contains(upload.getAge().trim())) {
                                filteringUploads.add(upload);
                            }

                        }
                        filteredUploads = filteringUploads;
                    }
                }
            }
        } else {
            if (region.isEmpty()) {
                if (sex.isEmpty()) {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (age.contains(upload.getAge().trim()) && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                } else {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getSex().trim().equals(sex) && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (age.contains(upload.getAge().trim()) && upload.getSex().trim().equals(sex)
                                        && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                }
            } else {
                if (sex.isEmpty()) {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getRegion().trim().contains(region) && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getRegion().trim().contains(region) && age.contains(upload.getAge().trim())
                                        && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                } else {
                    if (ageList.isEmpty()) {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getRegion().trim().contains(region) && upload.getSex().trim().equals(sex)
                                        && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    } else {
                        ArrayList<Upload> filteringUploads = new ArrayList<Upload>();
                        for (Upload upload : unfilteredUploads) {
                            for (String ex : experienceList) {
                                if (upload.getRegion().trim().contains(region) && upload.getSex().trim().equals(sex)
                                        && age.contains(upload.getAge().trim()) && upload.getExperience().trim().contains(ex)) {
                                    filteringUploads.add(upload);
                                    break;
                                }
                            }
                        }
                        filteredUploads = filteringUploads;
                    }
                }
            }
        }
    }

}

