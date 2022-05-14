package com.biswa1045.vistaar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    CardView btsignin;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ImageView mainFrame = ((ImageView) findViewById(R.id.logo1));
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.anim);
        mainFrame.startAnimation(hyperspaceJumpAnimation);





        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("808421566247-ek0041icspar7f5f8voglp8720ep7had.apps.googleusercontent.com")


                .requestEmail()
                .build();
        // int sign in clint
        googleSignInClient = GoogleSignIn.getClient(SigninActivity.this
                , googleSignInOptions);

        btsignin = findViewById(R.id.signin);
        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
//
        findViewById(R.id.visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent on = new Intent(SigninActivity.this,HomeActivity.class);
                startActivity(on);
                finish();
            }
        });
        if(firebaseUser!=null){
            user = FirebaseDatabase.getInstance().getReference("Account_Details");
            String qqq = firebaseUser.getUid();
            user.child(qqq).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data, connect to internet", task.getException());
                        String s = "connect to internet";
                        displayToast(s);
                    }
                    else {
                        if(task.getResult().getValue()!=null){
                            startActivity(new Intent(SigninActivity.this
                                    , HomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else {
                            LinearLayout l = findViewById(R.id.signin_layout);
                            l.setVisibility(View.VISIBLE);
                        }


                    }
                }
            });
        }else
        {
            LinearLayout l = findViewById(R.id.signin_layout);
            l.setVisibility(View.VISIBLE);
        }

       /* if (firebaseUser != null) {
            startActivity(new Intent(SigninActivity.this
                    , DetailsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }



        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                String s = "Google sign in successful";
                displayToast(s);
                //int sign in acctn
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        , null);
                        //check cre
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(SigninActivity.this
                                                    , DetailsActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            displayToast("Authentication successful");
                                        } else {
                                            displayToast("Authentication Failed:" + task.getException()
                                                    .getMessage());
                                            String s = "Authentication Failed:";
                                            displayToast(s);
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                    String se = "Authentication Failed:"+e;
                    displayToast(se);
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
