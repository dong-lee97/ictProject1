package com.example.ictproject.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ictproject.R;
import com.example.ictproject.activity.ConnectActivity;
import com.example.ictproject.upload.Upload;
import java.util.ArrayList;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {
    private Context mContext;
    private ArrayList<Upload> unRecommendUploads;
    private ArrayList<Upload> recommendUploads;

    RecommendAdapter(Context context, ArrayList<Upload> uploads) {
        mContext = context;
        unRecommendUploads = uploads;
        recommendUploads = uploads;
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_recommend, parent, false);
        return new RecommendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        final Upload uploadCurrent = recommendUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewExperience.setText(uploadCurrent.getExperience());
        holder.textViewDetail.setText(uploadCurrent.getDetail());
        holder.textViewRegion.setText(uploadCurrent.getRegion());
        holder.textViewDay.setText(uploadCurrent.getDay());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .override(110, 110)
                .centerCrop()
                .into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConnectActivity.class);
                intent.putExtra("ResumeUid", uploadCurrent.getUid());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recommendUploads.size();
    }

    static class RecommendViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewExperience, textViewDetail, textViewRegion, textViewDay;
        ImageView imageView;
        Button button;


        RecommendViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recommend_image);
            textViewName = itemView.findViewById(R.id.recommend_name);
            textViewExperience = itemView.findViewById(R.id.recommend_experience);
            textViewDetail = itemView.findViewById(R.id.recommend_detail);
            textViewRegion = itemView.findViewById(R.id.recommend_region);
            textViewDay = itemView.findViewById(R.id.recommend_day);
            button = itemView.findViewById(R.id.recommend_button);
        }
    }

    ArrayList<Upload> recommend(ArrayList<String> experienceList, String sex, String ageRange, String needEx) {

        String experience = experienceList.toString().replace("[", "").replace("]", "").trim();

        if (sex.equals("무관")) {
            sex = null;
        }
        switch (ageRange) {
            case "20대 초반":
                ageRange = "20, 21, 22";
                break;
            case "20대 중반":
                ageRange = "23, 24, 25, 26";
                break;
            case "20대 후반":
                ageRange = "27, 28, 29";
                break;
            case "무관":
                ageRange = null;
                break;
        }
        if (needEx.equals("필수")) {
            needEx = experience;
        } else {
            needEx = null;
        }

        if (sex == null) {
            if (ageRange == null) {
                if (needEx == null) {

                } else {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (upload.getExperience().trim().contains(needEx)) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                }
            } else {
                if (needEx == null) {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (ageRange.contains(upload.getAge())) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                } else {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (ageRange.contains(upload.getAge()) && upload.getExperience().trim().contains(needEx)) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                }
            }
        } else {
            if (ageRange == null) {
                if (needEx == null) {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (upload.getSex().trim().equals(sex)) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                } else {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (upload.getSex().trim().equals(sex) && upload.getExperience().trim().contains(needEx)) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                }
            } else {
                if (needEx == null) {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (upload.getSex().trim().equals(sex) && ageRange.contains(upload.getAge())) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                } else {
                    ArrayList<Upload> recommendingUploads = new ArrayList<Upload>();
                    for (Upload upload : unRecommendUploads) {
                        if (upload.getSex().trim().equals(sex) && ageRange.contains(upload.getAge())
                                && upload.getExperience().trim().contains(needEx)) {
                            recommendingUploads.add(upload);
                        }
                    }
                    recommendUploads = recommendingUploads;
                }
            }
        }

        int i = (int) (Math.random() * (recommendUploads.size()));
        Upload upload = recommendUploads.get(i);
        recommendUploads.clear();
        recommendUploads.add(upload);

        return recommendUploads;

    }

}