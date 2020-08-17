package com.example.ictproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ictproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Resume_main extends AppCompatActivity {

    private TextView mEditTextFileName, mEditTextAge, mExperience, mRegion, mDay;
    private ImageView mImageView;
    private Button button1;
    private String myUid;
    private DatabaseReference cDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_main);

        button1 = findViewById(R.id.call1_button);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        mEditTextFileName = findViewById(R.id.edit_text_file_mName);
        mEditTextAge = findViewById(R.id.edit_text_mAge);
        mExperience = findViewById(R.id.mExperience);
        mRegion = findViewById(R.id.mPossible_region);
        mDay = findViewById(R.id.mPossible_day);
        mImageView = findViewById(R.id.mImage_view);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("name");
        String imageUrl = intent.getExtras().getString("imageUrl");
        String age = intent.getExtras().getString("age");
        String experience = intent.getExtras().getString("experience");
        String region = intent.getExtras().getString("region");
        String day = intent.getExtras().getString("day");
        final String uid = intent.getExtras().getString("ResumeUid");

        mEditTextFileName.setText(name);
        mEditTextAge.setText(age);
        mExperience.setText(experience);
        mRegion.setText(region);
        mDay.setText(day);

        Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerCrop()
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
    }
}