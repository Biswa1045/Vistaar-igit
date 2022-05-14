package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecActivity extends AppCompatActivity {


    @BindView(R.id.progressBar_admin)
    ProgressBar progressBar;

    @BindView(R.id.admin_rec)
    RecyclerView rcv;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);
         year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        ButterKnife.bind(this);
        init();
        getLikeList();
    }
    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rcv.setLayoutManager(gridLayoutManager);

        db = FirebaseFirestore.getInstance();
    }
    private void getLikeList() {
        Query query = db.collection(year).document("Event").collection("Events");

        FirestoreRecyclerOptions<retrieveModelevent> response = new FirestoreRecyclerOptions.Builder<retrieveModelevent>()
                .setQuery(query, retrieveModelevent.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<retrieveModelevent, RecActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, retrieveModelevent model) {
                progressBar.setVisibility(View.GONE);

                String event_name = model.getEvent_name();

                Glide.with(getApplicationContext())
                        .load(model.getEvent_photo())
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.event_thumb);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentq = new Intent(RecActivity.this,ParticipantsActivity.class);
                        intentq.putExtra("event_name",event_name);
                        startActivity(intentq);
                    }
                });

            }

            @Override
            public RecActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.event_short_item, group, false);

                return new RecActivity.FriendsHolder(view);
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

        @BindView(R.id.short_img)
        ImageView event_thumb;

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
    }

    @Override
    public void onBackPressed() {
        Intent in  = new Intent(RecActivity.this,AdminActivity.class);
        startActivity(in);
        finish();
    }
}