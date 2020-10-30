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

import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {


    private  Context context;
    private  List<StudentDataModuler> dataModulList=new ArrayList<>();
    private  OnItemClickListner listner;

    public StudentAdapter(Context context, List<StudentDataModuler> dataModulList) {
        this.context = context;
        this.dataModulList = dataModulList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.student_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentDataModuler currentItem=dataModulList.get(position);

        holder.nametext.setText("Name : "+currentItem.getName());
        holder.rolltext.setText("Roll:  "+currentItem.getRoll());
    }

    @Override
    public int getItemCount() {
        return dataModulList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView nametext,rolltext;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nametext=itemView.findViewById(R.id.studentItem_studentNameTextviewid);
            rolltext=itemView.findViewById(R.id.studentItem_studentRollTextviewid);

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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete This Student");
            MenuItem update=menu.add(Menu.NONE,2,2,"Update This Student");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);

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
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }






}
