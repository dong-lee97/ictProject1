package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ictproject.R;
import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptActivity extends AppCompatActivity {
    private EditText aCompany, aPhone;
    private CompanyUpload cUpload;
    private Upload upload;
    private String companyUid;
    private String uploadUid;
    final static String companyInformation = "companyInformation";
    final static String uploadInformation = "uploadInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptactivity);

        Button acceptButton = findViewById(R.id.accept_button);
        Button rejectButton = findViewById(R.id.reject_button);
        aCompany = findViewById(R.id.aCompanyName);
        aPhone = findViewById(R.id.aCompanyPhone);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        final Intent intent = getIntent();
        companyUid = intent.getStringExtra(companyInformation);
        uploadUid = intent.getStringExtra(uploadInformation);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (companyUid == null) {
                    if (dataSnapshot.child("employee").hasChild(uploadUid)) {
                        upload = dataSnapshot.child("employee").child(uploadUid).getValue(Upload.class);
                        aCompany.setText(upload.getCompanyName());
                        aPhone.setText(upload.getCompanyPhone());
                    }
                } else if (uploadUid == null) {
                    if (dataSnapshot.child("company").hasChild(companyUid)){
                        cUpload = dataSnapshot.child("company").child(companyUid).getValue(CompanyUpload.class);
                        aCompany.setText(cUpload.getCName());
                        aPhone.setText(cUpload.getCPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AcceptActivity.this, MessageActivity.class);
                if (companyUid == null){
                    intent1.putExtra("destinationUid", uploadUid);
                    intent1.putExtra("sendUid", uploadUid);
                    //알림을 준 개인회원의 uid
                } else if (uploadUid == null) {
                    intent1.putExtra("destinationUid", companyUid);//알림을 준 회사회원의 uid
                    intent1.putExtra("sendUid", companyUid);
                }
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AcceptActivity.this, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
            }
        });




    }
}
