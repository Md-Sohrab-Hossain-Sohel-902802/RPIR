package com.example.myapplication.Teacher.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.StudentAdapter;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.DataModuler.PresentDataModuler;
import com.example.myapplication.Teacher.TeacherStudentPresentActivity;
import com.example.myapplication.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentListAdapter extends RecyclerView.Adapter<PresentListAdapter.MyViewHolder> {




    private  Context context;
    private PresentListAdapter.OnItemClickListner listner;


    private  List<PresentDataModuler> presentList=new ArrayList<>();
    Activity activity;
    String roll,department,classname,subjectname;

    public PresentListAdapter(Activity presentListActivity, Context context, List<PresentDataModuler> presentList) {
        this.activity=presentListActivity;
        this.context = context;
        this.presentList = presentList;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.present_list_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final PresentDataModuler selectedItem=presentList.get(position);

                holder.nameTextview.setText("Date: "+selectedItem.getDate()+"\nTime: "+selectedItem.getTime());
                holder.rollTextview.setText("Roll : "+selectedItem.getRoll());
                holder.attendenceTextview.setText(selectedItem.getPresence());


              /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePresentDiolouge(selectedItem.getId(),selectedItem.getDate(),selectedItem.getPresence());
                    }
                });*/



    }


    @Override
    public int getItemCount() {
        return presentList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView nameTextview,rollTextview,attendenceTextview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nameTextview=itemView.findViewById(R.id.presentListNameTextviewid);
            rollTextview=itemView.findViewById(R.id.presentListRollTextviewid);
            attendenceTextview=itemView.findViewById(R.id.presentlist_PresentTextviewid);

            attendenceTextview.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            attendenceTextview.setSelected(true);

            itemView.setOnCreateContextMenuListener(this);




        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("choose an action");
       MenuItem update=menu.add(Menu.NONE,1,1,"Update");
       MenuItem delete=menu.add(Menu.NONE,2,2,"Delete");
         update.setOnMenuItemClickListener(this);
         delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:{
                            listner.onUpdate(position);
                            return  true;
                        }
                        case 2:{
                            listner.onDelete(position);
                            return  true;
                        }
                    }
                }
            }
            return false;
        }






    }




    public interface  OnItemClickListner{
      void onDelete(int position);
      void onUpdate(int position);
    }


    public void setOnItemClickListner(PresentListAdapter.OnItemClickListner listner){
        this.listner=listner;
    }


}
