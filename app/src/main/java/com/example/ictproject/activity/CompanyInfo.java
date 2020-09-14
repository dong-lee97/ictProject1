package com.example.ictproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyInfo extends AppCompatActivity {

    private EditText cEditTextName, cEditTextPhone;
    private DatabaseReference cDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        cEditTextName = findViewById(R.id.companyEditText);
        cEditTextPhone = findViewById(R.id.cPhoneEditText);

        Button cUploadButton = findViewById(R.id.cUploadButton);

        cDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        cUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUploadFile();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void cUploadFile() {
        if(cEditTextName.length()>0 && cEditTextPhone.length()>8) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            CompanyUpload companyUpload = new CompanyUpload(cEditTextName.getText().toString().trim(),
                    cEditTextPhone.getText().toString().trim(), uid);
            cDatabaseRef.child("company").child(uid).setValue(companyUpload);
            Toast.makeText(CompanyInfo.this, "등록 성공", Toast.LENGTH_LONG).show();
            myStartActivity(LoginActivity.class);
        } else{
            Toast.makeText(CompanyInfo.this, "등록 실패", Toast.LENGTH_LONG).show();
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
