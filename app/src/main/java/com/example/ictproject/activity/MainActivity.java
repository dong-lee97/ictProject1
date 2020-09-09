package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.ictproject.fragment_adapter.FragmentChat;
import com.example.ictproject.fragment_adapter.FragmentHome;
import com.example.ictproject.fragment_adapter.FragmentPage;
import com.example.ictproject.fragment_adapter.FragmentRecommend;
import com.example.ictproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private BottomNavigationView bottomNavigationView;
    private String uid;

    Fragment FragmentHome;
    Fragment FragmentRecommend;
    Fragment FragmentChat;
    Fragment FragmentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentHome = new FragmentHome();
        FragmentRecommend = new FragmentRecommend();
        FragmentChat = new FragmentChat();
        FragmentPage = new FragmentPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, FragmentHome).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Map<String, String> map = new HashMap<>();
                map.put("pushToken", token);
                firebaseFirestore.getInstance().collection("token").document(uid).set(map);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, FragmentHome).commit();
                    return true;
                case R.id.recommendItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, FragmentRecommend).commit();
                    return true;
                case R.id.chatItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, FragmentChat).commit();
                    return true;
                case R.id.myPageItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, FragmentPage).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void setNavigationView(Boolean visible) {
        if (bottomNavigationView.isShown() && !visible) {
            bottomNavigationView.setVisibility(View.GONE);
        } else if (!bottomNavigationView.isShown() && visible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}


