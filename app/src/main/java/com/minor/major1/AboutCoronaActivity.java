package com.minor.major1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutCoronaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_corona);
        toolbar=findViewById(R.id.about_corona_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Corona");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutCoronaActivity.this,MainActivity.class));
            }
        });

    }
}