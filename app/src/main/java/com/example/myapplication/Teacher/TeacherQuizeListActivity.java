package com.example.myapplication.Teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.QuizListAdapter;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherQuizeListActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView quizeListRecyclerviewid;
    private FloatingActionButton floatingActionButton;

    // variable for database

    private  DatabaseReference databaseReference;
    private StorageReference storageReference;
    private  Uri imageUri;

    private UsersShared shared;
    String userId;
    //variable for diolouge

    private  EditText quizNameEidttext;
    private  Spinner accessSpinner;
    private  Button quizSaveButton;
    private String quizeName,selectedAccess;
    private  String quizKey;

    String[] access={"Only me","All Teachers","All Student"};
    private SpinnerCustomAdapter accessSpinnerAdapter;

    private List<QuizeDataModuler> quizDataList=new ArrayList<>();
    private QuizListAdapter quizListAdapter;

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_quize_list);

        toolbar=findViewById(R.id.teacher_Quize_ListToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Quiz List");

        quizeListRecyclerviewid=findViewById(R.id.quiz_listRecyclerviewid);
        floatingActionButton=findViewById(R.id.addQuizeFloatingButtonid);
        quizeListRecyclerviewid.setHasFixedSize(true);
        quizeListRecyclerviewid.setLayoutManager(new LinearLayoutManager(this));

        quizListAdapter=new QuizListAdapter(this,quizDataList);
        quizeListRecyclerviewid.setAdapter(quizListAdapter);

        progressDialog=new ProgressDialog(this);



        accessSpinnerAdapter=new SpinnerCustomAdapter(this,access);

        shared=new UsersShared(TeacherQuizeListActivity.this);
        userId=shared.getId();
        databaseReference= FirebaseDatabase.getInstance().getReference().child(userId).child("Quiz");
        storageReference= FirebaseStorage.getInstance().getReference().child("QuizImage");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    quizSaveDiolouge();
            }
        });



        quizListAdapter.setOnItemClickListner(new QuizListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TeacherQuizeListActivity.this,TeacherQuizQuestionListActivity.class);
                intent.putExtra("quizKey",quizDataList.get(position).getKey());
                intent.putExtra("quizName",quizDataList.get(position).getQuizName());
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
                    deleteQuiz(quizDataList.get(position).getKey());
            }

            @Override
            public void onUpdate(int position) {
                quizKey=quizDataList.get(position).getKey();
                quizUpdateDiolouge(position);
            }

            @Override
            public void onImageSet(int position) {
                quizKey=quizDataList.get(position).getKey();
                    openfilechooser();
            }
        });
    }



    public void deleteQuiz(String key){
        progressDialog.setTitle("Deleting Quiz");
        databaseReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(TeacherQuizeListActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void setQuizImage(){
        progressDialog.setTitle("Uploading Image");
        progressDialog.show();
        String imagename=databaseReference.push().getKey();
        StorageReference filePath=storageReference.child(imagename+databaseReference.push().getKey()
        +"."+getFileExtension(imageUri));

        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloaduri=urlTask.getResult();

                databaseReference.child(quizKey).child("image").setValue(downloaduri.toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(TeacherQuizeListActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                    }
                                }
                            });


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setTitle("Loading........");
        progressDialog.show();
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        quizDataList.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            String key=snapshot1.child("key").getValue().toString();
                            String quizNamee=snapshot1.child("quizName").getValue().toString();
                            String accessType=snapshot1.child("accesstype").getValue().toString();
                            String image=snapshot1.child("image").getValue().toString();
                            QuizeDataModuler dataModuler=new QuizeDataModuler(key,quizNamee,accessType,image);
                            quizDataList.add(dataModuler);
                            quizListAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openfilechooser() {

        Intent intentf=new Intent();
        intentf.setType("image/*");
        intentf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentf,1);

    }

    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }
    public void quizSaveDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizeListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quize_diolouge_layoute,null);
        builder.setView(view);
        quizNameEidttext=view.findViewById(R.id.quiz_diolouge_QuizeNameEdittext);
        accessSpinner=view.findViewById(R.id.quize_diolouge_AccessSpinnerid);
        quizSaveButton=view.findViewById(R.id.quize_SaveButtonid);

        accessSpinner.setAdapter(accessSpinnerAdapter);



        final AlertDialog dialog=builder.create();
        dialog.show();

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccess=access[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quizSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizeName=quizNameEidttext.getText().toString();
              if(quizeName.isEmpty()){
                    quizNameEidttext.setError("Enter Quize Name");
                  quizNameEidttext.requestFocus();
                }else if(selectedAccess.isEmpty()){
                    Toast.makeText(TeacherQuizeListActivity.this, "Please Choose Access Type", Toast.LENGTH_SHORT).show();
                }else{
                    saveQuiz(dialog);
                }
            }
        });
    }
    public void quizUpdateDiolouge(int position) {

        QuizeDataModuler d=quizDataList.get(position);

        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizeListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quize_diolouge_layoute,null);
        builder.setView(view);
        quizNameEidttext=view.findViewById(R.id.quiz_diolouge_QuizeNameEdittext);
        accessSpinner=view.findViewById(R.id.quize_diolouge_AccessSpinnerid);
        quizSaveButton=view.findViewById(R.id.quize_SaveButtonid);

        accessSpinner.setAdapter(accessSpinnerAdapter);

        quizNameEidttext.setText(d.getQuizName());
        quizSaveButton.setText("Update");
        int i=0;
        if(d.getAccesstype().equals("Only me")){
            i=0;
        }else if(d.getAccesstype().equals("All Teachers")){
            i=1;
        }else if(d.getAccesstype().equals("All Student")){
            i=2;
        }
        accessSpinner.setSelection(i);

        final AlertDialog dialog=builder.create();
        dialog.show();

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccess=access[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quizSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizeName=quizNameEidttext.getText().toString();
              if(quizeName.isEmpty()){
                    quizNameEidttext.setError("Enter Quize Name");
                  quizNameEidttext.requestFocus();
                }else if(selectedAccess.isEmpty()){
                    Toast.makeText(TeacherQuizeListActivity.this, "Please Choose Access Type", Toast.LENGTH_SHORT).show();
                }else{
                    updateQuize(dialog);
                }
            }
        });
    }

  public void saveQuiz(final Dialog dialog){

        String key=databaseReference.push().getKey();

      QuizeDataModuler qData=new QuizeDataModuler(key,quizeName,selectedAccess,"null");

      databaseReference.child(key).setValue(qData).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  dialog.dismiss();
                  Toast.makeText(TeacherQuizeListActivity.this, "Quiz Added Successful", Toast.LENGTH_SHORT).show();
              }
          }
      });
  }

  public void updateQuize(final Dialog dialog){
        progressDialog.setTitle("Updating......");
        progressDialog.show();
         HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("quizName",quizeName);
        hashMap.put("accesstype",selectedAccess);
      databaseReference.child(quizKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  dialog.dismiss();
                  progressDialog.dismiss();
                  Toast.makeText(TeacherQuizeListActivity.this, "Quiz Update Successful", Toast.LENGTH_SHORT).show();
              }
          }
      });
  }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            setQuizImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}