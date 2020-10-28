package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.ictproject.R;
import com.example.ictproject.fragment_adapter.RegionAdapter;
import com.example.ictproject.upload.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectIdentity extends AppCompatActivity {

    private DatabaseReference DatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_identity);

        Button button_of_employee = findViewById(R.id.button_of_employee);
        Button button_of_employer = findViewById(R.id.button_of_employer);


        DatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        final Intent intent = getIntent();
        final String fackbook = intent.getStringExtra("fackbookLogin");
        final String google = intent.getStringExtra("googleLogin");
        button_of_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fackbook != null || google != null) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = user.getUid();
                    Intent intent = new Intent(SelectIdentity.this, MainActivity.class);
                    DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Upload upload = new Upload("google", "", uid);
                            DatabaseRef.child("employee").child(uid).setValue(upload);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SelectIdentity.this, RegisterActivity.class);
                    startActivity(intent);
                }

            }
        });

        button_of_employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fackbook != null || google != null) {
                    Intent intent = new Intent(SelectIdentity.this, CompanyInfo.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SelectIdentity.this, CompanyRegister.class);
                    startActivity(intent);
                }
            }
        });


    }
}
