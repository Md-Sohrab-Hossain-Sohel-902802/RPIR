package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.Admin.Adapter.NoticeAdapter;
import com.example.myapplication.Admin.DataModuler.NoticeDataModuler;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminNoticeListActivity extends AppCompatActivity {

    private List<NoticeDataModuler> noticedataList=new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private  DatabaseReference databaseReference;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice_list);

        toolbar=findViewById(R.id.noticeAppbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Notice List");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notice");
        recyclerView=findViewById(R.id.admin_noticeListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new NoticeAdapter(this,noticedataList);
        recyclerView.setAdapter(adapter);




    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        noticedataList.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                                NoticeDataModuler dataModuler=snapshot1.getValue(NoticeDataModuler.class);
                                noticedataList.add(dataModuler);
                                adapter.notifyDataSetChanged();
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addMenuButtonid){
            startActivity(new Intent(AdminNoticeListActivity.this, PublishNoticeActivity.class));

        }




        return super.onOptionsItemSelected(item);
    }

}