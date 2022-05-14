package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        RelativeLayout relativeLayout = findViewById(R.id.rel_his);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();



        BottomNavigationView bottomNavigationNiew = findViewById(R.id.buttom_navigation);
        bottomNavigationNiew.setItemIconTintList(null);
        bottomNavigationNiew.setSelectedItemId(R.id.history_nav);
        bottomNavigationNiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.event_nav:
                        startActivity(new Intent(getApplicationContext(),EventActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.gallery_nav:
                        startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history_nav:

                        return true;
                }
                return false;
            }
        });
        findViewById(R.id.his_2020).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HistoryActivity.this,History_gall_Activity.class);
                in.putExtra("year","2022");
                startActivity(in);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {

    }

}