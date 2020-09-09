package com.example.ictproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.ictproject.R;

public class SelectIdentity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_identity);

        Button button_of_employee = findViewById(R.id.button_of_employee);
        Button button_of_employer = findViewById(R.id.button_of_employer);

        button_of_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectIdentity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        button_of_employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectIdentity.this, CompanyRegister.class);
                startActivity(intent);
            }
        });


    }
}
