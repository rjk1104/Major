package com.minor.major1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutAppActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        toolbar=findViewById(R.id.dist_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About App");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutAppActivity.this,MainActivity.class));
            }
        });
        version=(TextView) findViewById(R.id.version);
        try{
            version.setText(getPackageManager().getPackageInfo(getPackageName(),0).versionName);

        }catch(PackageManager.NameNotFoundException e){
            Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }


    }
}