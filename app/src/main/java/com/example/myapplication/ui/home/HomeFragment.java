package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.Admin.AdminNoticeListActivity;
import com.example.myapplication.Admin.PublishNoticeActivity;
import com.example.myapplication.Admin.StudentCategoryActivity;
import com.example.myapplication.Admin.SubjectListActivity;
import com.example.myapplication.R;

public class HomeFragment extends Fragment {

    private CardView addnewClass,addnewTeacher,addnewStudent,publishNotice,addSubject;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addnewClass=view.findViewById(R.id.main_AddNewClass);
        addnewTeacher=view.findViewById(R.id.mainAddnewTeacher);
        addnewStudent=view.findViewById(R.id.mainAddStudent);
        publishNotice=view.findViewById(R.id.mainPublishNotice);
        addSubject=view.findViewById(R.id.mainAddSubject);


            publishNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AdminNoticeListActivity.class));

                }
            });
        addSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), SubjectListActivity.class));

                }
            });



        addnewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddTeacherActivity.class));
            }
        });

        addnewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getContext(), StudentCategoryActivity.class);

                intent.putExtra("from","student");
                startActivity(intent);

            }
        });
     addnewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getContext(), StudentCategoryActivity.class);

                intent.putExtra("from","routine");
                startActivity(intent);

            }
        });






        return view;
    }
}