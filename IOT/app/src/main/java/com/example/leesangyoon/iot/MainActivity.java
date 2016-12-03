package com.example.leesangyoon.iot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String longtitude = "";
    String latitude = "";
    Boolean isGPSEnabled;
    TextView lat, lon, remain, distance;
    LinearLayout wrap;
    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longtitude);
        remain = (TextView) findViewById(R.id.remain);
        distance = (TextView) findViewById(R.id.distance);
        wrap = (LinearLayout)findViewById(R.id.parkingAreaInfo);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            lat.setText(String.valueOf(latitude));
            lon.setText(String.valueOf(longitude));

            try {
                getDistanceToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Toast.makeText(
//                    getApplicationContext(),
//                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
//                    Toast.LENGTH_LONG).show();
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
        wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailInfo.class);
                startActivity(intent);
            }
        });

        //progressLoading();
    }

//        if (!isGPSEnabled) {
//            new AlertDialog.Builder(MainActivity.this)
//                    .setTitle("GPS 켜기")
//                    .setMessage("GPS를 켜시겠습니까?")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(myIntent);
//                            dialogInterface.cancel();
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    })
//                    .show();
//        }



//    private void progressLoading() {
//        if (isGPSEnabled) {
//            load = ProgressDialog.show(this, "Loading...", "주변 주차장 목록을 불러오는 중입니다...", false, false);
//            locationListener = new MyLocationListener();
//
//            //선택된 프로바이더를 사용해 위치정보를 업데이트
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
//        }
//    }
//
//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        //LocationListener을 이용해서 위치정보가 업데이트 되었을때 동작 구현
//        public void onLocationChanged(Location loc) {
//            load.dismiss();
//
//            longtitude = String.valueOf(loc.getLongitude());
//            latitude = String.valueOf(loc.getLatitude());
//
//            lat.setText(latitude);
//            lon.setText(longtitude);
//
//            try {
//                getDistanceToServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            // TODO Auto-generated method stub
//
//        }
//    }

    private void getDistanceToServer() throws Exception {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("latitude", lat.getText().toString());
        postParam.put("longtitude", lon.getText().toString());

        String URL = "http://52.41.19.232/getDistance";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if (response.getString("result").equals("fail")) {
                        Toast.makeText(MainActivity.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, String.valueOf(response.getInt("result")), Toast.LENGTH_SHORT).show();
                        distance.setText(String.valueOf(response.getInt("result")) + " M");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }
}
