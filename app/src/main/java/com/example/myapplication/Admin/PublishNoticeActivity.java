package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.stfalcon.multiimageview.MultiImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PublishNoticeActivity extends AppCompatActivity {

    private  Button chooseButton,uploadButton;
    TextView alertTextview;


    private static final int PICK_IMAGE = 1;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    private ProgressDialog progressDialog;
    ArrayList urlStrings;

    private ImageView imageView1,imageView2,imageView3,imageView4;
    private LinearLayout linearLayout;
    private  TextView moreTextview;
    List<Bitmap> orderImagesList=new ArrayList<>();
    private EditText noticeTitleEdittext;
    String noticeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_notice);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading..........");



        chooseButton=findViewById(R.id.publishnoticeSelectImageButtonid);
        uploadButton=findViewById(R.id.publishnoticeUploadButtonid);
        alertTextview=findViewById(R.id.alertTextviewid);
        imageView1=findViewById(R.id.publishnotice_Image1);
        imageView2=findViewById(R.id.publishnotice_Image2);
        imageView3=findViewById(R.id.publishnotice_Image3);
        imageView4=findViewById(R.id.publishnotice_Image4);
        moreTextview=findViewById(R.id.publishnotice_showmoreTExtview);
        noticeTitleEdittext=findViewById(R.id.publish_notice_TitleEdittextid);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 noticeTitle = noticeTitleEdittext.getText().toString();

                if (noticeTitle.isEmpty()) {
                    noticeTitleEdittext.setError("Enter Some Text");
                    noticeTitleEdittext.requestFocus();
                } else {


                    urlStrings = new ArrayList<>();
                    progressDialog.show();
                    alertTextview.setText("If Loading Takes to long press button again");
                    StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("NoticeImage");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    String key = databaseReference.push().getKey();
                    for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {
                        Uri IndividualImage = (Uri) ImageList.get(upload_count);
                        final StorageReference ImageName = ImageFolder.child((key + databaseReference.push().getKey()) + IndividualImage.getLastPathSegment());

                        ImageName.putFile(IndividualImage).addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ImageName.getDownloadUrl().addOnSuccessListener(
                                                new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        urlStrings.add(String.valueOf(uri));
                                                        if (urlStrings.size() == ImageList.size()) {
                                                            storeLink(urlStrings);
                                                        }

                                                    }
                                                }
                                        );
                                    }
                                }
                        );


                    }


                }
            }
        });


    }

    private void storeLink(ArrayList<String> urlStrings) {

        HashMap<String, String> hashMap = new HashMap<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String day = sdf.format(d);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String key=databaseReference.push().getKey();


        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("i"+(i+1), urlStrings.get(i));
        }
        for(int i=urlStrings.size(); i<15; i++){
            hashMap.put("i"+(i+1),"null");
        }
        hashMap.put("day",day);
        hashMap.put("date",currentDate);
        hashMap.put("time",currentTime);
        hashMap.put("key",key);
        hashMap.put("title",noticeTitle);
        hashMap.put("length", String.valueOf(urlStrings.size()));

        databaseReference.child(key).setValue(hashMap)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PublishNoticeActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    alertTextview.setText("Uploaded Successfully");
                                    uploadButton.setVisibility(View.GONE);

                                    ImageList.clear();
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PublishNoticeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {


              if (data.getClipData() != null) {

                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {
                        ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSlect = currentImageSlect + 1;
                 }


                    if(currentImageSlect>4){
                            imageView1.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);
                            imageView3.setVisibility(View.VISIBLE);
                            imageView4.setVisibility(View.VISIBLE);
                            moreTextview.setVisibility(View.VISIBLE);
                            Picasso.get().load(ImageList.get(0).toString()).into(imageView1);
                            Picasso.get().load(ImageList.get(1).toString()).into(imageView2);
                            Picasso.get().load(ImageList.get(2).toString()).into(imageView3);
                            Picasso.get().load(ImageList.get(3).toString()).into(imageView4);
                            moreTextview.setText((currentImageSlect-4)+" +more Items");
                    }else if(currentImageSlect==4){
                        imageView1.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.VISIBLE);
                        Picasso.get().load(ImageList.get(0).toString()).into(imageView1);
                        Picasso.get().load(ImageList.get(1).toString()).into(imageView2);
                        Picasso.get().load(ImageList.get(2).toString()).into(imageView3);
                        Picasso.get().load(ImageList.get(3).toString()).into(imageView4);
                    }else if(currentImageSlect==3){
                        imageView1.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        Picasso.get().load(ImageList.get(0).toString()).into(imageView1);
                        Picasso.get().load(ImageList.get(1).toString()).into(imageView2);
                        Picasso.get().load(ImageList.get(2).toString()).into(imageView3);
                    }else if(currentImageSlect==2){
                        imageView1.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.VISIBLE);
                        Picasso.get().load(ImageList.get(0).toString()).into(imageView1);
                         Picasso.get().load(ImageList.get(2).toString()).into(imageView3);
                    }





                     alertTextview.setText("You have selected" + ImageList.size() + "Images");
                    chooseButton.setVisibility(View.GONE);
                   uploadButton.setVisibility(View.VISIBLE);
                    noticeTitleEdittext.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}