package com.example.leesangyoon.iot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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

    TextView lat, lon, remain, distance;
    LinearLayout wrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longtitude);
        distance = (TextView) findViewById(R.id.distance);
        wrap = (LinearLayout)findViewById(R.id.parkingAreaInfo);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("THRUPATH");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        GpsInfo gps = new GpsInfo(MainActivity.this);
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
    }

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
