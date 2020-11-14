package com.example.myapplication.Student.Adapter;

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

import com.example.myapplication.R;
import com.example.myapplication.Student.DataModuler.StudentQuizDataModuler;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StudentQuizListAdapter extends RecyclerView.Adapter<StudentQuizListAdapter.MyViewHolder> {

    private  Context context;
    private  List<StudentQuizDataModuler> quizeDataList=new ArrayList<>();
    private OnItemClickListner listner;

    public StudentQuizListAdapter(Context context, List<StudentQuizDataModuler> quizeDataList) {
        this.context = context;
        this.quizeDataList = quizeDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.student_quize_list_item_list,parent,false);
        return  new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentQuizDataModuler currentItem=quizeDataList.get(position);

            holder.quizeNameTextview.setText(currentItem.getQuizName()+"\n Total  questions: ("+currentItem.getTotalQuestion()+")");

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

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView quizeNameTextview;
        private  ImageView quizeImageview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quizeNameTextview=itemView.findViewById(R.id.student_quize_list_Textviewid);
            quizeImageview=itemView.findViewById(R.id.student_quize_list_Imageviewid);
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
