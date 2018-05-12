package com.ptit.tranhoangminh.newsharefood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ptit.tranhoangminh.newsharefood.views.categoryViews.activities.CategoryActivity;

/**
 * Created by Dell on 3/12/2018.
 */

public class Splashscreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Button btnVaobep, btnDangNhap;
    GoogleApiClient googleApiClient;
    public static final int REQUEST_PERMISSION_LOCATION = 1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        AnhXa();
        //tạo 1 api yeu cau truy cap location service
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //xin quyen .kiem tra xem nguoi dung da cap quyen chua.chua thi hien thi bang deny or allow
        int checkPermissionCoarseLocaltion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED && checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            googleApiClient.connect();
        }


        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Splashscreen.this, LoginActivity.class);
                startActivity(login);

            }
        });



    }

    //kiem tra vs request code tren.neu allow thi connect
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        @SuppressLint("MissingPermission")
        Location vitriHienTai = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        //Log.d("vt", vitriHienTai.getLatitude() + "");
        if (vitriHienTai != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude", String.valueOf(vitriHienTai.getLatitude()));
            editor.putString("longitude", String.valueOf(vitriHienTai.getLongitude()));
            editor.commit();
            Log.d("vitri", vitriHienTai.getLatitude() + " - " + vitriHienTai.getLongitude());
        }
        btnVaobep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDangNhap = new Intent(Splashscreen.this, CategoryActivity.class);
                startActivity(iDangNhap);
                finish();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void AnhXa() {
        btnVaobep = findViewById(R.id.btnVaobep);
        btnDangNhap = findViewById(R.id.btnDangnhap);
        sharedPreferences = getSharedPreferences("toado", MODE_PRIVATE);
    }
}
