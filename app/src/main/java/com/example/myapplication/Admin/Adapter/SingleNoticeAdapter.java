package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.DataModuler.NoticeDataModuler;
import com.example.myapplication.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SingleNoticeAdapter extends RecyclerView.Adapter<SingleNoticeAdapter.MyViewHolder> {


    private  Context context;
    private List<String> imageList=new ArrayList<>();
    private  OnItemClickListner listner;

    public SingleNoticeAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_notice_image_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Picasso.get().load(imageList.get(position)).into(holder.zoomageView);

   }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    ZoomageView zoomageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            zoomageView=itemView.findViewById(R.id.singlenoticeItem_Imageviewid);
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
            delete.setOnMenuItemClickListener(this);

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

                    }
                }
            }
            return false;
        }
    }

    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onDelete(int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }






}
