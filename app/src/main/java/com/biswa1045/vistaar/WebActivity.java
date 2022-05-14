package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    Dialog dialog;
    String glink,EventName,eventphoto,userID,year;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
         dialog = new Dialog(this);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
         db = FirebaseFirestore.getInstance();
         webView = findViewById(R.id.link_web);
         glink =  getIntent().getStringExtra("glink");
         EventName  = getIntent().getStringExtra("EventName");
         eventphoto  = getIntent().getStringExtra("eventphoto");
         webView.loadUrl(glink);
         mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(WebActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
    public void check(){
        dialog.setContentView(R.layout.check);
        TextView no = dialog.findViewById(R.id.no);
        TextView yes = dialog.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(WebActivity.this,HomeActivity.class);
                startActivity(in);
                finish();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = mAuth.getCurrentUser().getUid();
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
                        startActivity(new Intent(WebActivity.this
                                , DetailsActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                       finish();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}