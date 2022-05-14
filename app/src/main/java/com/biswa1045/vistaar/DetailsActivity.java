package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    EditText name,mobile,rollno,branch;
    String sname,smobile,srollno,sbranch;
    Button register;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference user;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    int total;
   // ArrayAdapter<String> adapter_spinner;
   // ArrayList<String> spinner_array;
  //  Spinner spinner_year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        rollno = findViewById(R.id.rollno);
        branch = findViewById(R.id.branch);
        register = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseDatabase.getInstance().getReference("Account_Details");
        reference = FirebaseDatabase.getInstance().getReference("users_count");
        firebaseUser = fAuth.getCurrentUser();
        if(firebaseUser!=null){
            String qqq = firebaseUser.getUid();
            user.child(qqq).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data, connect to internet", task.getException());
                    }
                    else {
                        if(task.getResult().getValue()!=null){
                            startActivity(new Intent(DetailsActivity.this
                                    , HomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }


                    }
                }
            });
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                 sname = name.getText().toString().trim();
                 smobile = mobile.getText().toString().trim();
                 srollno = rollno.getText().toString().trim();
                 sbranch = branch.getText().toString().trim();


                if (TextUtils.isEmpty(sname)) {
                    name.setError("name is Required.");
                    return;
                }
                if (TextUtils.isEmpty(smobile)) {
                    mobile.setError("mobile is Required.");
                    return;
                }
                if (TextUtils.isEmpty(srollno)) {
                    rollno.setError("rollno is Required.");
                    return;
                }
                if (TextUtils.isEmpty(sbranch)) {
                    branch.setError("branch is Required.");
                    return;
                }
                String userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();

                user.put("name", sname);
                user.put("mobile", smobile);
                user.put("rollno", srollno);
                user.put("branch", sbranch);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Account_Details");

                        myRef.child(userID).setValue("true");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    total += Integer.parseInt(snapshot.getValue().toString());
                                }

                                snapshot.getRef().setValue(total);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                //increase user

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            total += Integer.parseInt(snapshot.getValue().toString());
                        }

                        snapshot.getRef().setValue(total);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}