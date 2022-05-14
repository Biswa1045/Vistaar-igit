package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    String eventphoto,eventdetails,eventlocation,eventtime,EventName,branch,mobile,name,rollno,eventlink,eventpayment;
    ImageView Event_poster,Event_Details;
    TextView event_name_toolbar;
    Dialog dialog;
    FirebaseUser user;
    FirebaseAuth mAuth;

    String userID;
    LinearLayout l;
    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        dialog = new Dialog(this);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        EventName = getIntent().getStringExtra("event_name");
        event_name_toolbar = findViewById(R.id.event_name_toolbar);
        event_name_toolbar.setText(EventName);
        Event_poster = findViewById(R.id.eventposter);
        Event_Details = findViewById(R.id.eventdetails);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null){
            userID = mAuth.getCurrentUser().getUid();
        }

         l = findViewById(R.id.layout_event_details);

        findViewById(R.id.back_event_det).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(EventDetailsActivity.this,HomeActivity.class);
                startActivity(in);
                finish();
            }
        });
        if(user!=null) {
        DatabaseReference databaseReference9= FirebaseDatabase.getInstance().getReference("Event_participant").child(year).child(EventName);
        databaseReference9.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userID).exists()){
                   l.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //extract user data on firestore

    DocumentReference docuser = db.collection("users").document(userID);
    docuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    branch = document.getString("branch");
                    mobile = document.getString("mobile");
                    name = document.getString("name");
                    rollno = document.getString("rollno");


                }
            } else {
                Toast.makeText(EventDetailsActivity.this, "check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    });
}
        DocumentReference docRef = db.collection(year).document("Event").collection("Events").document(EventName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        eventphoto =  document.getString("event_photo");
                        eventdetails =  document.getString("event_datails");
                        eventlink =  document.getString("event_link");
                        eventpayment =  document.getString("payment_chk");

                        Glide.with(getApplicationContext())
                                .load(eventphoto)
                                .error(R.drawable.load)
                                .into(Event_poster);
                        Glide.with(getApplicationContext())
                                .load(eventdetails)
                                .error(R.drawable.load)
                                .into(Event_Details);

                    }
                } else {
                    Toast.makeText(EventDetailsActivity.this, "check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });




        findViewById(R.id.event_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null) {

                    dialog.setContentView(R.layout.team_details);
                    Button button = dialog.findViewById(R.id.team_submit);
                    TextView link = dialog.findViewById(R.id.google_form_link);
                    LinearLayout lllll = dialog.findViewById(R.id.par_layout);
                    if(eventpayment.equals("true")){
                        lllll.setVisibility(View.INVISIBLE);
                    }else{

                    }
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent_web = new Intent();
                            intent_web.setAction(Intent.ACTION_VIEW);
                            intent_web.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent_web.setData(Uri.parse(eventlink));
                            startActivity(intent_web);
                            finish();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            EditText team1 = dialog.findViewById(R.id.participate_details_1);
                            EditText team2 = dialog.findViewById(R.id.participate_details_2);
                            EditText team3 = dialog.findViewById(R.id.participate_details_3);
                            EditText team4 = dialog.findViewById(R.id.participate_details_4);
                            String team_name_s1 = team1.getText().toString().trim();
                            String team_name_s2 = team2.getText().toString().trim();
                            String team_name_s3 = team3.getText().toString().trim();
                            String team_name_s4 = team4.getText().toString().trim();

                            if (TextUtils.isEmpty(team_name_s1)) {
                                team1.setError("names are Required.");
                                return;

                            }else {
                                String email = user.getEmail();
                                String s = team_name_s1 + "," + team_name_s2 + "," + team_name_s3 + "," + team_name_s4;
                                Map<String, Object> event_par = new HashMap<>();

                                event_par.put("event_team", s);
                                event_par.put("event_email", email);
                                event_par.put("event_team_captain", name);
                                event_par.put("event_team_branch", branch);
                                event_par.put("event_team_rollno", rollno);
                                event_par.put("event_team_mobile", mobile);
                                DocumentReference eventReference = db.collection(year).document("Event").collection("Events").document(EventName).collection("participants").document(userID);

                                eventReference.set(event_par).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        button.setEnabled(false);
                                        Map<String, Object> event_show = new HashMap<>();

                                        event_show.put("event_name", EventName);
                                        event_show.put("event_photo", eventphoto);

                                        DocumentReference eventReference2 = db.collection("users").document(userID).collection("Events_participate").document(EventName);

                                        eventReference2.set(event_show).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference myRef = database.getReference("Event_participant").child(year);

                                                myRef.child(EventName).child(userID).setValue("true");
                                                Toast.makeText(getApplicationContext(), " Event Upload Successful", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                                l.setVisibility(View.GONE);


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }

                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);


            }else{
                    Toast.makeText(EventDetailsActivity.this,"Create A Account",Toast.LENGTH_SHORT).show();
                }



            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(EventDetailsActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
}