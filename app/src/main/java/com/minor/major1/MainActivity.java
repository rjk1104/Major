package com.minor.major1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView increased_confirm, confirm_no;
    TextView  active_no;
    TextView increased_recover, recover_no;
    TextView increased_death, death_no;
    TextView lastUpdate;
    Toolbar toolbar;
    CardView card, card2, card3, card4;

    ImageView refreshBtn;
    View shimmer_layout;
    private static final int PERMISSION_INTERNET = 1;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        toolbar =(Toolbar) findViewById(R.id.dist_details_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // confirm
        increased_confirm = findViewById(R.id.dist_daily_confirm_cases);
        confirm_no = findViewById(R.id.dist_total_confirm_cases);

        // Active
        active_no = findViewById(R.id.dist_total_active_cases);

        // Recover
        increased_recover = findViewById(R.id.dist_daily_recover_cases);
        recover_no = findViewById(R.id.dist_total_recover_cases);

        // Death
        increased_death = findViewById(R.id.dist_daily_death_cases);
        death_no = findViewById(R.id.dist_total_death_cases);

        // Last update
        lastUpdate = findViewById(R.id.update);



        card = findViewById(R.id.card);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StateListActivity.class));

            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StateListActivity.class));

            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StateListActivity.class));

            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StateListActivity.class));

            }
        });

        requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET);
        requestPermission(Manifest.permission.ACCESS_NETWORK_STATE, PERMISSION_ACCESS_NETWORK_STATE);

        refreshBtn = findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shimmer_layout = findViewById(R.id.shimmer_layout);
                shimmer_layout.setVisibility(View.VISIBLE);
                getData();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);

            }
        });


        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.status_by_state) {
            startActivity(new Intent(MainActivity.this, StateListActivity.class));
        }
        if(id == R.id.about_corona) {
            startActivity(new Intent(MainActivity.this, AboutCoronaActivity.class));
        }
        if(id == R.id.symptom) {
            startActivity(new Intent(MainActivity.this, SymptomsActivity.class));
        }
        if(id == R.id.about_app) {
            startActivity(new Intent(MainActivity.this, AboutAppActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }





    private void requestPermission(String permission, int requestId) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_INTERNET: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET);
                }
                return;
            }
            case PERMISSION_ACCESS_NETWORK_STATE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.ACCESS_NETWORK_STATE, PERMISSION_ACCESS_NETWORK_STATE);
                }
                return;
            }
        }
    }


    private void getData() {
        shimmer_layout = findViewById(R.id.shimmer_layout);
        final String url = "https://api.covid19india.org/data.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("statewise");


                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    String active = jsonObject1.getString("active");
                    String confirmed = jsonObject1.getString("confirmed");
                    String death = jsonObject1.getString("deaths");
                    String recovered = jsonObject1.getString("recovered");

                    String increase_confirm = jsonObject1.getString("deltaconfirmed");
                    String increase_death = jsonObject1.getString("deltadeaths");
                    String increase_recover = jsonObject1.getString("deltarecovered");
                    String updated_time = jsonObject1.getString("lastupdatedtime");


                    // confirm data
                    confirm_no.setText(confirmed);
                    increased_confirm.setText("[+"+increase_confirm+"]");

                    // active data
                    active_no.setText(active);

                    // recover data
                    recover_no.setText(recovered);
                    increased_recover.setText("[+"+increase_recover+"]");

                    // death data
                    death_no.setText(death);
                    increased_death.setText("[+"+increase_death+"]");
                    lastUpdate.setText("Last updated time: "+updated_time);

//                    Toast.makeText(MainActivity.this, jsonObject1.toString(), Toast.LENGTH_SHORT).show();




                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            shimmer_layout.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);




                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    shimmer_layout.setVisibility(View.INVISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                shimmer_layout.setVisibility(View.INVISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return super.getParams();
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}


