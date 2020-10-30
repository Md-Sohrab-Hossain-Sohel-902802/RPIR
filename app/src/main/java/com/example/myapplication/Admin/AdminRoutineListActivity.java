package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.RoutineManagement;
import com.example.myapplication.Admin.Adapter.SpinnerArrayListAdapter;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.Admin.DataModuler.TeachercodeDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminRoutineListActivity extends AppCompatActivity  implements View.OnClickListener {
    private TextView period,p1,p2,p3,p4,p5,p6,p7;
    private  TextView timedate,p1TimeDate,p2TimeDate,p3TimeDate,p4TimeDate,p5TimeDate,p6TimeDate,p7TimeDate;
    private  TextView saturday,saturdayP1,saturdayP2,saturdayP3,saturdayP4,saturdayP5,saturdayP6,saturdayP7;
    private  TextView sunday,sundayP1,sundayP2,sundayP3,sundayP4,sundayP5,sundayP6,sundayP7;
    private  TextView monday,mondayP1,mondayP2,mondayP3,mondayP4,mondayP5,mondayP6,mondayP7;
    private  TextView tuesday,tuesdayP1,tuesdayP2,tuesdayP3,tuesdayP4,tuesdayP5,tuesdayP6,tuesdayP7;
    private  TextView wednesday,wednesdayP1,wednesdayP2,wednesdayP3,wednesdayP4,wednesdayP5,wednesdayP6,wednesdayP7;
    private  TextView thursday,thursdayP1,thursdayP2,thursdayP3,thursdayP4,thursdayP5,thursdayP6,thursdayP7;
    private  TextView header,addRoutineHeaderTextview;

    String totalShortform;





    //OthersVAriable
    private Spinner groupSpinner,shiftSpinner;
    private TextView diolougeHeader;
    private Button addButton;
    String[] groups={"A","B"};
    String[] shifts={"First","Second"};
    private SpinnerCustomAdapter groupsAdapter,shiftsAdapter;

    private String department,semester,shift,group,getgroup,getshift,teacherCode;
  private   List<TeachercodeDataModuler> allTeacherCodeList=new ArrayList<>();
  private   List<RoutineDataModuler> routineDataList=new ArrayList<>();



    private SpinnerArrayListAdapter teacherCodeAdapter;




    //routineDiolouge


    private  EditText subjectCodeEdittext,rteacherNameEdittext,roomNumberEdittext;
    private Spinner teacherCodeSpinner;
    private Button routineAddDioloubeButton;
   private boolean isfirstSelected=true;
    private Toolbar toolbar;

    private  ProgressDialog progressDialog1,progressDialog2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_routine_list);

    progressDialog1=new ProgressDialog(this);
    progressDialog2=new ProgressDialog(this);
    progressDialog1.setTitle("Loading..");
    progressDialog2.setTitle("Saving Data..");
    progressDialog1.setCanceledOnTouchOutside(false);
    progressDialog2.setCanceledOnTouchOutside(false);




        initialize();
          getAllTeacherCode();

        department=getIntent().getStringExtra("department");
        semester=getIntent().getStringExtra("semester");
        groupsAdapter=new SpinnerCustomAdapter(this,groups);
        shiftsAdapter=new SpinnerCustomAdapter(this,shifts);
        selectDiolouge(department,semester);
        teacherCodeAdapter=new SpinnerArrayListAdapter(this,allTeacherCodeList);













        saturdayP1.setOnClickListener(this);
        saturdayP2.setOnClickListener(this);
        saturdayP3.setOnClickListener(this);
        saturdayP4.setOnClickListener(this);
        saturdayP5.setOnClickListener(this);
        saturdayP6.setOnClickListener(this);
        saturdayP7.setOnClickListener(this);


        //sunday

        sundayP1.setOnClickListener(this);
        sundayP2.setOnClickListener(this);
        sundayP3.setOnClickListener(this);
        sundayP4.setOnClickListener(this);
        sundayP5.setOnClickListener(this);
        sundayP6.setOnClickListener(this);
        sundayP7.setOnClickListener(this);

        //monday



        mondayP1.setOnClickListener(this);
        mondayP2.setOnClickListener(this);
        mondayP3.setOnClickListener(this);
        mondayP4.setOnClickListener(this);
        mondayP5.setOnClickListener(this);
        mondayP6.setOnClickListener(this);
        mondayP7.setOnClickListener(this);


        //tuesday


        tuesdayP1.setOnClickListener(this);
        tuesdayP2.setOnClickListener(this);
        tuesdayP3.setOnClickListener(this);
        tuesdayP4.setOnClickListener(this);
        tuesdayP5.setOnClickListener(this);
        tuesdayP6.setOnClickListener(this);
        tuesdayP7.setOnClickListener(this);
    //wednesday
        wednesdayP1.setOnClickListener(this);
        wednesdayP2.setOnClickListener(this);
        wednesdayP3.setOnClickListener(this);
        wednesdayP4.setOnClickListener(this);
        wednesdayP5.setOnClickListener(this);
        wednesdayP6.setOnClickListener(this);
        wednesdayP7.setOnClickListener(this);
        //thursday
        thursdayP1.setOnClickListener(this);
        thursdayP2.setOnClickListener(this);
        thursdayP3.setOnClickListener(this);
        thursdayP4.setOnClickListener(this);
        thursdayP5.setOnClickListener(this);
        thursdayP6.setOnClickListener(this);
        thursdayP7.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.routine_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.routine_reset_MenuButtonid){
            selectDiolouge(department,semester);
        }


        return super.onOptionsItemSelected(item);
    }


    public void setPEriodTime(){
        if(getShift().equals("First")){
            p1TimeDate.setText("8.00\n8.45");
            p2TimeDate.setText("8.45\n9.30");
            p3TimeDate.setText("9.30\n10.15");
            p4TimeDate.setText("10.15\n11.00");
            p5TimeDate.setText("11.00\n11.45");
            p6TimeDate.setText("11.45\n12.30");
            p7TimeDate.setText("12.30\n1.15");

        }else{
            p1TimeDate.setText("1.30\n2.15");
            p2TimeDate.setText("2.15\n3.00");
            p3TimeDate.setText("3.00\n3.45");
            p4TimeDate.setText("3.45\n4.30");
            p5TimeDate.setText("4.30\n5.15");
            p6TimeDate.setText("5.15\n6.00");
            p7TimeDate.setText("6.00\n6.45");
        }

    }

    protected void onGetData() {

        if(getgroup!="" && getshift!="" ){


            RoutineManagement routineManagement=new RoutineManagement(
                    AdminRoutineListActivity.this,
                    routineDataList,
                    saturday,
                    saturdayP1,
                    saturdayP2,
                    saturdayP3,
                    saturdayP4,
                    saturdayP5,
                    saturdayP6,
                    saturdayP7,
                    sunday,
                    sundayP1,
                    sundayP2,
                    sundayP3,
                    sundayP4,
                    sundayP5,
                    sundayP6,
                    sundayP7,
                    monday,
                    mondayP1,
                    mondayP2,
                    mondayP3,
                    mondayP4,
                    mondayP5,
                    mondayP6,
                    mondayP7,
                    tuesday,
                    tuesdayP1,
                    tuesdayP2,
                    tuesdayP3,tuesdayP4,
                    tuesdayP5,
                    tuesdayP6,
                    tuesdayP7,
                    wednesday,
                    wednesdayP1,
                    wednesdayP2,
                    wednesdayP3,
                    wednesdayP4,
                    wednesdayP5,
                    wednesdayP6,
                    wednesdayP7,
                    thursday,
                    thursdayP1,
                    thursdayP2,
                    thursdayP3,
                    thursdayP4,
                    thursdayP5,
                    thursdayP6,
                    thursdayP7
            );
          routineManagement.setRoutineText();


        }




    }



    public void getClassRoutine(String department,String group,String shift,String semester){
        progressDialog1.show();
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminRoutineListActivity.this);
        String url=urls.getRoutine()+department+"/"+group+"/"+shift+"/"+semester;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog1.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    routineDataList.clear();
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);

                        RoutineDataModuler dataModuler=new RoutineDataModuler(
                                receive.getString("_id"),
                                receive.getString("startTime"),
                                receive.getString("endTime"),
                                receive.getString("day"),
                                receive.getString("teacherName"),
                                receive.getString("roomNumber"),
                                receive.getString("teacherCode"),
                                receive.getString("department"),
                                receive.getString("groups"),
                                receive.getString("shift"),
                                receive.getString("semester"),
                                receive.getString("period"),
                                receive.getString("subjectCode"),
                                receive.getString("serial")
                        );
                        routineDataList.add(dataModuler);
                    }

                    onGetData();

                } catch (JSONException e) {
                    progressDialog1.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(AdminRoutineListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);




    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.r_saturday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Saturday","1","11");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Saturday","1","11");
            }

        } else if(v.getId()==R.id.r_saturday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Saturday","2","12");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Saturday","2","12");
            }

        }
    else if(v.getId()==R.id.r_saturday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Saturday","3","13");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Saturday","3","13");
            }

        }    else if(v.getId()==R.id.r_saturday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Saturday","4","14");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Saturday","4","14");
            }

        } else if(v.getId()==R.id.r_saturday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Saturday","5","15");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Saturday","5","15");
            }

        } else if(v.getId()==R.id.r_saturday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Saturday","6","16");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Saturday","6","16");
            }

        }else if(v.getId()==R.id.r_saturday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Saturday","7","17");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Saturday","7","17");
            }
        }

    //Sunday

        if(v.getId()==R.id.r_sunday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Sunday","1","21");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Sunday","1","21");
            }

        } else if(v.getId()==R.id.r_sunday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Sunday","2","22");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Sunday","2","22");
            }

        }
        else if(v.getId()==R.id.r_sunday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Sunday","3","23");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Sunday","3","23");
            }

        }    else if(v.getId()==R.id.r_sunday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Sunday","4","24");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Sunday","4","24");
            }

        } else if(v.getId()==R.id.r_sunday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Sunday","5","25");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Sunday","5","25");
            }

        } else if(v.getId()==R.id.r_sunday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Sunday","6","26");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Sunday","6","26");
            }

        }else if(v.getId()==R.id.r_sunday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Sunday","7","27");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Sunday","7","27");
            }
        }


        //Monday


        if(v.getId()==R.id.r_monday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Monday","1","31");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Monday","1","31");
            }

        } else if(v.getId()==R.id.r_monday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Monday","2","32");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Monday","2","32");
            }

        }
        else if(v.getId()==R.id.r_monday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Monday","3","33");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Monday","3","33");
            }

        }    else if(v.getId()==R.id.r_monday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Monday","4","34");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Monday","4","34");
            }

        } else if(v.getId()==R.id.r_monday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Monday","5","35");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Monday","5","35");
            }

        } else if(v.getId()==R.id.r_monday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Monday","6","36");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Monday","6","36");
            }

        }else if(v.getId()==R.id.r_monday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Monday","7","37");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Monday","7","37");
            }
        }
        //Tuesday


        if(v.getId()==R.id.r_tuesday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Tuesday","1","41");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Tuesday","1","41");
            }

        } else if(v.getId()==R.id.r_tuesday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Tuesday","2","42");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Tuesday","2","42");
            }

        }
        else if(v.getId()==R.id.r_tuesday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Tuesday","3","43");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Tuesday","3","43");
            }

        }    else if(v.getId()==R.id.r_tuesday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Tuesday","4","44");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Tuesday","4","44");
            }

        } else if(v.getId()==R.id.r_tuesday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Tuesday","5","45");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Tuesday","5","45");
            }

        } else if(v.getId()==R.id.r_tuesday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Tuesday","6","46");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Tuesday","6","46");
            }

        }else if(v.getId()==R.id.r_tuesday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Tuesday","7","47");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Tuesday","7","47");
            }
        }

        //Wednesday


        if(v.getId()==R.id.r_wednesday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Wednesday","1","51");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Wednesday","1","51");
            }

        } else if(v.getId()==R.id.r_wednesday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Wednesday","2","52");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Wednesday","2","52");
            }

        }
        else if(v.getId()==R.id.r_wednesday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Wednesday","3","53");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Wednesday","3","53");
            }

        }    else if(v.getId()==R.id.r_wednesday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Wednesday","4","54");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Wednesday","4","54");
            }

        } else if(v.getId()==R.id.r_wednesday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Wednesday","5","55");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Wednesday","5","55");
            }

        } else if(v.getId()==R.id.r_wednesday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Wednesday","6","56");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Wednesday","6","56");
            }

        }else if(v.getId()==R.id.r_wednesday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Wednesday","7","57");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Wednesday","7","57");
            }
        }


        //Thursday


        if(v.getId()==R.id.r_thursday_p1data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.00","8.45","Thursday","1","61");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("1.30","2.15","Thursday","1","61");
            }

        } else if(v.getId()==R.id.r_thursday_p2data){
            if(getshift.equals("First")){
                addRoutineDiolouge("8.45","9.30","Thursday","2","62");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("2.15","3.00","Thursday","2","62");
            }

        }
        else if(v.getId()==R.id.r_thursday_p3data){
            if(getshift.equals("First")){
                addRoutineDiolouge("9.30","10.15","Thursday","3","63");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.00","3.45","Thursday","3","63");
            }

        }    else if(v.getId()==R.id.r_thursday_p4data){
            if(getshift.equals("First")){
                addRoutineDiolouge("10.15","11","Thursday","4","64");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("3.45","4.30","Thursday","4","64");
            }

        } else if(v.getId()==R.id.r_thursday_p5data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11","11.45","Thursday","5","65");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("4.30","5.15","Thursday","5","65");
            }

        } else if(v.getId()==R.id.r_thursday_p6data){
            if(getshift.equals("First")){
                addRoutineDiolouge("11.45","12.30","Thursday","6","66");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("5.15","6.00","Thursday","6","66");
            }

        }else if(v.getId()==R.id.r_thursday_p7data){
            if(getshift.equals("First")){
                addRoutineDiolouge("12.30","1.15","Thursday","7","67");
            }else if(getshift.equals("Second")){
                addRoutineDiolouge("6.00","6.45","Thursday","7","67");
            }
        }




    }


    public void getAllTeacherCode(){
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminRoutineListActivity.this);
        String url=urls.getTeacherCode();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    allTeacherCodeList.clear();
                    TeachercodeDataModuler dataModuler1=new TeachercodeDataModuler(
                            "Select One",
                            ""
                    );
                    allTeacherCodeList.add(dataModuler1);
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);

                        TeachercodeDataModuler dataModuler=new TeachercodeDataModuler(
                                receive.getString("name"),
                                receive.getString("code")
                        );
                        allTeacherCodeList.add(dataModuler);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminRoutineListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);




    }


    public void addRoutineDiolouge(final String startTime, final String endTime, final String day, final String period, final String serial) {

        AlertDialog.Builder builder=new AlertDialog.Builder(AdminRoutineListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.routine_diolouge_layout,null);
        builder.setView(view);
        routineAddDioloubeButton=view.findViewById(R.id.routineDiolouge_saveButtonid);
        addRoutineHeaderTextview=view.findViewById(R.id.routine_Diolouge_HeaderTextviewid);
        subjectCodeEdittext=view.findViewById(R.id.routineDiolouge_SubjectCodeEdittextid);
        teacherCodeSpinner=view.findViewById(R.id.routineDiolouge_TeacherCodeSpinnerid);
        rteacherNameEdittext=view.findViewById(R.id.routinediolouge_TeacherNameEdittextid);
        roomNumberEdittext=view.findViewById(R.id.routinediolouge_RoomNumberEdittextid);
        teacherCodeSpinner.setAdapter(teacherCodeAdapter);

        addRoutineHeaderTextview.setText(day+"  : "+startTime+"(st)-- "+endTime+"(et) \n Period: "+period);



        final AlertDialog dialog=builder.create();
        dialog.show();

        teacherCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isfirstSelected==true){
                    isfirstSelected=false;
                }else{
                    TeachercodeDataModuler data=allTeacherCodeList.get(position);
                    rteacherNameEdittext.setText(data.getName());
                    teacherCode=data.getCode();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        routineAddDioloubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(subjectCodeEdittext.getText().toString().isEmpty()){
                    subjectCodeEdittext.setError("Enter Subject code");
                    subjectCodeEdittext.requestFocus();
                } else if(teacherCode.isEmpty()){
                    Toast.makeText(AdminRoutineListActivity.this, "Please Select An Teacher Code", Toast.LENGTH_SHORT).show();
                }
                else if(roomNumberEdittext.getText().toString().isEmpty()){
                    roomNumberEdittext.setError("Enter Room Number");
                    roomNumberEdittext.requestFocus();
                }else if(rteacherNameEdittext.getText().toString().isEmpty()){
                    rteacherNameEdittext.setError("Enter Teacher Name");
                    rteacherNameEdittext.requestFocus();
                }else{


                    saveRoutine(dialog,startTime,endTime,day,rteacherNameEdittext.getText().toString(),teacherCode,period,subjectCodeEdittext.getText().toString(),roomNumberEdittext.getText().toString(),serial);
                }



//                saveShiftGroups(shift,group,dialog);
//
//                getgroup=getGroups();
//                getshift=getShift();
//
//                String shrtdep=makeDepartmentShort(department);
//                String shrtshift=makeShiftShort(getshift);
//                String shrtsem=makeSemesterShort(semester);
//
//                totalShortform= shrtsem+""+shrtdep+""+getgroup+""+shrtshift;
//                header.setText(""+totalShortform);
            }
        });
    }


    private void saveRoutine(final AlertDialog dialog, final String st, final String et, final String day, final String tName, final String tC, final String period, final String sc, final String rN, final String serial) {

        progressDialog2.show();
        URLS urls=new URLS();

        RequestQueue requestQueue= Volley.newRequestQueue(AdminRoutineListActivity.this);
        String url=urls.getRoutine();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog2.dismiss();
                    JSONObject jsonObject=new JSONObject(response);

                    Toast.makeText(AdminRoutineListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getClassRoutine(department,getgroup,getshift,semester);
                    dialog.dismiss();
                } catch (JSONException e) {
                    progressDialog2.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog2.dismiss();
                Toast.makeText(AdminRoutineListActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("startTime",st);
                parms.put("endTime",et);
                parms.put("day",day);
                parms.put("teacherName",tName);
                parms.put("teacherCode",tC);
                parms.put("department",department);
                parms.put("group",getGroups());
                parms.put("shift",getShift());
                parms.put("period",period);
                parms.put("subjectCode",sc);
                parms.put("roomNumber",rN);
                parms.put("semester",semester);
                parms.put("serial",serial);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }





    public void selectDiolouge(final String department, final String semester) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminRoutineListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.student_shift_group_diolouge,null);
        builder.setView(view);
        groupSpinner=view.findViewById(R.id.studentlist_diolougeGroupSpinnerid);
        diolougeHeader=view.findViewById(R.id.studentlistselect_Textviewid);
        shiftSpinner=view.findViewById(R.id.studentList_diolougeShiftSpinnerid);
        addButton=view.findViewById(R.id.studentlistSelect_AddButtonid);
        addButton.setText("Ok");
        diolougeHeader.setText(department+" ( "+semester+" )");


        groupSpinner.setAdapter(groupsAdapter);
        shiftSpinner.setAdapter(shiftsAdapter);


        final AlertDialog dialog=builder.create();
        dialog.show();

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group=groups[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift=shifts[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShiftGroups(shift,group,dialog);

                getgroup=getGroups();
                getshift=getShift();
            setPEriodTime();
              String shrtdep=makeDepartmentShort(department);
              String shrtshift=makeShiftShort(getshift);
               String shrtsem=makeSemesterShort(semester);

               totalShortform= shrtsem+""+shrtdep+""+getgroup+""+shrtshift;
                header.setText(""+totalShortform);
                getClassRoutine(department,getgroup,getshift,semester);

            }
        });
    }


    private void saveShiftGroups(String shift, String group, AlertDialog dialog) {
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("shift",shift);
        editor.putString("group",group);
        editor.commit();
        dialog.dismiss();
    }


    public String getGroups(){
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        String group=sharedPreferences.getString("group","");
        return  group;
    }
    public String getShift(){
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        String shift=sharedPreferences.getString("shift","");
        return  shift;
    }

    public String makeDepartmentShort(String department){
        String shrt = null;
        if(department.equals("Computer")){
            shrt="Cmt";
        } else if(department.equals("Electrical")){
            shrt="Ele";
        }else if(department.equals("Mechanical")){
            shrt="Mc";
        }
        else if(department.equals("Civil")){
            shrt="Cv";
        } else if(department.equals("Electronics")){
            shrt="Elc";
        }else if(department.equals("Power")){
            shrt="Pow";
        }else if(department.equals("Electromedical")){
            shrt="Elm";
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








    private void initialize() {
        header=findViewById(R.id.r_header);
        //find Period
        period=findViewById(R.id.r_periodTextviewid);
        p1=findViewById(R.id.r_p1);
        p2=findViewById(R.id.r_p2);
        p3=findViewById(R.id.r_p3);
        p4=findViewById(R.id.r_p4);
        p5=findViewById(R.id.r_p5);
        p6=findViewById(R.id.r_p6);
        p7=findViewById(R.id.r_p7);

        //find date and time

        timedate=findViewById(R.id.r_timedate);
        p1TimeDate=findViewById(R.id.r_p1_timedate);
        p2TimeDate=findViewById(R.id.r_p2_timedate);
        p3TimeDate=findViewById(R.id.r_p3_timedate);
        p4TimeDate=findViewById(R.id.r_p4_timedate);
        p5TimeDate=findViewById(R.id.r_p5_timedate);
        p6TimeDate=findViewById(R.id.r_p6_timedate);
        p7TimeDate=findViewById(R.id.r_p7_timedate);

        //find saturday


        saturday=findViewById(R.id.r_saturday);
        saturdayP1=findViewById(R.id.r_saturday_p1data);
        saturdayP2=findViewById(R.id.r_saturday_p2data);
        saturdayP3=findViewById(R.id.r_saturday_p3data);
        saturdayP4=findViewById(R.id.r_saturday_p4data);
        saturdayP5=findViewById(R.id.r_saturday_p5data);
        saturdayP6=findViewById(R.id.r_saturday_p6data);
        saturdayP7=findViewById(R.id.r_saturday_p7data);


        //find sunday


        sunday=findViewById(R.id.r_sunday);
        sundayP1=findViewById(R.id.r_sunday_p1data);
        sundayP2=findViewById(R.id.r_sunday_p2data);
        sundayP3=findViewById(R.id.r_sunday_p3data);
        sundayP4=findViewById(R.id.r_sunday_p4data);
        sundayP5=findViewById(R.id.r_sunday_p5data);
        sundayP6=findViewById(R.id.r_sunday_p6data);
        sundayP7=findViewById(R.id.r_sunday_p7data);


        //find monday

        monday=findViewById(R.id.r_monday);
        mondayP1=findViewById(R.id.r_monday_p1data);
        mondayP2=findViewById(R.id.r_monday_p2data);
        mondayP3=findViewById(R.id.r_monday_p3data);
        mondayP4=findViewById(R.id.r_monday_p4data);
        mondayP5=findViewById(R.id.r_monday_p5data);
        mondayP6=findViewById(R.id.r_monday_p6data);
        mondayP7=findViewById(R.id.r_monday_p7data);


        //find tuesday
        tuesday=findViewById(R.id.r_tuesday);
        tuesdayP1=findViewById(R.id.r_tuesday_p1data);
        tuesdayP2=findViewById(R.id.r_tuesday_p2data);
        tuesdayP3=findViewById(R.id.r_tuesday_p3data);
        tuesdayP4=findViewById(R.id.r_tuesday_p4data);
        tuesdayP5=findViewById(R.id.r_tuesday_p5data);
        tuesdayP6=findViewById(R.id.r_tuesday_p6data);
        tuesdayP7=findViewById(R.id.r_tuesday_p7data);

        //find wednesday


        wednesday=findViewById(R.id.r_wednesday);
        wednesdayP1=findViewById(R.id.r_wednesday_p1data);
        wednesdayP2=findViewById(R.id.r_wednesday_p2data);
        wednesdayP3=findViewById(R.id.r_wednesday_p3data);
        wednesdayP4=findViewById(R.id.r_wednesday_p4data);
        wednesdayP5=findViewById(R.id.r_wednesday_p5data);
        wednesdayP6=findViewById(R.id.r_wednesday_p6data);
        wednesdayP7=findViewById(R.id.r_wednesday_p7data);

        //find thursday


        thursday=findViewById(R.id.r_thursday);
        thursdayP1=findViewById(R.id.r_thursday_p1data);
        thursdayP2=findViewById(R.id.r_thursday_p2data);
        thursdayP3=findViewById(R.id.r_thursday_p3data);
        thursdayP4=findViewById(R.id.r_thursday_p4data);
        thursdayP5=findViewById(R.id.r_thursday_p5data);
        thursdayP6=findViewById(R.id.r_thursday_p6data);
        thursdayP7=findViewById(R.id.r_thursday_p7data);



    }



}

