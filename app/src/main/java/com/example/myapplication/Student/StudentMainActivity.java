package com.example.myapplication.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.StartActivity;
import com.example.myapplication.Student.Adapter.StudentMainAdapter;
import com.example.myapplication.Teacher.TeacherMainActivity;

public class StudentMainActivity extends AppCompatActivity {

    private GridView gridView;
    private StudentMainAdapter adapter;

    private  String[] values={"Routine","Cgpa Calculator","Probidhan","Academic Calender","Study","Institute","Book List","Notice","Board"};
    private  int[] images={
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile
    };

    private  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        toolbar=findViewById(R.id.student_main_ToolbarId);
        setSupportActionBar(toolbar);
        this.setTitle("");


        gridView=findViewById(R.id.studentMainGridviewid);
        adapter=new StudentMainAdapter(this,values,images);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        startActivity(new Intent(StudentMainActivity.this,StudentRoutineActivity.class));
                    }   if(position==1){
                        startActivity(new Intent(StudentMainActivity.this,StudentCgpaCalculatorActivity.class));
                    }
            }
        });





    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.main_logoutmenubuttonid){
            UsersShared shared=new UsersShared(StudentMainActivity.this) ;
            shared.saveUserdata("","","","","","","","","","","");
            startActivity(new Intent(StudentMainActivity.this, StartActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}