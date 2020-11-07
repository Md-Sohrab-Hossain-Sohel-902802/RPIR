package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Admin.Adapter.SingleNoticeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SingleNoticeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    String id;
    private DatabaseReference databaseReference;
    private List<String> imageList=new ArrayList<>();
    private SingleNoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_notice);
        id=getIntent().getStringExtra("key");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notice").child(id);
        recyclerView=findViewById(R.id.singlenoticeListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new SingleNoticeAdapter(this,imageList);
        recyclerView.setAdapter(adapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            String length=snapshot.child("length").getValue().toString();
                            int l=Integer.parseInt(length);
                            imageList.clear();
                            for (int i=0; i<l; i++){
                                String path="i"+(i+1);
                                String image=snapshot.child(path).getValue().toString();
                                imageList.add(image);
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