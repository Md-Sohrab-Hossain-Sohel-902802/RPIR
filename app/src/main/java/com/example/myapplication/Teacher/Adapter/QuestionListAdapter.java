package com.example.myapplication.Teacher.Adapter;

import android.content.Context;
import android.text.Html;
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
import com.example.myapplication.Teacher.DataModuler.QuestionListDataModuler;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder> {

    private  Context context;
    private  List<QuestionListDataModuler> questionDataList=new ArrayList<>();
    private OnItemClickListner listner;

    public QuestionListAdapter(Context context, List<QuestionListDataModuler> questionDataList) {
        this.context = context;
        this.questionDataList = questionDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.question_list_item_layout,parent,false);
        return  new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuestionListDataModuler currentItem=questionDataList.get(position);

        String number="Question: "+(position+1)+"\n";
        String question="Q: "+currentItem.getQuestion()+"\n" ;
        String option=" 1. "+currentItem.getOption1()+"\n 2. "+currentItem.getOption2()+"\n 3. "+currentItem.getOption3()+"\n 4. "+currentItem.getOption4()+"\n";
        String answer="Ans:  Option "+currentItem.getAnswerNumber()+" is Correct";
        String finaltext=number+question+option+answer;
         holder.textView.setText(finaltext);


    }

    @Override
    public int getItemCount() {
        return  questionDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView textView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.question_list_QuestionTextviewid);
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
                            listner.onUpdate(position);
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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete This Question");
            MenuItem update=menu.add(Menu.NONE,2,2,"Update");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);

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
