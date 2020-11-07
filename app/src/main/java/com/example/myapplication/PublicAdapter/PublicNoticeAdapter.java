package com.example.myapplication.PublicAdapter;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PublicNoticeAdapter extends RecyclerView.Adapter<PublicNoticeAdapter.MyViewHolder> {


    private  Context context;
    private  List<NoticeDataModuler> dataModulList=new ArrayList<>();
    private  OnItemClickListner listner;

    public PublicNoticeAdapter(Context context, List<NoticeDataModuler> dataModulList) {
        this.context = context;
        this.dataModulList = dataModulList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notice_list_item_layoute,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NoticeDataModuler currentItem=dataModulList.get(position);

        holder.headingtextview.setText(currentItem.getTitle());
        holder.dateTimeTextview.setText(currentItem.getDate()+"  --"+currentItem.getDay()+" At "+currentItem.getTime());

        holder.imageView1.setVisibility(View.GONE);
        holder.imageView2.setVisibility(View.GONE);
        holder.imageView3.setVisibility(View.GONE);
        holder.imageView4.setVisibility(View.GONE);
        holder.showmoretextview.setVisibility(View.GONE);

        if(currentItem.getLength().equals("1")){
                holder.linearLayout.setVisibility(View.GONE);
                holder.imageView2.setVisibility(View.GONE);
             Picasso.get().load(currentItem.getI1()).placeholder(R.drawable.avatar).into(holder.imageView1);

        }else if(currentItem.getLength().equals("2")){
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView3.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getI1()).placeholder(R.drawable.avatar).into(holder.imageView1);
            Picasso.get().load(currentItem.getI2()).placeholder(R.drawable.avatar).into(holder.imageView3);

        }else if(currentItem.getLength().equals("3")){
            holder.imageView1.setVisibility(View.VISIBLE);
             holder.imageView3.setVisibility(View.VISIBLE);
            holder.imageView4.setVisibility(View.VISIBLE);

            Picasso.get().load(currentItem.getI1()).placeholder(R.drawable.avatar).into(holder.imageView1);
             Picasso.get().load(currentItem.getI2()).placeholder(R.drawable.avatar).into(holder.imageView3);
            Picasso.get().load(currentItem.getI3()).placeholder(R.drawable.avatar).into(holder.imageView4);
        }

        else if(currentItem.getLength().equals("4")){
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView2.setVisibility(View.VISIBLE);
            holder.imageView3.setVisibility(View.VISIBLE);
            holder.imageView4.setVisibility(View.VISIBLE);

            Picasso.get().load(currentItem.getI1()).placeholder(R.drawable.avatar).into(holder.imageView1);
            Picasso.get().load(currentItem.getI2()).placeholder(R.drawable.avatar).into(holder.imageView2);
            Picasso.get().load(currentItem.getI3()).placeholder(R.drawable.avatar).into(holder.imageView3);
            Picasso.get().load(currentItem.getI4()).placeholder(R.drawable.avatar).into(holder.imageView4);



        }else{
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView2.setVisibility(View.VISIBLE);
            holder.imageView3.setVisibility(View.VISIBLE);
            holder.imageView4.setVisibility(View.VISIBLE);
            holder.showmoretextview.setVisibility(View.VISIBLE);

            Picasso.get().load(currentItem.getI1()).placeholder(R.drawable.avatar).into(holder.imageView1);
            Picasso.get().load(currentItem.getI2()).placeholder(R.drawable.avatar).into(holder.imageView2);
            Picasso.get().load(currentItem.getI3()).placeholder(R.drawable.avatar).into(holder.imageView3);
            Picasso.get().load(currentItem.getI4()).placeholder(R.drawable.avatar).into(holder.imageView4);
           int length=Integer.parseInt(currentItem.getLength());

            holder.showmoretextview.setText((length-4)+" +more items");




        }




   }

    @Override
    public int getItemCount() {
        return dataModulList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
       private  ImageView imageView1,imageView2,imageView3,imageView4;
       private  TextView headingtextview,showmoretextview,dateTimeTextview;
       private LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView1=itemView.findViewById(R.id.notice_layoutImage1);
            imageView2=itemView.findViewById(R.id.notice_layoutImage2);
            imageView3=itemView.findViewById(R.id.notice_layoutImage3);
            imageView4=itemView.findViewById(R.id.notice_layoutImage4);
            linearLayout=itemView.findViewById(R.id.notice_secondLinear);

            headingtextview=itemView.findViewById(R.id.notice_header_Textviewid);
            showmoretextview=itemView.findViewById(R.id.noticeItem_showmoreTextviewid);
            dateTimeTextview=itemView.findViewById(R.id.notice_dateTime_Textviewid);

            itemView.setOnClickListener(this);

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


    }

    public interface  OnItemClickListner{
        void onItemClick(int position);

    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }






}
