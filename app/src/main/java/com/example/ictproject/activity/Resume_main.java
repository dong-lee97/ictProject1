package com.example.ictproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.ictproject.R;



public class Resume_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_main);

        Button button1 = findViewById(R.id.call1_button);
        ImageView back = findViewById(R.id.back);
        TextView mEditTextFileName = findViewById(R.id.edit_text_file_mName);
        TextView mEditTextAge = findViewById(R.id.edit_text_mAge);
        TextView mExperience = findViewById(R.id.mExperience);
        TextView mDetail = findViewById(R.id.mExperienceDetail);
        TextView mRegion = findViewById(R.id.mPossible_region);
        TextView mDay = findViewById(R.id.mPossible_day);
        ImageView mImageView = findViewById(R.id.mImage_view);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String imageUrl = intent.getExtras().getString("imageUrl");
        String age = intent.getExtras().getString("age");
        String experience = intent.getExtras().getString("experience");
        String detail = intent.getExtras().getString("detail");
        String region = intent.getExtras().getString("region");
        String day = intent.getExtras().getString("day");
        String sex = intent.getExtras().getString("sex");
        String age_sex = age + "(" + sex + ")";
        final String uid = intent.getExtras().getString("ResumeUid");

        mEditTextFileName.setText(name);
        mEditTextAge.setText(age_sex);
        mExperience.setText(experience);
        mDetail.setText(detail);
        mRegion.setText(region);
        mDay.setText(day);

        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .into(mImageView);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resume_main.this, ConnectActivity.class);
                intent.putExtra("ResumeUid", uid);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resume_main.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}