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

import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.PresentController;
import com.example.myapplication.Teacher.TStudentPresentListActivity;
import com.example.myapplication.Teacher.TeacherChatingActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<StudentDataModuler> studentDataList=new ArrayList<>();

    String department,group,shift,semester,subjectCode;


    public StudentListAdapter(Context context,Activity activity, String subjectCode, String department, String group, String shift, String semester, List<StudentDataModuler> studentDataList) {
        this.context = context;
        this.activity=activity;
        this.subjectCode=subjectCode;
        this.department=department;
        this.group=group;
        this.shift=shift;
        this.semester=semester;
       this.studentDataList = studentDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.studentlist_sample_layoute,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final StudentDataModuler ct=studentDataList.get(position);


        holder.nameTextview.setText(studentDataList.get(position).getName());
        holder.rollTextview.setText(studentDataList.get(position).getRoll());


        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String day = sdf.format(d);
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                UsersShared shared=new UsersShared(activity);
               String teacherCode= shared.getTeacherCode();


                PresentController presentController=new PresentController(context,teacherCode,"Present",day,currentDate,currentTime,department,group,shift,semester,ct.getName(),ct.getRoll(),ct.getRegistration(),"",subjectCode);
                presentController.presentSubmit();

            }
        });
      holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String day = sdf.format(d);
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                UsersShared shared=new UsersShared(activity);
                String teacherCode= shared.getTeacherCode();



                PresentController presentController=new PresentController(context,teacherCode,"Absent",day,currentDate,currentTime,department,group,shift,semester,ct.getName(),ct.getRoll(),ct.getRegistration(),"",subjectCode);
                presentController.presentSubmit();

            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TStudentPresentListActivity.class);


                intent.putExtra("department",ct.getDepartment());
                intent.putExtra("name",ct.getName());
                intent.putExtra("roll",ct.getRoll());
                intent.putExtra("registration",ct.getRegistration());
                intent.putExtra("semester",ct.getSemester());
                intent.putExtra("group",ct.getGroups());
                intent.putExtra("shift",ct.getShifts());
                intent.putExtra("subjectCode",subjectCode);



                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView nameTextview,rollTextview;
        Button presentButton,absentButton;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextview=itemView.findViewById(R.id.studentList_nameTextviewid);
            rollTextview=itemView.findViewById(R.id.studentList_RollTextviewid);
            presentButton=itemView.findViewById(R.id.studentList_PresentButtonid);
            absentButton=itemView.findViewById(R.id.studentList_AbsentButtonid);





        }
    }
}
