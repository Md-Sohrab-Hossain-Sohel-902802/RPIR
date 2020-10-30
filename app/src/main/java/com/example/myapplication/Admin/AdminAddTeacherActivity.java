package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.Adapter.TeacherAdapter;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.TeacherRegisterActivity;
import com.example.myapplication.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAddTeacherActivity extends AppCompatActivity {

    private  RecyclerView recyclerView;

    private EditText teacherNameEdittext,teacherCodeEdittext,teacherPhoneEdittext;
    private Spinner dipartmentSpinner,mainDepartmentSpinner;
    private Button addButton;
    private  String name,code,phone,department;

    private SpinnerCustomAdapter spinnerAdapter;

    final String[] values={"Computer","Electrical","Power","Civil","Mechanical","Electronics","Electromedical"};

    private String mainselectedDepartement;
    private  List<TeacherDataModuler> teacherdataList=new ArrayList<>();
    private TeacherAdapter adapter;

    private  Uri imageUri;
    private DatabaseReference database;
    private  ProgressDialog progressDialog;
    private  ProgressDialog progressDialog2;
    private  ProgressDialog progressDialog3;



    private  StorageReference storageReference;

    String currentCode,currentDepartment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_teacher);
        progressDialog2=new ProgressDialog(this);
        progressDialog2.setTitle("Loading");
        progressDialog2.setCanceledOnTouchOutside(false);

        progressDialog3=new ProgressDialog(this);
        progressDialog3.setTitle("Loading");
        progressDialog3.setCanceledOnTouchOutside(false);

        toolbar=findViewById(R.id.admin_addTeacherToolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Teacher List");

        storageReference= FirebaseStorage.getInstance().getReference().child("Rpi").child("TeacherImage");
        database= FirebaseDatabase.getInstance().getReference();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Image Uploading");
        progressDialog.setMessage("Please Wait......");

        recyclerView=findViewById(R.id.addnewTeacherRecyclerViewid);
        mainDepartmentSpinner=findViewById(R.id.addTeacher_DepartmentListSpinner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerAdapter=new SpinnerCustomAdapter(AdminAddTeacherActivity.this,values);
        mainDepartmentSpinner.setAdapter(spinnerAdapter);

        mainDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainselectedDepartement=values[position];
                teacherdataList.clear();
                adapter.notifyDataSetChanged();
              getAllTeacher();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    adapter=new TeacherAdapter(this,teacherdataList);
    recyclerView.setAdapter(adapter);




    adapter.setOnItemClickListner(new TeacherAdapter.OnItemClickListner() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onDelete(int position) {
            TeacherDataModuler currentItem=teacherdataList.get(position);
                    deleteTeacher(currentItem.getId());
        }

        @Override
        public void onUpdate(int position) {

        }

        @Override
        public void onImageUpdate(int position) {

            openfilechooser();

             currentCode=teacherdataList.get(position).getCode();
             currentDepartment=teacherdataList.get(position).getDepartmentname();





        }
        });

    }

    private void deleteTeacher(String id) {
        URLS urls=new URLS();


        RequestQueue requestQueue= Volley.newRequestQueue(AdminAddTeacherActivity.this);
        String url=urls.getTeacherAdd()+id;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(AdminAddTeacherActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    getAllTeacher();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddTeacherActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }

    private void openfilechooser() {

        Intent intentf=new Intent();
        intentf.setType("image/*");
        intentf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentf,1);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            saveImage();
      }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void saveImage() {
        progressDialog.show();

        String key=database.push().getKey();

        StorageReference filePath=storageReference.child(key+database.push().getKey()+"."+getFileExtension(imageUri));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloaduri=urlTask.getResult();
               saveImageLink(currentCode,currentDepartment,downloaduri.toString());
            }
        });
    }






    @Override
    protected void onStart() {
        super.onStart();
        getAllTeacher();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addMenuButtonid){
            showcustomdioloage();
        }
        return super.onOptionsItemSelected(item);
    }




    public void getAllTeacher(){
        progressDialog2.show();

        URLS urls=new URLS();


        RequestQueue requestQueue= Volley.newRequestQueue(AdminAddTeacherActivity.this);
        String url=urls.getGetDepartmentTeacher()+mainselectedDepartement;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog2.dismiss();

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    teacherdataList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        TeacherDataModuler teacherDataModul=new TeacherDataModuler(
                                receive.getString("_id"),
                                receive.getString("name"),
                                receive.getString("code"),
                                receive.getString("departmentname"),
                                receive.getString("phone"),
                                receive.getString("image")
                        );

                        teacherdataList.add(teacherDataModul);
                   adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    progressDialog2.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog2.dismiss();
                Toast.makeText(AdminAddTeacherActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }



    public void showcustomdioloage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminAddTeacherActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_teacher_diolouge_layout,null);
        builder.setView(view);
        teacherCodeEdittext=view.findViewById(R.id.teacherdiolouge_TeacherCodeEdittextid);
        teacherNameEdittext=view.findViewById(R.id.teacherdiolouge_TeacherNameEdittextid);
        addButton=view.findViewById(R.id.teacherdiolouge_AddTeacherButton);
        teacherPhoneEdittext=view.findViewById(R.id.teacherdiolouge_TeacherPhoneEdittextid);
        dipartmentSpinner=view.findViewById(R.id.teacher_diolougeDepartmentSpinnerid);

     dipartmentSpinner.setAdapter(spinnerAdapter);



        final AlertDialog dialog=builder.create();
        dialog.show();

        dipartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department=values[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=teacherNameEdittext.getText().toString();
                code=teacherCodeEdittext.getText().toString();
                phone=teacherPhoneEdittext.getText().toString();
                if(name.isEmpty()){
                    teacherNameEdittext.setError("Enter Teacher Name");
                    teacherNameEdittext.requestFocus();
                }else if(code.isEmpty()){
                    teacherCodeEdittext.setError("Enter Teacher Code");
                    teacherCodeEdittext.requestFocus();
                }else if(phone.isEmpty()){
                    teacherPhoneEdittext.setError("Enter  Phone");
                    teacherPhoneEdittext.requestFocus();
                }else if(department.isEmpty()){
                    Toast.makeText(AdminAddTeacherActivity.this, "Please Choose A Department", Toast.LENGTH_SHORT).show();
                }else{
                  saveTeacherData(dialog);
                }
            }
        });
    }
    private void saveImageLink(final String code, final String department, final String image) {
    RequestQueue requestQueue= Volley.newRequestQueue(AdminAddTeacherActivity.this);
    URLS urls=new URLS();

    String imageUrl=urls.getTeacherAdd()+"image/"+code+"/"+department;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, imageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    Toast.makeText(AdminAddTeacherActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                   onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AdminAddTeacherActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("image",image);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }




    private void saveTeacherData(final AlertDialog dialog) {
        progressDialog3.show();
    URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminAddTeacherActivity.this);
        String url=urls.getTeacherAdd();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog3.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                  String r=  jsonObject.getString("r");
                  if(r.equals("u")){
                      Toast.makeText(AdminAddTeacherActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                  }else if(r.equals("p")){
                      Toast.makeText(AdminAddTeacherActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                      teacherPhoneEdittext.setError("Phone Number Already Used");
                      teacherPhoneEdittext.requestFocus();
                  }else if(r.equals("d")){

                      Toast.makeText(AdminAddTeacherActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                      teacherNameEdittext.setText("");
                      teacherCodeEdittext.setText("");
                      teacherPhoneEdittext.setText("");
                      dialog.dismiss();
                      //  teacherDataList.clear();
                      onStart();
                  }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog3.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddTeacherActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog3.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("name",name);
                parms.put("code",code);
                parms.put("departmentname",department);
                parms.put("image","");
                parms.put("phone",phone);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }
}