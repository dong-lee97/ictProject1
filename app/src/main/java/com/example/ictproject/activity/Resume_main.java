package com.example.ictproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ictproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Resume_main extends AppCompatActivity {

    private TextView mEditTextFileName, mEditTextAge, mExperience, mRegion, mDay, mDetail;
    private String myUid;
    private DatabaseReference cDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_main);

        Button button1 = findViewById(R.id.call1_button);
        ImageView back = findViewById(R.id.back);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        mEditTextFileName = findViewById(R.id.edit_text_file_mName);
        mEditTextAge = findViewById(R.id.edit_text_mAge);
        mExperience = findViewById(R.id.mExperience);
        mDetail = findViewById(R.id.mExperienceDetail);
        mRegion = findViewById(R.id.mPossible_region);
        mDay = findViewById(R.id.mPossible_day);
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
                cDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("company").hasChild(myUid)){
                            Intent intent1 = new Intent(Resume_main.this, ConnectActivity.class);
                            intent1.putExtra("ResumeUid", uid);
                            startActivity(intent1);
                        } else {
                            Intent intent2 = new Intent(Resume_main.this, ConnectActivity1.class);
                            intent2.putExtra("ResumeUid", uid);
                            startActivity(intent2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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