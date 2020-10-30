package com.example.myapplication.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Admin.DataModuler.TeachercodeDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.ChatAdapter;
import com.example.myapplication.Teacher.DataModuler.ChatData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherChatingActivity extends AppCompatActivity {



    private Toolbar chatToolbar;

    private ImageView userImage;
    private TextView userName;

    private  String name,image,reciverUserid;
    private String senderUserid;

    private DatabaseReference rootRef;

    private EditText messageBoxEdittext;
    private ImageButton sendMessageButton,sendFilesButton;

    private RecyclerView recyclerView;


    private List<ChatData> dataList=new ArrayList<>();

    private ChatAdapter adapter;

    private UsersShared shared;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chating);
    shared=new UsersShared(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        saveActionBar();

        name=getIntent().getStringExtra("name");
        image=getIntent().getStringExtra("image");
        reciverUserid=getIntent().getStringExtra("uid");

        if(image.equals("")){
            Picasso.get().load(R.drawable.avatar).into(userImage);
        }else {
            Picasso.get().load(image).into(userImage);
        } userName.setText(name);


        rootRef= FirebaseDatabase.getInstance().getReference().child("collage");

        senderUserid=shared.getId();



        sendMessageButton=findViewById(R.id.send_message_btn);
        messageBoxEdittext=findViewById(R.id.input_message);
        sendFilesButton=findViewById(R.id.send_files_btn);

        recyclerView=findViewById(R.id.private_message_list_of_users);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new ChatAdapter(this,TeacherChatingActivity.this,dataList,image);
        recyclerView.setAdapter(adapter);







        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBoxEdittext.getText().toString().isEmpty()){
                    Toast.makeText(TeacherChatingActivity.this, "Write Something", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(messageBoxEdittext.getText().toString());
                }
            }
        });





    }



    @Override
    protected void onStart() {
        super.onStart();

        dataList.clear();
        rootRef.keepSynced(true);
        rootRef.child("Message").child(senderUserid).child(reciverUserid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataList.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){

                            ChatData message=snapshot1.getValue(ChatData.class);


                            dataList.add(message);
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });










    }

    private void saveActionBar() {
        chatToolbar=findViewById(R.id.chat_Toolbarid);
        setSupportActionBar(chatToolbar);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView =layoutInflater.inflate(R.layout.custom_chat_ber,null);
        actionBar.setCustomView(actionBarView);

        userImage=findViewById(R.id.custom_profile_image);
        userName=findViewById(R.id.custom_profile_name);
    }


    private void sendMessage(String message) {



        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM dd,yyy");
        String currentdate=simpleDateFormat.format(calendar.getTime());



        SimpleDateFormat  simpleDateFormat2=new SimpleDateFormat("hh:mm a");
        String currenttime=simpleDateFormat2.format(calendar.getTime());

        String messageSenderRef="Message/"+senderUserid+"/"+reciverUserid;
        String messageReciverRef="Message/"+reciverUserid+"/"+senderUserid;

        DatabaseReference userMessageKeyref=rootRef.child("Message").child(senderUserid).child(reciverUserid).push();

        String messageKey=userMessageKeyref.getKey();


        Map messageTextBody=new HashMap();
        messageTextBody.put("message",message);
        messageTextBody.put("from",senderUserid);
        messageTextBody.put("type","text");
        messageTextBody.put("to",senderUserid);
        messageTextBody.put("messageId",messageKey);
        messageTextBody.put("time",currenttime);
        messageTextBody.put("date",currentdate);





        Map messageBodyDetails=new HashMap();
        messageBodyDetails.put(messageSenderRef+"/"+messageKey,messageTextBody);
        messageBodyDetails.put(messageReciverRef+"/"+messageKey,messageTextBody);



        rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    messageBoxEdittext.setText("");
                    Toast.makeText(TeacherChatingActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}