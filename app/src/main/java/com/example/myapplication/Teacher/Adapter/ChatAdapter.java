package com.example.myapplication.Teacher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.DataModuler.ChatData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {


    private  int counter=1;
    private  DatabaseReference databaseReference;
    private  Context context;
    private List<ChatData> dataList=new ArrayList<>();
    private  Activity activity;
    String mImage;

    public ChatAdapter(Context context, Activity activity, List<ChatData> dataList, String image) {
        this.context = context;
        this.dataList = dataList;
        this.activity=activity;
        this.mImage=image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final ChatData selecteditem=dataList.get(position);
        String fromUserid=selecteditem.getFrom();

        String messageType=selecteditem.getType();

if(mImage.equals("")){
    Picasso.get().load(R.drawable.avatar).into(holder.reciverProfileImage);
}else {
    Picasso.get().load(mImage).placeholder(R.drawable.profile).into(holder.reciverProfileImage);
}

        holder.receiverMessageTExt.setVisibility(View.GONE);
        holder.reciverProfileImage.setVisibility(View.GONE);
        holder.senderMessageTExt.setVisibility(View.GONE);
        holder.messageSenderPicture.setVisibility(View.GONE);
        holder.messageReceiverPicture.setVisibility(View.GONE);
        holder.receiverMessageTime.setVisibility(View.GONE);
        holder.senderMessageTime.setVisibility(View.GONE);

if(messageType.equals("text")){
    UsersShared shared=new UsersShared(activity);
    String teacherCode=shared.getId();

    if(fromUserid.equals(teacherCode)){
        holder.senderMessageTExt.setVisibility(View.VISIBLE);
        holder.senderMessageTExt.setBackgroundResource(R.drawable.sender_messages_layout);
        holder.senderMessageTExt.setText(selecteditem.getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter%2==0){


                    if(holder.receiverMessageTime.getVisibility()==View.VISIBLE || holder.senderMessageTime.getVisibility()==View.VISIBLE){
                        holder.receiverMessageTime.setVisibility(View.GONE);
                        holder.senderMessageTime.setVisibility(View.GONE);
                    }else {


                        holder.senderMessageTime.setVisibility(View.VISIBLE);
                        holder.senderMessageTime.setText(selecteditem.getDate() + "  At: " + selecteditem.getTime());
                    }
                      }else{
                    holder.senderMessageTime.setVisibility(View.GONE);
                }

            }
        });

    }else{

        holder.reciverProfileImage.setVisibility(View.VISIBLE);
        holder.receiverMessageTExt.setVisibility(View.VISIBLE);
        holder.receiverMessageTExt.setBackgroundResource(R.drawable.recever_message_layout);



        holder.receiverMessageTExt.setText(selecteditem.getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter%2==0){

                    holder.receiverMessageTime.setVisibility(View.VISIBLE);
                    holder.receiverMessageTime.setText(selecteditem.getDate()+"  At: "+selecteditem.getTime());
                }else{
                    holder.receiverMessageTime.setVisibility(View.GONE);
                }

            }
        });





    }









}














    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{
        public TextView senderMessageTExt,receiverMessageTExt;
        public CircleImageView reciverProfileImage;
        public ImageView messageSenderPicture,messageReceiverPicture;
        public TextView receiverMessageTime;
        public TextView senderMessageTime;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            senderMessageTExt=itemView.findViewById(R.id.sender_message_Text);
            receiverMessageTExt=itemView.findViewById(R.id.receiver_message_TExt);
            reciverProfileImage=itemView.findViewById(R.id.chatItem_ProfileImageviewid);
            messageReceiverPicture=itemView.findViewById(R.id.message_receiver_Imageview);
            messageSenderPicture=itemView.findViewById(R.id.message_sender_Imageview);
            receiverMessageTime=itemView.findViewById(R.id.receiver_message_Time);
            senderMessageTime=itemView.findViewById(R.id.sender_message_Time);




        }
    }
}
