package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {
Dialog dialog;
int PICK_VIDEO_REQUEST = 100;
int PICK_EVENT_REQUEST = 101;
int PICK_EVENT_DETAILS_REQUEST = 102;
int PICK_GALLERY_PHOTO_REQUEST= 103;
int PICK_GALLERY_VIDEO_REQUEST= 104;
ImageView event_img,event_details_img;
Uri uri_intro_video,event,event_details,gallery_photo,gallery_video;
String event_name_s,event_time_s,event_location_s,event_link_s,payment_chk,payment_check;
    StorageReference storageReference;
    VideoView videoView;
    String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    Uri firebase_intro_vdo_uri,firebase_event_uri,firebase_event_details_uri,firebase_photo_uri,firebase_video_uri;
    private FirebaseFirestore firestore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_admin);
         dialog = new Dialog(this);
         videoView = dialog.findViewById(R.id.intro_video);

         event_img = dialog.findViewById(R.id.event);

         event_details_img = dialog.findViewById(R.id.event_details);
         firestore = FirebaseFirestore.getInstance();
         storageReference = FirebaseStorage.getInstance().getReference().child(year);
         findViewById(R.id.add_intro).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 add_intro();
             }
         });
         findViewById(R.id.add_photo).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 select_photo();
             }
         });
        findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.add_event_dialog);
                dialog.findViewById(R.id.event_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SelectEventImage();
                    }
                });
                dialog.findViewById(R.id.event_details_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SelectEventDetailsImage();
                    }
                });
                Button button = dialog.findViewById(R.id.event_upload);
                CheckBox chk = dialog.findViewById(R.id.payment_check);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText event_name_e = dialog.findViewById(R.id.event_name);
                        EditText event_time_e = dialog.findViewById(R.id.event_time);
                        EditText event_location_e = dialog.findViewById(R.id.event_location);
                        EditText event_link_e = dialog.findViewById(R.id.event_link);

                        event_name_s = event_name_e.getText().toString().trim();
                        event_time_s = event_time_e.getText().toString().trim();
                        event_location_s = event_location_e.getText().toString().trim();
                        event_link_s = event_link_e.getText().toString().trim();
                        if (TextUtils.isEmpty(event_name_s)) {
                            event_name_e.setError("name is Required.");
                            return;
                        }else
                        if (TextUtils.isEmpty(event_time_s)) {
                            event_time_e.setError("time is Required.");
                            return;
                        }else
                        if (TextUtils.isEmpty(event_location_s)) {
                            event_location_e.setError("location is Required.");
                            return;
                        }
                        if (TextUtils.isEmpty(event_link_s)) {
                            event_link_e.setError("link is Required.");
                            return;
                        }else{
                            if(chk.isChecked()){
                                 payment_chk = "chk";
                            }else{
                                payment_chk = "not";
                            }
                            upoadevent(payment_chk);
                        }

                    }
                });





                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

            }
        });
        findViewById(R.id.event_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(AdminActivity.this,RecActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        overridePendingTransition(0,0);
    }
    public void add_intro(){
        dialog.setContentView(R.layout.add_video_dialog);
        Button button = dialog.findViewById(R.id.sel_intro_vdo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectVideo();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
    public void select_photo(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from here..."), PICK_GALLERY_PHOTO_REQUEST);

    }
    private void SelectEventImage() {

        Intent intent_event = new Intent();
        intent_event.setType("image/*");
        intent_event.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent_event, "Select Image from here..."), PICK_EVENT_REQUEST);
    }
    private void SelectEventDetailsImage() {

        Intent intent_detail = new Intent();
        intent_detail.setType("image/*");
        intent_detail.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent_detail, "Select Image from here..."), PICK_EVENT_DETAILS_REQUEST);
    }
    private void SelectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select video from here..."), PICK_VIDEO_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri_intro_video = data.getData();
          //  videoView.setVideoURI(uri_intro_video);
            uploadintroVideo();
        }
        if (requestCode == PICK_EVENT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            event = data.getData();
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == PICK_EVENT_DETAILS_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            event_details = data.getData();
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == PICK_GALLERY_PHOTO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            gallery_photo = data.getData();
            uploadgalleryphoto();
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

        }

    }
    private String getfileExt(Uri videoUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
    }
    private void uploadintroVideo() {

        if(uri_intro_video != null ){
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref =storageReference.child("intro_video").child(System.currentTimeMillis()+"."+getfileExt(uri_intro_video));
            ref.putFile(uri_intro_video)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                     firebase_intro_vdo_uri = uri;
                                    Map<String, Object> intro_map = new HashMap<>();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    intro_map.put("intro_url", String.valueOf(firebase_intro_vdo_uri));
                                    intro_map.put("intro_name", String.valueOf(formatter.format(date)));


                                    DocumentReference eventReference = firestore.collection(year).document("intro_url");

                                    eventReference.set(intro_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext()," Video Upload Successful",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }
                            });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminActivity.this, "Upload Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (double)progress + "%");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void upoadevent(String p_chk){
     //   p_event.setVisibility(View.VISIBLE);
        if(event!=null&&event_details!=null){
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref_event =storageReference.child("events").child(event_name_s).child(event_name_s+"."+getfileExt(event));
            StorageReference ref_event_details =storageReference.child("events").child(event_name_s).child(event_name_s+"_details"+"."+getfileExt(event_details));

            ref_event.putFile(event)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref_event.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    firebase_event_uri = uri;
                                    ref_event_details.putFile(event_details)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    ref_event_details.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            firebase_event_details_uri = uri;
                                                            if(p_chk.equals("chk")){
                                                                payment_check = "true";
                                                            }else{
                                                                payment_check = "false";
                                                            }
                                                            Map<String, Object> event_map = new HashMap<>();

                                                            event_map.put("event_name",event_name_s);
                                                            event_map.put("event_photo",String.valueOf(firebase_event_uri));
                                                            event_map.put("event_datails", String.valueOf(firebase_event_details_uri));
                                                            event_map.put("event_time", event_time_s);
                                                            event_map.put("event_location",event_location_s);
                                                            event_map.put("event_link",event_link_s);
                                                            event_map.put("payment_chk",payment_check);

                                                            DocumentReference eventReference = firestore.collection(year).document("Event").collection("Events").document(event_name_s);

                                                            eventReference.set(event_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    Toast.makeText(getApplicationContext()," Event Upload Successful",Toast.LENGTH_SHORT).show();
                                                                    dialog.dismiss();
                                                                    progressDialog.dismiss();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });






                                                        }
                                                    });


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AdminActivity.this, "Upload Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) .addOnProgressListener(
                                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(
                                                        UploadTask.TaskSnapshot taskSnapshot)
                                                {
                                                    double progress
                                                            = (100.0
                                                            * taskSnapshot.getBytesTransferred()
                                                            / taskSnapshot.getTotalByteCount());
                                                    progressDialog.setMessage(
                                                            "Uploaded "
                                                                    + (double)progress + "%");
                                                }
                                            });



                                }
                            });
                            //second action






                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminActivity.this, "Upload Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (double)progress + "%");
                        }
                    });




        }else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadgalleryphoto() {

        if(gallery_photo != null ){
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref =storageReference.child("gallery").child(System.currentTimeMillis()+"."+getfileExt(gallery_photo));
            ref.putFile(gallery_photo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    firebase_photo_uri = uri;
                                    Map<String, Object> photo_map = new HashMap<>();

                                    photo_map.put("photo_link", String.valueOf(firebase_photo_uri));
                                    String id = firestore.collection(year).document("Gallery").collection("photo").document().getId();

                                    DocumentReference eventReference = firestore.collection(year).document("Gallery").collection("photo").document(id);

                                    eventReference.set(photo_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext()," photo Upload Successful",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }
                            });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminActivity.this, "Upload Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (double)progress + "%");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    //recycle view function



}
