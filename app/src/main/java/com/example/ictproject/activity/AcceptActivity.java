package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ictproject.R;
import com.example.ictproject.upload.CompanyUpload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptActivity extends AppCompatActivity {
    private TextView aCompany, aPhone;
    private CompanyUpload cUpload;
    private String companyUid;
    FirebaseUser user;
    final static String companyInformation = "companyInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptactivity);

        TextView watchLocation = findViewById(R.id.watchLocation);
        Button acceptButton = findViewById(R.id.accept_button);
        Button rejectButton = findViewById(R.id.reject_button);
        aCompany = findViewById(R.id.aCompanyName);
        aPhone = findViewById(R.id.aCompanyPhone);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user");

        final Intent intent = getIntent();
        companyUid = intent.getStringExtra(companyInformation);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("company").hasChild(companyUid)){
                    cUpload = dataSnapshot.child("company").child(companyUid).getValue(CompanyUpload.class);
                    aCompany.setText(cUpload.getCompanyName());
                    aPhone.setText(cUpload.getCompanyPhone());
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
                intent1.putExtra("destinationUid", companyUid);//알림을 준 회사회원의 uid
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AcceptActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
        });

        watchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AcceptActivity.this, MapActivity.class);
                intent.putExtra("company_location", aCompany.getText().toString().trim());
                startActivity(intent);
            }
        });




    }
}
