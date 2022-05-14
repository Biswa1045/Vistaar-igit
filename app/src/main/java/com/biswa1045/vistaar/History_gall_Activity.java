package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
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

public class History_gall_Activity extends AppCompatActivity {
    @BindView(R.id.p_gallery_his)
    ProgressBar progressBar;

    @BindView(R.id.rcv_gallery_his)
    RecyclerView rcv;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    String year,year_his;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_gall);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        dialog = new Dialog(this);
        year_his = getIntent().getStringExtra("year");
        TextView text =findViewById(R.id.back_gall_his);
        text.setText(year_his);
        RelativeLayout relativeLayout = findViewById(R.id.rel_his_gall);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();


        ButterKnife.bind(this);
        init();
        String default_s = "photo" ;
        getPhotoList(default_s);





    }

    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rcv.setLayoutManager(gridLayoutManager);

        db = FirebaseFirestore.getInstance();
    }
    private void getPhotoList(String item) {
        Query query = db.collection(year_his).document("Gallery").collection(item);

        FirestoreRecyclerOptions<event_photo> response = new FirestoreRecyclerOptions.Builder<event_photo>()
                .setQuery(query, event_photo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<event_photo, History_gall_Activity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(History_gall_Activity.FriendsHolder holder, int position, event_photo model) {
                progressBar.setVisibility(View.GONE);
                String img_s = model.getPhoto_link();
                Glide.with(getApplicationContext())
                        .load(img_s)
                        .error(R.drawable.load)
                        .into(holder.photo_gallery_item);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showpopup(img_s);
                    }
                });

            }

            @Override
            public History_gall_Activity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.photo_item, group, false);

                return new History_gall_Activity.FriendsHolder(view);
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

        @BindView(R.id.photo_gallery_item)
        ImageView photo_gallery_item;

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
    public void showpopup(String img_s){
        ImageView img;
        dialog.setContentView(R.layout.image_dialog);
        img = dialog.findViewById(R.id.img_popup);
        Glide.with(getApplicationContext())
                .load(img_s)
                .error(R.drawable.ic_launcher_background)
                .into(img);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(History_gall_Activity.this,HistoryActivity.class);
        startActivity(in);
        finish();
    }
}