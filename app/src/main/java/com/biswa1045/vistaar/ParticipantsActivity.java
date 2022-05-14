package com.biswa1045.vistaar;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantsActivity extends AppCompatActivity {
String name;
    @BindView(R.id.ppp_par_admin)
    ProgressBar progressBar;

    @BindView(R.id.par_rec)
    RecyclerView rcv;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);
         year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        name = getIntent().getStringExtra("event_name");
        ButterKnife.bind(this);
        init();
        getParList();
    }
    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        rcv.setLayoutManager(gridLayoutManager);

        db = FirebaseFirestore.getInstance();
    }
    private void getParList() {
        Query query = db.collection(year).document("Event").collection("Events").document(name).collection("participants");

        FirestoreRecyclerOptions<modelAll> response = new FirestoreRecyclerOptions.Builder<modelAll>()
                .setQuery(query, modelAll.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<modelAll, ParticipantsActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(ParticipantsActivity.FriendsHolder holder, int position, modelAll model) {
                progressBar.setVisibility(View.GONE);

                String captain = model.getEvent_team_captain();
                String team =model.getEvent_team();
                String mobile = model.getEvent_team_mobile();
                String rollno =model.getEvent_team_rollno();
                String branch =model.getEvent_team_branch();
                String email =model.getEvent_email();
                holder.captain.setText(captain);
                holder.team.setText(team);
                holder.mobile.setText(mobile);
                holder.rollno.setText(rollno);
                holder.branch.setText(branch);
                holder.email.setText(email);




            }

            @Override
            public ParticipantsActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.participants_item, group, false);

                return new ParticipantsActivity.FriendsHolder(view);
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

        @BindView(R.id.captain_name_item)
        TextView captain;
        @BindView(R.id.branch_item)
        TextView branch;
        @BindView(R.id.rollno_item)
        TextView rollno;
        @BindView(R.id.team_item)
        TextView team;
        @BindView(R.id.mobile_item)
        TextView mobile;
        @BindView(R.id.email_item)
        TextView email;

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
        Intent in =new Intent(ParticipantsActivity.this,RecActivity.class);
        startActivity(in);
        finish();
    }
}