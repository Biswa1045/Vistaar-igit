package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {
    @BindView(R.id.p_evnt)
    ProgressBar progressBar;

    @BindView(R.id.event_rcv)
    RecyclerView rcv;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        RelativeLayout relativeLayout = findViewById(R.id.rec_eve);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        BottomNavigationView bottomNavigationNiew = findViewById(R.id.buttom_navigation);
        bottomNavigationNiew.setItemIconTintList(null);
        bottomNavigationNiew.setSelectedItemId(R.id.event_nav);
        bottomNavigationNiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.event_nav:

                        return true;
                    case R.id.gallery_nav:
                        startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history_nav:
                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });




        ButterKnife.bind(this);
        init();
        getLikeList();

    }
    private void init() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        rcv.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(rcv);
        db = FirebaseFirestore.getInstance();
    }

    private void getLikeList() {
        Query query = db.collection(year).document("Event").collection("Events");

        FirestoreRecyclerOptions<event> response = new FirestoreRecyclerOptions.Builder<event>()
                .setQuery(query, event.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<event, EventActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(EventActivity.FriendsHolder holder, int position, event model) {
                progressBar.setVisibility(View.GONE);

                String event_name = model.getEvent_name();
                String event_time = model.getEvent_time();
                String event_location = model.getEvent_location();
                holder.event_time.setText(event_time);
                holder.event_location.setText(event_location);
                holder.event_name_event.setText(event_name);
                Glide.with(getApplicationContext())
                        .load(model.getEvent_photo())
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.event_poster);
                holder.event_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentq = new Intent(EventActivity.this,EventDetailsActivity.class);
                        intentq.putExtra("event_name",event_name);
                        startActivity(intentq);
                    }
                });

            }

            @Override
            public EventActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.event, group, false);

                return new EventActivity.FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        rcv.setAdapter(adapter);


    }


    public class FriendsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_poster)
        ImageView event_poster;
        @BindView(R.id.event_register)
        TextView event_register;
        @BindView(R.id.event_time)
        TextView event_time;
        @BindView(R.id.event_location)
        TextView event_location;
        @BindView(R.id.event_name_event)
        TextView event_name_event;
        public FriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        finishAffinity();
    }
    @Override
    public void onBackPressed() {

    }
}