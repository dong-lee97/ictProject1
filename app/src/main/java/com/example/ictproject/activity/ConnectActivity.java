package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ictproject.R;
import com.example.ictproject.SendNotification;
import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Upload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConnectActivity extends AppCompatActivity {

    private Button button2, cancel_button;
    private EditText company, phone;
    private FirebaseUser user;
    private String uid, ResumeUid, mPushToken;
    private DatabaseReference cDatabaseRef;
    private CollectionReference collectionReference;
    private CompanyUpload cUpload;
    private Upload mUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        button2 = findViewById(R.id.call2_button);
        cancel_button = findViewById(R.id.cancel_button);
        company = findViewById(R.id.rCompanyName);
        phone = findViewById(R.id.rCompanyPhone);

        cDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
        collectionReference = FirebaseFirestore.getInstance().collection("token");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        ResumeUid = intent.getExtras().getString("ResumeUid");

        //company 가 연락하는 경우
        cDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("company").hasChild(uid)) {
                    cUpload = dataSnapshot.child("company").child(uid).getValue(CompanyUpload.class);
                    company.setText(cUpload.getCName());
                    phone.setText(cUpload.getCPhone());

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionReference.document(ResumeUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    mPushToken = documentSnapshot.getString("pushToken");
                                    SendNotification.sendNotification(mPushToken, "shareAbility", cUpload.getCName() + "에서 연락이 왔습니다!" , cUpload);
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    mUpload = dataSnapshot.child("employee").child(uid).getValue(Upload.class);
                    company.setText(mUpload.getCompanyName());
                    phone.setText(mUpload.getCompanyPhone());

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionReference.document(ResumeUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    mPushToken = documentSnapshot.getString("pushToken");
                                    SendNotification.sendNotification(mPushToken, "shareAbility", mUpload.getCompanyName() + "에서 연락이 왔습니다!" , mUpload);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}