package com.example.myapplication.Public;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Admin.Adapter.NoticeAdapter;
import com.example.myapplication.Admin.AdminNoticeListActivity;
import com.example.myapplication.Admin.DataModuler.NoticeDataModuler;
import com.example.myapplication.PublicAdapter.PublicNoticeAdapter;
import com.example.myapplication.R;
import com.example.myapplication.SingleNoticeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PublicNoticeListActivity extends AppCompatActivity {
    private List<NoticeDataModuler> noticedataList=new ArrayList<>();
    private RecyclerView recyclerView;
    private PublicNoticeAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_notice_list);



        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notice");
        recyclerView=findViewById(R.id.public_noticeListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new PublicNoticeAdapter(this,noticedataList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListner(new PublicNoticeAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                String key=noticedataList.get(position).getKey();

                Intent intent=new Intent(PublicNoticeListActivity.this, SingleNoticeActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });


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

}