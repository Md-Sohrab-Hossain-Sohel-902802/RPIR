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
import com.example.myapplication.Admin.DataModuler.SubjectDataModuler;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {


    private  Context context;
    private  List<SubjectDataModuler> dataModulList=new ArrayList<>();
    private  OnItemClickListner listner;

    public SubjectAdapter(Context context, List<SubjectDataModuler> dataModulList) {
        this.context = context;
        this.dataModulList = dataModulList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.subjectlist_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectDataModuler currentItem=dataModulList.get(position);

        holder.subjectnametext.setText(currentItem.getName());
        holder.subjectcodetext.setText(currentItem.getCode());
    }

    @Override
    public int getItemCount() {
        return dataModulList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {


        private  TextView subjectnametext,subjectcodetext;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectnametext=itemView.findViewById(R.id.subjectList_SubjectNameTextviewid);
            subjectcodetext=itemView.findViewById(R.id.subjectList_CodeTextviewid);

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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete");
            MenuItem update=menu.add(Menu.NONE,2,2,"Update");
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
