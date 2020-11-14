package com.example.myapplication.Teacher.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.Adapter.StudentAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private  Context context;
    private  List<QuizeDataModuler> quizeDataList=new ArrayList<>();
    private OnItemClickListner listner;

    public QuizListAdapter(Context context, List<QuizeDataModuler> quizeDataList) {
        this.context = context;
        this.quizeDataList = quizeDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.quize_list_item_list,parent,false);
        return  new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            QuizeDataModuler currentItem=quizeDataList.get(position);

            holder.quizeNameTextview.setText(currentItem.getQuizName()+"\n Total  questions: ("+currentItem.getQnumber()+")");

            if(currentItem.getImage().equals("null")){
                Picasso.get().load(R.drawable.cg1).placeholder(R.drawable.cg1).into(holder.quizeImageview);

            }else{
                Picasso.get().load(currentItem.getImage()).placeholder(R.drawable.cg1).into(holder.quizeImageview);
            }



    }

    @Override
    public int getItemCount() {
        return  quizeDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView quizeNameTextview;
        private  ImageView quizeImageview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quizeNameTextview=itemView.findViewById(R.id.quize_list_Textviewid);
            quizeImageview=itemView.findViewById(R.id.quize_list_Imageviewid);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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
                            listner.onImageSet(position);
                            return  true;
                        }
                        case 3:{
                            listner.onUpdate(position);
                            return  true;
                        } case 4:{
                            listner.onTakeExam(position);
                            return  true;
                        }
                    }
                }
            }
            return false;
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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete This Quiz");
            MenuItem update=menu.add(Menu.NONE,3,3,"Update This Quiz");
            MenuItem takeExam=menu.add(Menu.NONE,4,4,"Take An exam for this quiz");
            MenuItem setimage=menu.add(Menu.NONE,2,2,"Set An Image");
            delete.setOnMenuItemClickListener(this);
            setimage.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);
            takeExam.setOnMenuItemClickListener(this);

        }
    }

    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onDelete(int position);
        void onUpdate(int position);
        void onImageSet(int position);
        void onTakeExam(int position);
    }
    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }





}
