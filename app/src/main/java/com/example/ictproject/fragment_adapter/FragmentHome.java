package com.example.ictproject.fragment_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ictproject.R;
import com.example.ictproject.activity.MainActivity;
import com.example.ictproject.activity.Resume_register;
import com.example.ictproject.upload.Upload;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    private ArrayAdapter<CharSequence> regionWholeSpin,regionSpin1, regionSpin2, ageSpin, sexSpin;
    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;
    private ArrayList<Upload> mUploads;
    private ImageView r_resume;
    private String uid;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_fragment_home, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        mRelativeLayout = rootView.findViewById(R.id.fExperienceLayout);
        r_resume = rootView.findViewById(R.id.r_resume);
        ImageView filter = rootView.findViewById(R.id.filter);

        TextView okay = rootView.findViewById(R.id.fExperienceOk);
        final Button cStore = rootView.findViewById(R.id.fcStoreButton);
        final Button cafe = rootView.findViewById(R.id.fCafeButton);
        final Button restaurant = rootView.findViewById(R.id.fRestaurantButton);
        final Button academy = rootView.findViewById(R.id.fAcademyButton);
        final Button mart = rootView.findViewById(R.id.fMartButton);
        final Button bar = rootView.findViewById(R.id.fCleanButton);
        final Button clothes = rootView.findViewById(R.id.fClothesButton);
        final Button beauty = rootView.findViewById(R.id.fBeautyButton);
        final Button gym = rootView.findViewById(R.id.fGymButton);
        final Button pcRoom = rootView.findViewById(R.id.fPcButton);

        mUploads = new ArrayList<>();
        final ArrayList<String> fExperienceList = new ArrayList<>();
        final ArrayList<String> fRegionList = new ArrayList<>();
        final ArrayList<String> sexList = new ArrayList<>();
        final ArrayList<Integer> ageList = new ArrayList<>();

        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final Spinner spin = rootView.findViewById(R.id.wholeSpinner);
        final Spinner spin1 = rootView.findViewById(R.id.spinner1);
        final Spinner spin2 = rootView.findViewById(R.id.spinner2);
        regionWholeSpin = ArrayAdapter.createFromResource(getContext(), R.array.spinner_whole, R.layout.spinner_text);
        regionWholeSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(regionWholeSpin);

        final Spinner spin3 = rootView.findViewById(R.id.ageSpinner1);
        ageSpin = ArrayAdapter.createFromResource(getContext(), R.array.spinner_age, android.R.layout.simple_spinner_dropdown_item);
        ageSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(ageSpin);

        final Spinner spin4 = rootView.findViewById(R.id.sexSpinner1);
        sexSpin = ArrayAdapter.createFromResource(getContext(), R.array.spinner_sex, android.R.layout.simple_spinner_dropdown_item);
        sexSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin4.setAdapter(sexSpin);

        ///////////////////////////////////////////////////////////////////////////

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.child("employee").getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getName() == null) {

                    } else {
                        mUploads.add(upload);
                    }
                }
                mAdapter = new ImagesAdapter(getActivity(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("employee").hasChild(uid) || dataSnapshot.child("company").hasChild(uid)) {
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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelativeLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).setNavigationView(false);
            }
        });

        filterExperience(cStore,fExperienceList);
        filterExperience(cafe,fExperienceList);
        filterExperience(clothes,fExperienceList);
        filterExperience(restaurant,fExperienceList);
        filterExperience(academy,fExperienceList);
        filterExperience(mart,fExperienceList);
        filterExperience(bar,fExperienceList);
        filterExperience(beauty,fExperienceList);
        filterExperience(gym,fExperienceList);
        filterExperience(pcRoom,fExperienceList);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (regionWholeSpin.getItem(position).toString().trim().equals("전체")) {
                    fRegionList.clear();
                    regionSpin1 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_nothing, R.layout.spinner_text);
                    regionSpin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_nothing, R.layout.spinner_text);
                    regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(regionSpin1);
                    spin2.setAdapter(regionSpin2);
                } else if (regionWholeSpin.getItem(position).toString().trim().equals("광역시")) {
                    fRegionList.clear();
                    regionSpin1 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do, R.layout.spinner_text);
                    regionSpin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(regionSpin1);

                    spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (regionSpin1.getItem(position).toString().trim().equals("서울")){
                                fRegionList.clear();
                                fRegionList.add("서울");
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_seoul, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("서울");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (regionSpin1.getItem(position).toString().trim().equals("인천")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_incheon, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("인천");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (regionSpin1.getItem(position).toString().trim().equals("대전")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_daejeon, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("대전");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (regionSpin1.getItem(position).toString().trim().equals("대구")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_daegu, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("대구");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (regionSpin1.getItem(position).toString().trim().equals("울산")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_ulsan, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("울산");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (regionSpin1.getItem(position).toString().trim().equals("부산")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_busan, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("부산");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (regionSpin1.getItem(position).toString().trim().equals("광주")){
                                fRegionList.clear();
                                regionSpin2 = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinner_do_gwangju, R.layout.spinner_text);
                                regionSpin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin2.setAdapter(regionSpin2);

                                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (regionSpin2.getItem(position).toString().trim().equals("전체")) {
                                            fRegionList.clear();
                                            fRegionList.add("광주");
                                        } else {
                                            fRegionList.clear();
                                            fRegionList.add(regionSpin2.getItem(position).toString().trim());
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ageSpin.getItem(position).toString().trim().equals("전체")) {
                    ageList.clear();
                } else {
                    ageList.clear();
                    String ageRange = ageSpin.getItem(position).toString().trim();
                    String firstAge = ageRange.substring(0,2);
                    String secondAge = ageRange.substring(3);
                    int startAge = Integer.parseInt(firstAge);
                    int lastAge = Integer.parseInt(secondAge);
                    ageList.add(startAge);
                    ageList.add(startAge + 1);
                    ageList.add(startAge + 2);
                    ageList.add(startAge + 3);
                    ageList.add(lastAge);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sexSpin.getItem(position).toString().trim().equals("전체")) {
                    sexList.clear();
                } else if (sexSpin.getItem(position).toString().trim().equals("남")){
                    sexList.clear();
                    sexList.add(sexSpin.getItem(position).toString().trim());
                } else if (sexSpin.getItem(position).toString().trim().equals("여")) {
                    sexList.clear();
                    sexList.add(sexSpin.getItem(position).toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelativeLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setNavigationView(true);
                mAdapter.filter(fExperienceList, fRegionList, ageList, sexList);
                mAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    private void filterExperience(final Button button, final ArrayList<String> list) {
        final int[] num = {0};

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0]++;
                if (num[0] % 2 == 0) {
                    button.setTextColor(getResources().getColor(R.color.black));
                    list.remove(button.getText().toString().trim());

                } else {
                    button.setTextColor(getResources().getColor(R.color.mainColor));
                    list.add(button.getText().toString().trim());
                }
            }
        });
    }

}
