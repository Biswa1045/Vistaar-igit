package com.biswa1045.vistaar;



import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

    public class HomeActivity extends AppCompatActivity {
    private long admin_login_key = 398017;
    Dialog dialog;
        ViewFlipper viewFlipper;
VideoView intro_video;

        private  static final int RC_APP_UPDATE=100;

        TextView user_;
        DatabaseReference reference,reference8,reference9,reference10;
String intro_url,intro_name;
    @BindView(R.id.event_home_rec)
    RecyclerView rcv;
        @BindView(R.id.your_event)
        RecyclerView rcv2;
    private FirestoreRecyclerAdapter adapter,adapter2;
    private FirebaseFirestore db;
        FirebaseAuth mAuth;
        FirebaseUser user;
        String year,userID;
        int total;
        String facebook,insta,youtube;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        intro_video = findViewById(R.id.intro_video);
        findViewById(R.id.pp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(p);
                finish();
            }
        });
        if(user!=null){
            userID = mAuth.getCurrentUser().getUid();
            ImageView profile = findViewById(R.id.profile);
            String pro =String.valueOf( mAuth.getCurrentUser().getPhotoUrl());
            Glide.with(getApplicationContext())
                    .load(pro)
                    .error(R.drawable.logo)
                    .into(profile);

        }
        reference8 = FirebaseDatabase.getInstance().getReference("facebook");
        reference9 = FirebaseDatabase.getInstance().getReference("insta");
        reference10 = FirebaseDatabase.getInstance().getReference("youtube");
        reference8.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     facebook = String.valueOf(snapshot.getValue().toString());
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        reference9.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    insta = String.valueOf(snapshot.getValue().toString());
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        reference10.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    youtube = String.valueOf(snapshot.getValue().toString());
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_web = new Intent();
                intent_web.setAction(Intent.ACTION_VIEW);
                intent_web.addCategory(Intent.CATEGORY_BROWSABLE);
                intent_web.setData(Uri.parse(facebook));
                startActivity(intent_web);
                finish();
            }
        });
        findViewById(R.id.insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_web = new Intent();
                intent_web.setAction(Intent.ACTION_VIEW);
                intent_web.addCategory(Intent.CATEGORY_BROWSABLE);
                intent_web.setData(Uri.parse(insta));
                startActivity(intent_web);
                finish();
            }
        });
        findViewById(R.id.youtube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_web = new Intent();
                intent_web.setAction(Intent.ACTION_VIEW);
                intent_web.addCategory(Intent.CATEGORY_BROWSABLE);
                intent_web.setData(Uri.parse(youtube));
                startActivity(intent_web);
                finish();
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("users_count");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    total = Integer.parseInt(snapshot.getValue().toString());
                }

              //  snapshot.getRef().setValue(total);
                int count = total+10;
                TextView count_t = findViewById(R.id.count);
                count_t.setText(count+"  Joined");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        db = FirebaseFirestore.getInstance();
        RelativeLayout relativeLayout = findViewById(R.id.relative_home);
        viewFlipper = findViewById(R.id.viewflipper);
        // sliding image

        int images[] = {R.drawable.vistaar_poster, R.drawable.igit2, R.drawable.igit1, R.drawable.igit3,R.drawable.igit4};
        for(int image:images){
            flipper(image);
        }


        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        DocumentReference intro_ref = db.collection(year).document("intro_url");

        intro_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    intro_url = documentSnapshot.getString("intro_url");
                    intro_name = documentSnapshot.getString("intro_name");
                  //  downloadintro(intro_name);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        BottomNavigationView bottomNavigationNiew = findViewById(R.id.buttom_navigation);
        bottomNavigationNiew.setItemIconTintList(null);
        bottomNavigationNiew.setSelectedItemId(R.id.home_nav);
        bottomNavigationNiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:

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
                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        ButterKnife.bind(this);
        init();
        getEventList();
        if(user!=null) {
            getYourEvent();
        }

        user_ = findViewById(R.id.user_name);
        if(user!=null){
            String user_name = user.getDisplayName();

            user_.setText("Hi, "+user_name);
        }else{
            user_.setText("sign in");

        }
        user_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user==null) {
                    Intent in = new Intent(HomeActivity.this, SigninActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });

        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent in = new Intent(HomeActivity.this, SigninActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });

        findViewById(R.id.admin_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showmeem();
           //     Intent in = new Intent(HomeActivity.this,AdminActivity.class);
            //    startActivity(in);
            //    finish();
// start
/*
                dialog.setContentView(R.layout.admin_login);
                dialog.findViewById(R.id.admin_login_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText key = dialog.findViewById(R.id.key);
                        long skry = Integer.parseInt(key.getText().toString());
                        if (TextUtils.isEmpty(String.valueOf(skry))) {
                            key.setError("name is Required.");
                            return;
                        }else{
                            if(skry==admin_login_key){
                                Intent in = new Intent(HomeActivity.this,AdminActivity.class);
                                startActivity(in);
                                finish();
                            }else{
                                Toast.makeText(HomeActivity.this,"Wrong Password",Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);


 */
//    close





            }
        });




    }

    @Override
    public void onBackPressed() {
       finishAffinity();

    }
    private void init() {

     //   GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
       // rcv.setLayoutManager(gridLayoutManager);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



    }
    private void getEventList() {
        Query query = db.collection(year).document("Event").collection("Events");

        FirestoreRecyclerOptions<retrieveModelevent> response = new FirestoreRecyclerOptions.Builder<retrieveModelevent>()
                .setQuery(query, retrieveModelevent.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<retrieveModelevent, HomeActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(HomeActivity.FriendsHolder holder, int position, retrieveModelevent model) {
            //    progressBar.setVisibility(View.GONE);

                String event_name = model.getEvent_name();

                Glide.with(getApplicationContext())
                        .load(model.getEvent_photo())
                        .error(R.drawable.load)
                        .into(holder.event_thumb);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentq = new Intent(HomeActivity.this,EventDetailsActivity.class);
                        intentq.putExtra("event_name",event_name);
                        startActivity(intentq);
                    }
                });

            }

            @Override
            public HomeActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.event_short_item, group, false);

                return new HomeActivity.FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        rcv.setAdapter(adapter);
      //  progressBar2.setVisibility(View.GONE);


    }
        private void getYourEvent() {
            Query query2 = db.collection("users").document(userID).collection("Events_participate");

            FirestoreRecyclerOptions<retrieveModelevent> response2 = new FirestoreRecyclerOptions.Builder<retrieveModelevent>()
                    .setQuery(query2, retrieveModelevent.class)
                    .build();

            adapter2 = new FirestoreRecyclerAdapter<retrieveModelevent, HomeActivity.FriendsHolder>(response2) {
                @Override
                public void onBindViewHolder(HomeActivity.FriendsHolder holder, int position, retrieveModelevent model) {
                  //  progressBar2.setVisibility(View.GONE);

                    String event_name2 = model.getEvent_name();

                    Glide.with(getApplicationContext())
                            .load(model.getEvent_photo())
                            .error(R.drawable.load)
                            .into(holder.event_thumb);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentq = new Intent(HomeActivity.this,EventDetailsActivity.class);
                            intentq.putExtra("event_name",event_name2);
                            startActivity(intentq);
                        }
                    });

                }

                @Override
                public HomeActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.event_short_item, group, false);

                    return new HomeActivity.FriendsHolder(view);
                }

                @Override
                public void onError(FirebaseFirestoreException e) {
                    Log.e("error", e.getMessage());
                }
            };

            adapter2.notifyDataSetChanged();
            rcv2.setAdapter(adapter2);
         //   progressBar2.setVisibility(View.GONE);


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
      //  userID = mAuth.getCurrentUser().getUid();
        adapter.startListening();
        if(user!=null){
            adapter2.startListening();
        }



    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        if(user!=null) {
            adapter2.stopListening();
        }
        finishAffinity();
    }
    /*
        @SuppressLint("StaticFieldLeak")
        private void downloadintro( String fileName) {
            new AsyncTask<Void, Integer, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {

                    return download_video();
                }

                @Nullable
                private Boolean download_video() {
                    try {
                        File file = getFileStreamPath(fileName);
                        if (file.exists())
                            return true;

                        try {




                            String UU = intro_url;
                            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                            URL u = new URL(UU);
                          //
                            URLConnection connection = u.openConnection();
                            int contentLength = connection.getContentLength();
                            InputStream input = new BufferedInputStream(u.openStream());
                            byte data[] = new byte[contentLength];
                            long total = 0;
                            int count;
                            while ((count = input.read(data)) != -1) {
                                total += count;
                                publishProgress((int) ((total * 100) / contentLength));
                                fileOutputStream.write(data, 0, count);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            input.close();
                            return true;




                        } catch (final Exception e) {
                            e.printStackTrace();
                            return false;
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;

                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);

                }


                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        openvideo(fileName);

                    }
                }
            }.execute();
        }

        private void openvideo(String fileName) {

            try {
                File file = getFileStreamPath(fileName);
                Toast.makeText(this, ""+fileName+file, Toast.LENGTH_SHORT).show();
                Log.e("file", "file:" + file.getAbsolutePath());
                intro_video.setVideoPath(file.getAbsolutePath());
                intro_video.requestFocus();
                intro_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        intro_video.start();
                        findViewById(R.id.video_p).setVisibility(View.GONE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

     */
        public void flipper(int image){
            ImageView imageview = new ImageView(getApplicationContext());
            imageview.setBackgroundResource(image);
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imageview);
            viewFlipper.setFlipInterval(2500);
            viewFlipper.setAutoStart(true);
            viewFlipper.setInAnimation(getApplicationContext(),android.R.anim.slide_out_right);
            viewFlipper.setInAnimation(getApplicationContext(),android.R.anim.slide_in_left);
        }
        public void showmeem(){
            ImageView img;
            dialog.setContentView(R.layout.image_dialog);
            img = dialog.findViewById(R.id.img_popup);
            Glide.with(getApplicationContext())
                    .load(R.drawable.meem)
                    .error(R.drawable.ic_launcher_background)
                    .into(img);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();
        }
        public static void deleteCache(Context context) {
            try {
                File dir = context.getCacheDir();
                deleteDir(dir);
            } catch (Exception e) { e.printStackTrace();}
        }

        public static boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if(dir!= null && dir.isFile()) {
                return dir.delete();
            } else {
                return false;
            }
        }
}