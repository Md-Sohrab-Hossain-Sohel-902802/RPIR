package com.example.myapplication.Teacher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.TeacherChatingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllTeacherAdapter extends RecyclerView.Adapter<AllTeacherAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<TeacherDataModuler> allTeacherDataList=new ArrayList<>();

    public AllTeacherAdapter(Context context, Activity activity, List<TeacherDataModuler> allTeacherDataList) {
        this.context = context;
        this.activity = activity;
        this.allTeacherDataList = allTeacherDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_teacher_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsersShared shared=new UsersShared(activity);
        String teacherCode=shared.getTeacherCode();
        final TeacherDataModuler currentItem=allTeacherDataList.get(position);

        if(teacherCode.equals(currentItem.getCode())){

            holder.sendMessageButton.setVisibility(View.GONE);

        }
        if(currentItem.getImage().equals("")){
            Picasso.get().load(R.drawable.avatar).placeholder(R.drawable.avatar).into(holder.profileImage);

        }else {
            String image=currentItem.getImage();
         Picasso.get().load(image).placeholder(R.drawable.avatar).into(holder.profileImage);
        }
        holder.userNameTextview.setText(currentItem.getName()+"("+currentItem.getDepartmentname()+")");

//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(AllUserActivity.this,UserProfileActivity.class);
//                intent.putExtra("from","user");
//                intent.putExtra("uid",alluserdataList.getUid());
//                startActivity(intent);
//            }
//        });



        holder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(context, TeacherChatingActivity.class);
                intent.putExtra("name", currentItem.getName() + "(" + currentItem.getDepartmentname() + ")");
                intent.putExtra("image", currentItem.getImage());
                intent.putExtra("uid", currentItem.getId());
                context.startActivity(intent);




            }
        });


    }

    @Override
    public int getItemCount() {
        return allTeacherDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{


        ImageView profileImage;
        TextView userNameTextview;
        Button sendMessageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage=itemView.findViewById(R.id.allUser_profileImageviewid);
            userNameTextview=itemView.findViewById(R.id.allUser_NameTextviewid);
            sendMessageButton=itemView.findViewById(R.id.allUser_sendMessageButtonid);





        }
    }
}
