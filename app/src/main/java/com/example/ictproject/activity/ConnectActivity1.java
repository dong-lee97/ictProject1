package com.example.ictproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class ConnectActivity1 extends AppCompatActivity {

    private Button button1, cancel_button;
    private EditText company, phone;
    private FirebaseUser user;
    private String uid, ResumeUid, mPushToken;
    private DatabaseReference cDatabaseRef;
    private CollectionReference collectionReference;
    private Upload mUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);

        button1 = findViewById(R.id.registerSub_button);
        cancel_button = findViewById(R.id.cancel1_button);
        company = findViewById(R.id.subCompanyName);
        phone = findViewById(R.id.subCompanyPhone);

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
                if (dataSnapshot.child("employee").hasChild(uid)) {
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String companyEditText = company.getText().toString().trim();
                            String phoneEditText = phone.getText().toString().trim();
                            cDatabaseRef.child("employee").child(uid).child("companyName").setValue("(대타)" + companyEditText);
                            cDatabaseRef.child("employee").child(uid).child("companyPhone").setValue(phoneEditText);
                            Intent intent = new Intent(ConnectActivity1.this, ConnectActivity.class);
                            intent.putExtra("ResumeUid", ResumeUid);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String companyEditText = company.getText().toString().trim();
                            String phoneEditText = phone.getText().toString().trim();
                            mUpload = new Upload("(대타)"+ companyEditText, phoneEditText, uid);
                            cDatabaseRef.child("employee").child(uid).setValue(mUpload);
                            Intent intent = new Intent(ConnectActivity1.this, ConnectActivity.class);
                            intent.putExtra("ResumeUid", ResumeUid);
                            startActivity(intent);
                            finish();
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
