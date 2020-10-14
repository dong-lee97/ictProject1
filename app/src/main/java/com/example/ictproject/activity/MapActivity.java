package com.example.ictproject.activity;


import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.ictproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView location, address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        location = findViewById(R.id.companyLocation);
        address1 = findViewById(R.id.companyAddress);

        TextView locationCheck = findViewById(R.id.locationCheck);

        locationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String company_location = intent.getStringExtra("company_location");
        location.setText(company_location);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });
        ////////////////////

        // 버튼 이벤트
        String str = location.getText().toString();
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(
                    str, // 주소
                    1); // 최대 검색 결과 개수
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(addressList.get(0).toString());
        String[] splitStr = addressList.get(0).toString().split(",");
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
        address = address.replace("대한민국", "");
        address1.setText(address);

        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

        // 좌표(위도, 경도) 생성
        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        // 마커 생성
        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title("search result");
        mOptions2.snippet(address);
        mOptions2.position(point);
        // 마커 추가
        googleMap.addMarker(mOptions2);
        // 해당 좌표로 화면 줌
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

    }
}
