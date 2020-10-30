package com.example.myapplication.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Teacher.Adapter.TeacherRoutineAdapter;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.TeacherDailyRoutineActivity;
import com.example.myapplication.Teacher.TeacherStudentPresentActivity;

import java.util.ArrayList;
import java.util.List;

public class TeacherRoutineAdapter extends RecyclerView.Adapter<TeacherRoutineAdapter.MyViewHolder> {


    private Context context;
    private List<RoutineDataModuler> dataModulList = new ArrayList<>();
      OnItemClickListner listner;
    public TeacherRoutineAdapter(Context context, List<RoutineDataModuler> dataModulList) {
        this.context = context;
        this.dataModulList = dataModulList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_routine_list_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final RoutineDataModuler currentItem = dataModulList.get(position);

        holder.shrtClassname.setText(makeSemesterShort(currentItem.getSemester())+makeDepartmentShort(currentItem.getDepartment())+currentItem.getGroups()+makeShiftShort(currentItem.getShift()));
        holder.anotherTextview.setText(
                "Period: "+currentItem.getPeriod()+"\n"+
                        "Class-Time: "+currentItem.getStartTime()+"-"+currentItem.getEndTime()+ "\n"+
                        "Room Number: "+currentItem.getRoomNumber()+"\n"+
                        "Subject Code: "+currentItem.getSubjectCode()
        );


        holder.takePresentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, TeacherStudentPresentActivity.class);
                intent.putExtra("department",currentItem.getDepartment());
                intent.putExtra("groups",currentItem.getGroups());
                intent.putExtra("shift",currentItem.getShift());
                intent.putExtra("semester",currentItem.getSemester());
                intent.putExtra("subjectCode",currentItem.getSubjectCode());
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return dataModulList.size();
    }



    public String makeDepartmentShort(String department){
        String shrt = null;
        if(department.equals("Computer")){
            shrt="Cmt";
        } else if(department.equals("Electrical")){
            shrt="Et";
        }else if(department.equals("Mechanical")){
            shrt="Mt";
        }
        else if(department.equals("Civil")){
            shrt="Ct";
        } else if(department.equals("Electronics")){
            shrt="Ect";
        }else if(department.equals("Power")){
            shrt="Pt";
        }else if(department.equals("Electromedical")){
            shrt="Emt";
        }
        return  shrt;
    }
    public String makeShiftShort(String shift){
        String shrt=null;
        if(shift.equals("First")){
            shrt="1";
        }if(shift.equals("Second")){
            shrt="2";
        }
        return  shrt;
    }
    public String makeSemesterShort(String semester){
        String shrt=null;

        if(semester.equals("Semester-1")){
            shrt="1";
        }else  if(semester.equals("Semester-2")){
            shrt="2";
        }else  if(semester.equals("Semester-3")){
            shrt="3";
        }else  if(semester.equals("Semester-4")){
            shrt="4";
        }else  if(semester.equals("Semester-5")){
            shrt="5";
        }else  if(semester.equals("Semester-6")){
            shrt="6";
        }
        else  if(semester.equals("Semester-7")){
            shrt="7";
        } else  if(semester.equals("Semester-8")){
            shrt="8";
        }




        return  shrt;
    }








    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shrtClassname, anotherTextview;
        Button takePresentButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            shrtClassname = itemView.findViewById(R.id.t_routine_item_ShrtClassnameTextviewid);
            anotherTextview = itemView.findViewById(R.id.t_routine_otherAllTextviewid);
            takePresentButton = itemView.findViewById(R.id.t_routine_takePresentButtonid);
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


    public void setOnItemClickListner(TeacherRoutineAdapter.OnItemClickListner listner){
        this.listner=listner;
    }






}









