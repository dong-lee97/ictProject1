package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.ictproject.R;
import com.example.ictproject.RegionData;
import com.example.ictproject.fragment_adapter.RegionAdapter;
import com.example.ictproject.upload.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Resume_register extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ArrayList<RegionData> dataList, seoulDataList, incheonDataList, daejeonDataList, daegooDataList, ulsanDataList, busanDataList, gwangjooDataList;

    private EditText mEditTextFileName, mEditTextAge, mExperienceDetail, mDay;
    private ImageView mImageView;
    private TextView mRegion;
    private TextView region;
    private TextView mExperience;
    private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout;
    private LinearLayout experience;

    private String sex;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_register);

        Button mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mEditTextAge = findViewById(R.id.edit_text_age);
        mExperience = findViewById(R.id.experience);
        mExperienceDetail = findViewById(R.id.experienceDetail);
        mRegion = findViewById(R.id.possible_region);
        mDay = findViewById(R.id.possible_day);
        mImageView = findViewById(R.id.image_view);

        ImageView back = findViewById(R.id.back1);

        Button plus = findViewById(R.id.registerExperience);
        Button plus1 = findViewById(R.id.registerRegion);

        TextView okay1 = findViewById(R.id.experienceOk);
        TextView okay2 = findViewById(R.id.regionOk);

        Button button1 = findViewById(R.id.cafeButton);
        Button button2 = findViewById(R.id.restaurantButton);
        Button button3 = findViewById(R.id.cStoreButton);
        Button button4 = findViewById(R.id.academyButton);
        Button button5 = findViewById(R.id.martButton);
        Button button6 = findViewById(R.id.pcButton);
        Button button7 = findViewById(R.id.clothesButton);
        Button button8 = findViewById(R.id.beautyButton);
        Button button9 = findViewById(R.id.gymButton);
        Button button10 = findViewById(R.id.cleanButton);
        Button button11 = findViewById(R.id.extraButton);

        relativeLayout = findViewById(R.id.whole);
        relativeLayout1 = findViewById(R.id.experienceLayout);
        relativeLayout2 = findViewById(R.id.regionLayout);

        mStorageRef = FirebaseStorage.getInstance().getReference("user");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        final ArrayList<String> experienceList = new ArrayList<>();

        final ArrayList<String> regionList = new ArrayList<>();
        final ArrayList<String> detailList = new ArrayList<>();
        final ArrayList<Boolean> bList = new ArrayList<>();
        final TextView metropolitan = findViewById(R.id.metropolitan_city);
        final LinearLayout cityLayout = findViewById(R.id.city);
        TextView seoul = findViewById(R.id.seoul);
        TextView incheon = findViewById(R.id.incheon);
        TextView daejeon = findViewById(R.id.daejeon);
        TextView daegoo = findViewById(R.id.daegoo);
        TextView ulsan = findViewById(R.id.ulsan);
        TextView busan = findViewById(R.id.busan);
        TextView gwangjoo = findViewById(R.id.gwangjoo);
        region = findViewById(R.id.nothing);

        final RadioGroup radioGroup = findViewById(R.id.chooseSex);

        this.InitializeRegionData();
        this.InitializeSeoulRegionData();
        this.InitializeBusanRegionData();
        this.InitializeDaegooRegionData();
        this.InitializeDaejeonRegionData();
        this.InitializeGwangjooRegionData();
        this.InitializeIncheonRegionData();
        this.InitializeUlsanRegionData();
        final ListView listView = findViewById(R.id.listViewContainer);

        final Boolean[] Bg_change = {true};
        metropolitan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bg_change[0]) {
                    cityLayout.setVisibility(View.VISIBLE);
                    Bg_change[0] = false;
                } else {
                    cityLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    regionList.clear();
                    detailList.clear();
                    region.setText(null);
                    Bg_change[0] = true;
                }
            }
        });

        createRegionView(region, mRegion, seoul, listView, regionList, detailList, bList, seoulDataList, dataList);
        createRegionView(region, mRegion, incheon, listView, regionList, detailList, bList, incheonDataList, dataList);
        createRegionView(region, mRegion, daejeon, listView, regionList, detailList, bList, daejeonDataList, dataList);
        createRegionView(region, mRegion, daegoo, listView, regionList, detailList, bList, daegooDataList, dataList);
        createRegionView(region, mRegion, ulsan, listView, regionList, detailList, bList, ulsanDataList, dataList);
        createRegionView(region, mRegion, gwangjoo, listView, regionList, detailList, bList, gwangjooDataList, dataList);
        createRegionView(region, mRegion, busan, listView, regionList, detailList, bList, busanDataList, dataList);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null) {
                    Toast.makeText(Resume_register.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(experienceList);
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                relativeLayout1.setVisibility(View.VISIBLE);

            }
        });

        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
            }
        });

        okay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
            }
        });

        okay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.GONE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resume_register.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.man:
                        sex = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim();
                    case R.id.woman:
                        sex = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim();
                }
            }
        });

        experience = findViewById(R.id.layout3);

        TextView view1 = new TextView(Resume_register.this);
        TextView view2 = new TextView(Resume_register.this);
        TextView view3 = new TextView(Resume_register.this);
        TextView view4 = new TextView(Resume_register.this);
        TextView view5 = new TextView(Resume_register.this);
        TextView view6 = new TextView(Resume_register.this);
        TextView view7 = new TextView(Resume_register.this);
        TextView view8 = new TextView(Resume_register.this);
        TextView view9 = new TextView(Resume_register.this);
        TextView view10 = new TextView(Resume_register.this);
        TextView view11 = new TextView(Resume_register.this);

        createExperienceView(button1, view1, experienceList);
        createExperienceView(button2, view2, experienceList);
        createExperienceView(button3, view3, experienceList);
        createExperienceView(button4, view4, experienceList);
        createExperienceView(button5, view5, experienceList);
        createExperienceView(button6, view6, experienceList);
        createExperienceView(button7, view7, experienceList);
        createExperienceView(button8, view8, experienceList);
        createExperienceView(button9, view9, experienceList);
        createExperienceView(button10, view10, experienceList);
        createExperienceView(button10, view11, experienceList);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).override(110, 110).centerCrop().into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(final ArrayList<String> experienceList) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(uid).child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), downloadUrl.toString(), mEditTextAge.getText().toString().trim(), sex,
                                    experienceList.toString().replace("[", "").replace("]", "").trim(), mExperienceDetail.getText().toString().trim(), mRegion.getText().toString().trim(), mDay.getText().toString().trim(), user.getUid(), "", "");
                            mDatabaseRef.child("employee").child(user.getUid()).setValue(upload);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Resume_register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void createExperienceView(final Button button, TextView view, final ArrayList<String> list) {
        view.setText(button.getText().toString());
        view.setTextColor(getResources().getColor(R.color.black));
        view.setTextSize(15);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 0, 0);
        view.setLayoutParams(lp);
        final int[] num = {0};

        final TextView finalView = view;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0]++;
                if (num[0] % 2 == 0) {
                    button.setTextColor(getResources().getColor(R.color.black));
                    experience.removeView(finalView);
                    list.remove(button.getText().toString());
                    mExperience.setText(list.toString().replace("[", "").replace("]", "").trim());

                } else {
                    button.setTextColor(getResources().getColor(R.color.mainColor));
                    experience.addView(finalView);
                    list.add(button.getText().toString());
                    mExperience.setText(list.toString().replace("[", "").replace("]", "").trim());
                }
            }
        });

    }
    private void createRegionView(final TextView region, final TextView mRegion, final TextView name, final ListView listView, final ArrayList<String> regionList, final ArrayList<String> detailList,
                                  final ArrayList<Boolean> bList, final ArrayList<RegionData> regionDataList, final ArrayList<RegionData> dataList) {
        final Boolean[] Bg_change = {true};
        final RegionAdapter[] regionAdapter = {new RegionAdapter(this, dataList)};
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regionAdapter[0] = new RegionAdapter(getApplicationContext(), regionDataList);
                regionAdapter[0].notifyDataSetChanged();
                listView.setAdapter(regionAdapter[0]);
                if (Bg_change[0]) {
                    region.setText(name.getText().toString());
                    region.setTextColor(getResources().getColor(R.color.mainColor));
                    region.setTextSize(15);
                    listView.setVisibility(View.VISIBLE);
                    regionList.add(name.getText().toString());
                    Bg_change[0] = false;
                } else {
                    region.setText(null);
                    listView.setVisibility(View.INVISIBLE);
                    regionList.clear();
                    detailList.clear();
                    bList.clear();
                    Bg_change[0] = true;
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0, n = regionDataList.size(); i < n; i++) {
                            bList.add(true);
                        }
                        String regionDetail = regionDataList.get(position).getRegion();
                        if (bList.get(position)) {
                            detailList.add(regionDetail);
                            String possibleRegion = regionList.toString().replace("[", "").replace("]", " ") + detailList.toString().replace("[", "").replace("]", "").trim();
                            region.setText(possibleRegion);
                            mRegion.setText(possibleRegion);
                            bList.set(position, false);
                        } else {
                            detailList.remove(regionDetail);
                            String possibleRegion = regionList.toString().replace("[", "").replace("]", " ") + detailList.toString().replace("[", "").replace("]", "").trim();
                            region.setText(possibleRegion);
                            mRegion.setText(possibleRegion);
                            bList.set(position, true);
                        }
                    }
                });
            }
        });


    }

    public void InitializeRegionData(){
        dataList = new ArrayList<RegionData>();
        dataList.add(new RegionData(""));
    }
    public void InitializeSeoulRegionData() {
        seoulDataList = new ArrayList<RegionData>();
        seoulDataList.add(new RegionData("전체"));
        seoulDataList.add(new RegionData("강남구"));
        seoulDataList.add(new RegionData("강동구"));
        seoulDataList.add(new RegionData("강북구"));
        seoulDataList.add(new RegionData("강서구"));
        seoulDataList.add(new RegionData("관악구"));
        seoulDataList.add(new RegionData("광진구"));
        seoulDataList.add(new RegionData("구로구"));
        seoulDataList.add(new RegionData("금천구"));
        seoulDataList.add(new RegionData("노원구"));
        seoulDataList.add(new RegionData("도봉구"));
        seoulDataList.add(new RegionData("동대문구"));
        seoulDataList.add(new RegionData("동작구"));
        seoulDataList.add(new RegionData("마포구"));
        seoulDataList.add(new RegionData("서대문구"));
        seoulDataList.add(new RegionData("서초구"));
        seoulDataList.add(new RegionData("성동구"));
        seoulDataList.add(new RegionData("성북구"));
        seoulDataList.add(new RegionData("송파구"));
        seoulDataList.add(new RegionData("양천구"));
        seoulDataList.add(new RegionData("영등포구"));
        seoulDataList.add(new RegionData("용산구"));
        seoulDataList.add(new RegionData("은평구"));
        seoulDataList.add(new RegionData("종로구"));
        seoulDataList.add(new RegionData("중구"));
        seoulDataList.add(new RegionData("중랑구"));
    }
    public void InitializeIncheonRegionData() {
        incheonDataList = new ArrayList<RegionData>();
        incheonDataList.add(new RegionData("전체"));
        incheonDataList.add(new RegionData("강화군"));
        incheonDataList.add(new RegionData("계양구"));
        incheonDataList.add(new RegionData("남구"));
        incheonDataList.add(new RegionData("남동구"));
        incheonDataList.add(new RegionData("동구"));
        incheonDataList.add(new RegionData("부평구"));
        incheonDataList.add(new RegionData("서구"));
        incheonDataList.add(new RegionData("연수구"));
        incheonDataList.add(new RegionData("옹진군"));
        incheonDataList.add(new RegionData("중구"));
    }
    public void InitializeDaejeonRegionData() {
        daejeonDataList = new ArrayList<RegionData>();
        daejeonDataList.add(new RegionData("전체"));
        daejeonDataList.add(new RegionData("대덕구"));
        daejeonDataList.add(new RegionData("동구"));
        daejeonDataList.add(new RegionData("서구"));
        daejeonDataList.add(new RegionData("유성구"));
        daejeonDataList.add(new RegionData("중구"));
    }
    public void InitializeDaegooRegionData() {
        daegooDataList = new ArrayList<RegionData>();
        daegooDataList.add(new RegionData("전체"));
        daegooDataList.add(new RegionData("남구"));
        daegooDataList.add(new RegionData("달서구"));
        daegooDataList.add(new RegionData("달성군"));
        daegooDataList.add(new RegionData("동구"));
        daegooDataList.add(new RegionData("북구"));
        daegooDataList.add(new RegionData("서구"));
        daegooDataList.add(new RegionData("수성구"));
    }
    public void InitializeUlsanRegionData() {
        ulsanDataList = new ArrayList<RegionData>();
        ulsanDataList.add(new RegionData("전체"));
        ulsanDataList.add(new RegionData("남구"));
        ulsanDataList.add(new RegionData("동구"));
        ulsanDataList.add(new RegionData("북구"));
        ulsanDataList.add(new RegionData("울주군"));
        ulsanDataList.add(new RegionData("중구"));

    }
    public void InitializeBusanRegionData() {
        busanDataList = new ArrayList<RegionData>();
        busanDataList.add(new RegionData("전체"));
        busanDataList.add(new RegionData("강서구"));
        busanDataList.add(new RegionData("금정구"));
        busanDataList.add(new RegionData("기장군"));
        busanDataList.add(new RegionData("남구"));
        busanDataList.add(new RegionData("동구"));
        busanDataList.add(new RegionData("동래구"));
        busanDataList.add(new RegionData("부산진구"));
        busanDataList.add(new RegionData("북구"));
        busanDataList.add(new RegionData("사상구"));
        busanDataList.add(new RegionData("사하구"));
        busanDataList.add(new RegionData("서구"));
        busanDataList.add(new RegionData("수영구"));
        busanDataList.add(new RegionData("연제구"));
        busanDataList.add(new RegionData("영도구"));
        busanDataList.add(new RegionData("중구"));
        busanDataList.add(new RegionData("해운대구"));
    }
    public void InitializeGwangjooRegionData() {
        gwangjooDataList = new ArrayList<RegionData>();
        gwangjooDataList.add(new RegionData("전체"));
        gwangjooDataList.add(new RegionData("광산구"));
        gwangjooDataList.add(new RegionData("남구"));
        gwangjooDataList.add(new RegionData("동구"));
        gwangjooDataList.add(new RegionData("북구"));
        gwangjooDataList.add(new RegionData("서구"));
    }
}