package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> {


    private  Context context;
    private  List<TeacherDataModuler> dataModulList=new ArrayList<>();
    private  OnItemClickListner listner;

    public TeacherAdapter(Context context, List<TeacherDataModuler> dataModulList) {
        this.context = context;
        this.dataModulList = dataModulList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.teacher_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TeacherDataModuler currentItem=dataModulList.get(position);
        if(currentItem.getImage().equals("")){
         //   Picasso.get().load(R.drawable.avatar).into(holder.imageView);
          Picasso.get().load(R.drawable.avatar).into(holder.imageView);
        } else{
         //   Picasso.get().load(R.drawable.avatar).into(holder.imageView);
          Picasso.get().load(currentItem.getImage()).placeholder(R.drawable.avatar).into(holder.imageView);
        }



            holder.nametext.setText("Name : "+currentItem.getName());
            holder.codetext.setText("Teacher Code: "+currentItem.getCode()+"\nDepartment:"+currentItem.getDepartmentname());
    }

    @Override
    public int getItemCount() {
        return dataModulList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView nametext,codetext;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nametext=itemView.findViewById(R.id.teacheritem_TeacherNameTextviewid);
            codetext=itemView.findViewById(R.id.teacheritem_TeacherCodeTextviewid);
            imageView=itemView.findViewById(R.id.teacheritem_TEacherImageviewid);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onClick(View v) {
                if(listner!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                            listner.onItemClick(position);
                    }
                }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("choose an action");
                    MenuItem delete=menu.add(Menu.NONE,1,1,"Delete This Teacher");
                    MenuItem update=menu.add(Menu.NONE,2,2,"Update This Teacher");
                    MenuItem image=menu.add(Menu.NONE,3,3,"Update Image");
                    delete.setOnMenuItemClickListener(this);
                    update.setOnMenuItemClickListener(this);
                    image.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                        switch (item.getItemId()){
                            case 1:{
                                listner.onDelete(position);
                                return  true;
                            }
                            case 2:{
                                listner.onUpdate(position);
                                return  true;
                            }    case 3:{
                                listner.onImageUpdate(position);
                                return  true;
                            }
                        }
                }
            }
            return false;
        }
    }

    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onDelete(int position);
        void onUpdate(int position);
        void onImageUpdate(int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
            this.listner=listner;
    }






}
