package com.example.ictproject.fragment_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ictproject.R;
import com.example.ictproject.activity.Resume_register;
import com.example.ictproject.upload.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private ImageView r_resume;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_fragment_home, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
        r_resume = rootView.findViewById(R.id.r_resume);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.child("employee").getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getName() == null){

                    } else {
                        mUploads.add(upload);
                    }
                }
                mAdapter = new ImagesAdapter(getActivity(), mUploads);
                mRecyclerView.setAdapter(mAdapter);

                if (dataSnapshot.child("employee").hasChild(uid) || dataSnapshot.child("company").hasChild(uid)){
                    r_resume.setEnabled(false);
                } else {
                    r_resume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Resume_register.class);
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}
