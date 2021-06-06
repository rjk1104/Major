package com.minor.major1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minor.major1.Adapter.DistrictListAdapter;
import com.minor.major1.models.DistModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateWiseDetailsActivity extends AppCompatActivity {
    private TextView daily_confirm_casesTv;
    private TextView total_confirm_casesTv;

    private TextView total_active_casesTv;

    private TextView daily_recover_casesTv;
    private TextView total_recover_casesTv;

    private TextView daily_death_casesTv;
    private TextView total_death_casesTv;

    private Toolbar toolbar;

    private ProgressDialog progressDialog;
    private Intent intent;
    private String state_name;
    DistrictListAdapter districtListAdapter;
    List<DistModel> arrayList;
    private RecyclerView recyclerView;

    LinearLayout state_data_layout;
    MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise_details);

        intent = getIntent();
        state_name = intent.getStringExtra("state_name");

        daily_confirm_casesTv = (TextView) findViewById(R.id.dist_daily_confirm_cases);
        total_confirm_casesTv = (TextView) findViewById(R.id.dist_total_confirm_cases);

        total_active_casesTv = (TextView) findViewById(R.id.dist_total_active_cases);

        daily_recover_casesTv = (TextView) findViewById(R.id.dist_daily_recover_cases);
        total_recover_casesTv = (TextView) findViewById(R.id.dist_total_recover_cases);

        daily_death_casesTv = (TextView) findViewById(R.id.dist_daily_death_cases);
        total_death_casesTv = (TextView) findViewById(R.id.dist_total_death_cases);

        toolbar = (Toolbar) findViewById(R.id.toolbar_state_details);

        state_data_layout = findViewById(R.id.dist_data_layout);


        searchView = findViewById(R.id.search_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(state_name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StateWiseDetailsActivity.this, StateListActivity.class));
            }
        });


        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        getStateData();

        recyclerView = (RecyclerView) findViewById(R.id.dist_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getDistListData();
    }

    private void getDistListData() {
        String url = "https://api.covid19india.org/state_district_wise.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(state_name);

                            JSONObject distObject = jsonObject.getJSONObject("districtData");

                            JSONArray key = distObject.names();
                            for (int i = 0; i < key.length(); i++) {

                                String distName = key.getString(i);

                                JSONObject jsonObjectDist = distObject.getJSONObject(distName);

                                String confirm_cases = jsonObjectDist.getString("confirmed");



                                HashMap<String, String> map = new HashMap<>();

                                map.put("state_name", state_name);
                                map.put("confirm_cases", confirm_cases);
                                map.put("dist_name", distName);

                                DistModel distModel = new DistModel(distName, state_name, confirm_cases);

                                arrayList.add(distModel);

                            }


                            districtListAdapter = new DistrictListAdapter(getApplicationContext(), arrayList);
                            districtListAdapter.setHasStableIds(true);
                            recyclerView.setAdapter(districtListAdapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StateWiseDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StateWiseDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                }
        );

        int socketTime = 7000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(StateWiseDetailsActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

    private void getStateData() {

        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");

                            for (int i = 0; i < jsonArray.length(); i ++ ) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String state = jsonObject.getString("state");

                                if(state.equals(state_name)) {
                                    // Confirm Cases
                                    String total_confirm_cases = jsonObject.getString("confirmed");
                                    String daily_confirm_cases = jsonObject.getString("deltaconfirmed");
                                    total_confirm_casesTv.setText(total_confirm_cases);
                                    daily_confirm_casesTv.setText("[+" +daily_confirm_cases+ "]");

                                    // Active Cases
                                    String total_active_cases = jsonObject.getString("active");
                                    total_active_casesTv.setText(total_active_cases);

                                    // Recover Cases
                                    String total_recover_cases = jsonObject.getString("recovered");
                                    String daily_recover_cases = jsonObject.getString("deltarecovered");
                                    total_recover_casesTv.setText(total_recover_cases);
                                    daily_recover_casesTv.setText("[+"+daily_recover_cases+ "]");


                                    // Death Cases
                                    String total_death_cases = jsonObject.getString("deaths");
                                    String daily_death_cases = jsonObject.getString("deltadeaths");
                                    total_death_casesTv.setText(total_death_cases);
                                    daily_death_casesTv.setText("[+"+daily_death_cases+"]");

                                }

                            }



                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StateWiseDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                }
        );

        int socketTime = 70000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(retryPolicy);

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(StateWiseDetailsActivity.this);
        requestQueue.add(jsonObjectRequest);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.state_toolbar, menu);

        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        searchView.setHint("Search By District...");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                districtListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                districtListAdapter.getFilter().filter(newText);
                return false;
            }


        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                state_data_layout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                state_data_layout.setVisibility(View.VISIBLE);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }
}
