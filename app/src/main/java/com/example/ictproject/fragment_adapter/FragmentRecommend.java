package com.example.ictproject.fragment_adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ictproject.R;
import com.example.ictproject.RecommendData;
import com.example.ictproject.upload.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FragmentRecommend extends Fragment {

    private String sex, ageRange, needEx, region;
    private RecyclerView mRecyclerView;
    private RecommendAdapter mAdapter;
    private ArrayList<Upload> mUploads;

    public FragmentRecommend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_fragment_recommend, container, false);

        mRecyclerView = view.findViewById(R.id.recommendRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final TextView whichEx = view.findViewById(R.id.whichExperience);
        final TextView rSex = view.findViewById(R.id.algorithmSex);
        final TextView rAgeRange = view.findViewById(R.id.algorithmAge);
        final TextView rNeedEx = view.findViewById(R.id.algorithmExperience);
        final TextView rRegion = view.findViewById(R.id.algorithmRegion);

        final TextView recommendAnother = view.findViewById(R.id.recommendAnother);

        final RelativeLayout relativeLayout1 = view.findViewById(R.id.mainRecommend);
        final RelativeLayout relativeLayout2 = view.findViewById(R.id.dataOfRecommend);
        final RelativeLayout relativeLayout3 = view.findViewById(R.id.resultRecommend);

        final Button button1 = view.findViewById(R.id.rCafeButton);
        final Button button2 = view.findViewById(R.id.rRestaurantButton);
        final Button button3 = view.findViewById(R.id.rCStoreButton);
        final Button button4 = view.findViewById(R.id.rAcademyButton);
        final Button button5 = view.findViewById(R.id.rMartButton);
        final Button button6 = view.findViewById(R.id.rPcButton);
        final Button button7 = view.findViewById(R.id.rClothesButton);
        final Button button8 = view.findViewById(R.id.rBeautyButton);
        final Button button9 = view.findViewById(R.id.rGymButton);
        final Button button10 = view.findViewById(R.id.rCleanButton);
        final Button button11 = view.findViewById(R.id.rExtraButton);

        Button okayButton = view.findViewById(R.id.recommendYes);
        Button noButton = view.findViewById(R.id.recommendNo);


        final TextView experience = view.findViewById(R.id.recommendExperience);

        final RecommendData convenienceStore = new RecommendData("남", "20대 중반", "필수", "같은 구 소재");
        final RecommendData academy = new RecommendData("여", "20대 초반", "필수", "무관");
        final RecommendData restaurant = new RecommendData("남", "20대 초반", "무관", "같은 구 소재");
        final RecommendData gym = new RecommendData("남", "20대 중반", "필수", "같은 구 소재");
        final RecommendData pc = new RecommendData("무관", "20대 중반", "무관", "같은 구 소재");
        final RecommendData cleaning = new RecommendData("무관", "30대 중반 이상", "무관", "무관");
        final RecommendData beauty = new RecommendData("여", "20대 중반", "필수", "무관");
        final RecommendData clothes = new RecommendData("무관", "20대 중반", "필수", "무관");
        final RecommendData mart = new RecommendData("무관", "20대 중반", "필수", "무관");
        final RecommendData cafe = new RecommendData("여", "20대 중반", "필수", "같은 구 소재");

        final ArrayList<String> kind = new ArrayList<>();
        final ArrayList<RecommendData> dataList = new ArrayList<RecommendData>();
        final ArrayList<Upload> recommendUpload = new ArrayList<Upload>();

        selectExperience(button1, experience, kind, cafe, dataList);
        selectExperience(button2, experience, kind, restaurant, dataList);
        selectExperience(button3, experience, kind, convenienceStore, dataList);
        selectExperience(button4, experience, kind, academy, dataList);
        selectExperience(button5, experience, kind, mart, dataList);
        selectExperience(button6, experience, kind, pc, dataList);
        selectExperience(button7, experience, kind, clothes, dataList);
        selectExperience(button8, experience, kind, beauty, dataList);
        selectExperience(button9, experience, kind, gym, dataList);
        selectExperience(button10, experience, kind, cleaning, dataList);

        TextView textView = view.findViewById(R.id.recommendAlba);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout2.setVisibility(View.VISIBLE);

                for (RecommendData recommendData : dataList) {
                    sex = recommendData.getSex().trim();
                    ageRange = recommendData.getAgeRange().trim();
                    needEx = recommendData.getExperience().trim();
                    region = recommendData.getRegion().trim();
                }

                whichEx.setText(kind.toString().replace("[","").replace("]","").trim());
                rSex.setText(sex);
                rAgeRange.setText(ageRange);
                rNeedEx.setText(needEx);
                rRegion.setText(region);
            }
        });

        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
        mUploads = new ArrayList<>();

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.GONE);
                relativeLayout3.setVisibility(View.VISIBLE);
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.child("employee").getChildren()) {
                            Upload upload = postSnapshot.getValue(Upload.class);
                            if (upload.getExperience() == null) {

                            } else {
                                mUploads.add(upload);
                            }
                        }
                        mAdapter = new RecommendAdapter(getActivity(), mUploads);
                        mAdapter.recommend(kind, sex, ageRange, needEx);
                        for (Upload upload : mAdapter.recommend(kind, sex, ageRange, needEx)) {
                            recommendUpload.add(upload);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout2.setVisibility(View.GONE);
            }
        });

        recommendAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loop1:
                for (int i = 1; i<=1;) {
                    mAdapter.recommend(kind, sex, ageRange, needEx);
                    for (Upload upload : mAdapter.recommend(kind, sex, ageRange, needEx)) {
                        if (recommendUpload.contains(upload)) {
                            i--;
                            break;
                        } else {
                            recommendUpload.add(upload);
                            break Loop1;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return view;
    }

    private void selectExperience(final Button button, final TextView textView, final ArrayList<String> arrayList, final RecommendData recommendData,
                                  final ArrayList<RecommendData> dataList) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                dataList.clear();
                arrayList.add(button.getText().toString().trim());
                dataList.add(recommendData);
                textView.setText(button.getText().toString().trim());
            }
        });
    }

}