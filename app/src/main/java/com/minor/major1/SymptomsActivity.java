package com.minor.major1;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SymptomsActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        toolbar=findViewById(R.id.symptoms_corona_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Symptoms");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(SymptomsActivity.this,MainActivity.class));
            }
        });

    }
}