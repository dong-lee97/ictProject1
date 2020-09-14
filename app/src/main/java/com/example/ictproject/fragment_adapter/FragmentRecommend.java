package com.example.ictproject.fragment_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ictproject.R;
import com.example.ictproject.activity.RecommendDataActivity;

public class FragmentRecommend extends Fragment {

    public FragmentRecommend() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_recommend, container, false);

        final Button button = view.findViewById(R.id.rCStoreButton);
        TextView textView = view.findViewById(R.id.recommendAlba);

        final int[] num = {0};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0]++;
                if (num[0] % 2 == 0) {
                    button.setTextColor(getResources().getColor(R.color.black));
                } else {
                    button.setTextColor(getResources().getColor(R.color.mainColor));
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendDataActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}