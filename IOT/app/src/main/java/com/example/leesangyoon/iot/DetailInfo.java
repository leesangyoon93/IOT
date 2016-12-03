package com.example.leesangyoon.iot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daddyslab on 2016. 12. 3..
 */
public class DetailInfo extends AppCompatActivity {

    ImageView car1, car2, car3, car4, arrow1, arrow2, arrow3, arrow4, full;
    Timer mTimer = null;
    TimerTask task = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_detailinfo);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("아주대 삼거리 공영주차장");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        car1 = (ImageView)findViewById(R.id.car1);
        car2 = (ImageView)findViewById(R.id.car2);
        car3 = (ImageView)findViewById(R.id.car3);
        car4 = (ImageView)findViewById(R.id.car4);
        arrow1 = (ImageView)findViewById(R.id.arrow1);
        arrow2 = (ImageView)findViewById(R.id.arrow2);
        arrow3 = (ImageView)findViewById(R.id.arrow3);
        arrow4 = (ImageView)findViewById(R.id.arrow4);
        full = (ImageView)findViewById(R.id.full);

        if(task != null) {
            task.cancel();
            task = null;
        }

        task = new TimerTask() {
            public void run() {
                try {
                    getDataToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(task, 1000, 3000);

    }

    private void getDataToServer() throws Exception {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);

        final String URL = "http://52.41.19.232/getData";
        Map<String, String> postParam = new HashMap<String, String>();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(DetailInfo.this, "알 수 없는 에러가 발생합니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(response.getBoolean("pos1")) {
                            car1.setVisibility(View.VISIBLE);
                        }
                        if(response.getBoolean("pos2")) {
                            car2.setVisibility(View.VISIBLE);
                        }
                        if(response.getBoolean("pos3")) {
                            car3.setVisibility(View.VISIBLE);
                        }
                        if(response.getBoolean("pos4")) {
                            car4.setVisibility(View.VISIBLE);
                        }

                        if(!response.getBoolean("pos1")) {
                            full.setVisibility(View.GONE);
                            arrow1.setVisibility(View.VISIBLE);
                            arrow2.setVisibility(View.GONE);
                            arrow3.setVisibility(View.GONE);
                            arrow4.setVisibility(View.GONE);
                        }
                        else if(!response.getBoolean("pos2")) {
                            full.setVisibility(View.GONE);
                            arrow1.setVisibility(View.GONE);
                            arrow2.setVisibility(View.VISIBLE);
                            arrow3.setVisibility(View.GONE);
                            arrow4.setVisibility(View.GONE);
                        }
                        else if(!response.getBoolean("pos3")) {
                            full.setVisibility(View.GONE);
                            arrow1.setVisibility(View.GONE);
                            arrow2.setVisibility(View.GONE);
                            arrow3.setVisibility(View.VISIBLE);
                            arrow4.setVisibility(View.GONE);
                        }
                        else if(!response.getBoolean("pos4")) {
                            full.setVisibility(View.GONE);
                            arrow1.setVisibility(View.GONE);
                            arrow2.setVisibility(View.GONE);
                            arrow3.setVisibility(View.GONE);
                            arrow4.setVisibility(View.VISIBLE);
                        }
                        else {
                            full.setVisibility(View.VISIBLE);
                            arrow1.setVisibility(View.GONE);
                            arrow2.setVisibility(View.GONE);
                            arrow3.setVisibility(View.GONE);
                            arrow4.setVisibility(View.GONE);
                        }
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
