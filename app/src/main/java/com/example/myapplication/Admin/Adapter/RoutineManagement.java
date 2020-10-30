package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdminRoutineListActivity;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutineManagement {

    private Context context;
    private List<RoutineDataModuler> routineDataList=new ArrayList<>();
    private TextView saturday,saturdayP1,saturdayP2,saturdayP3,saturdayP4,saturdayP5,saturdayP6,saturdayP7;
    private  TextView sunday,sundayP1,sundayP2,sundayP3,sundayP4,sundayP5,sundayP6,sundayP7;
    private  TextView monday,mondayP1,mondayP2,mondayP3,mondayP4,mondayP5,mondayP6,mondayP7;
    private  TextView tuesday,tuesdayP1,tuesdayP2,tuesdayP3,tuesdayP4,tuesdayP5,tuesdayP6,tuesdayP7;
    private  TextView wednesday,wednesdayP1,wednesdayP2,wednesdayP3,wednesdayP4,wednesdayP5,wednesdayP6,wednesdayP7;
    private  TextView thursday,thursdayP1,thursdayP2,thursdayP3,thursdayP4,thursdayP5,thursdayP6,thursdayP7;


    public RoutineManagement(Context context,List<RoutineDataModuler> routineDataList, TextView saturday, TextView saturdayP1, TextView saturdayP2, TextView saturdayP3, TextView saturdayP4, TextView saturdayP5, TextView saturdayP6, TextView saturdayP7, TextView sunday, TextView sundayP1, TextView sundayP2, TextView sundayP3, TextView sundayP4, TextView sundayP5, TextView sundayP6, TextView sundayP7, TextView monday, TextView mondayP1, TextView mondayP2, TextView mondayP3, TextView mondayP4, TextView mondayP5, TextView mondayP6, TextView mondayP7, TextView tuesday, TextView tuesdayP1, TextView tuesdayP2, TextView tuesdayP3, TextView tuesdayP4, TextView tuesdayP5, TextView tuesdayP6, TextView tuesdayP7, TextView wednesday, TextView wednesdayP1, TextView wednesdayP2, TextView wednesdayP3, TextView wednesdayP4, TextView wednesdayP5, TextView wednesdayP6, TextView wednesdayP7, TextView thursday, TextView thursdayP1, TextView thursdayP2, TextView thursdayP3, TextView thursdayP4, TextView thursdayP5, TextView thursdayP6, TextView thursdayP7) {
          this.context=context;
          this.routineDataList=routineDataList;
      this.saturday = saturday;
        this.saturdayP1 = saturdayP1;
        this.saturdayP2 = saturdayP2;
        this.saturdayP3 = saturdayP3;
        this.saturdayP4 = saturdayP4;
        this.saturdayP5 = saturdayP5;
        this.saturdayP6 = saturdayP6;
        this.saturdayP7 = saturdayP7;
        this.sunday = sunday;
        this.sundayP1 = sundayP1;
        this.sundayP2 = sundayP2;
        this.sundayP3 = sundayP3;
        this.sundayP4 = sundayP4;
        this.sundayP5 = sundayP5;
        this.sundayP6 = sundayP6;
        this.sundayP7 = sundayP7;
        this.monday = monday;
        this.mondayP1 = mondayP1;
        this.mondayP2 = mondayP2;
        this.mondayP3 = mondayP3;
        this.mondayP4 = mondayP4;
        this.mondayP5 = mondayP5;
        this.mondayP6 = mondayP6;
        this.mondayP7 = mondayP7;
        this.tuesday = tuesday;
        this.tuesdayP1 = tuesdayP1;
        this.tuesdayP2 = tuesdayP2;
        this.tuesdayP3 = tuesdayP3;
        this.tuesdayP4 = tuesdayP4;
        this.tuesdayP5 = tuesdayP5;
        this.tuesdayP6 = tuesdayP6;
        this.tuesdayP7 = tuesdayP7;
        this.wednesday = wednesday;
        this.wednesdayP1 = wednesdayP1;
        this.wednesdayP2 = wednesdayP2;
        this.wednesdayP3 = wednesdayP3;
        this.wednesdayP4 = wednesdayP4;
        this.wednesdayP5 = wednesdayP5;
        this.wednesdayP6 = wednesdayP6;
        this.wednesdayP7 = wednesdayP7;
        this.thursday = thursday;
        this.thursdayP1 = thursdayP1;
        this.thursdayP2 = thursdayP2;
        this.thursdayP3 = thursdayP3;
        this.thursdayP4 = thursdayP4;
        this.thursdayP5 = thursdayP5;
        this.thursdayP6 = thursdayP6;
        this.thursdayP7 = thursdayP7;
    }


    public void setRoutineText(){

        if(routineDataList.size()>0){

            for(int i=0; i<routineDataList.size(); i++){
                String serial=routineDataList.get(i).getSerial();
                String subjectCode=routineDataList.get(i).getSubjectCode();
                String teachershrtname=routineDataList.get(i).getTeacherName().substring(3,5);
                String roomNumber=routineDataList.get(i).getRoomNumber();

                if(serial.equals("11")){
                    saturdayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                } else if(serial.equals("12")){
                    saturdayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("13")){
                    saturdayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("14")){
                    saturdayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                } else if(serial.equals("15")){
                    saturdayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                } else if(serial.equals("16")){
                    saturdayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("17")){
                    saturdayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("21")){
                    sundayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("22")){
                    sundayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("23")){
                    sundayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("24")){
                    sundayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("25")){
                    sundayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("26")){
                    sundayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("27")){
                    sundayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("31")){
                    mondayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("32")){
                    mondayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("33")){
                    mondayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                } else if(serial.equals("34")){
                    mondayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("35")){
                    mondayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("36")){
                    mondayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("37")){
                    mondayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("41")){
                    tuesdayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("42")){
                    tuesdayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("43")){
                    tuesdayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("44")){
                    tuesdayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("45")){
                    tuesdayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("46")){
                    tuesdayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("47")){
                    tuesdayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("51")){
                    wednesdayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("52")){
                    wednesdayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("53")){
                    wednesdayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("54")){
                    wednesdayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                } else if(serial.equals("55")){
                    wednesdayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("56")){
                    wednesdayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("57")){
                    wednesdayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("61")){
                    thursdayP1.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("62")){
                    thursdayP2.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("63")){
                    thursdayP3.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("64")){
                    thursdayP4.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("65")){
                    thursdayP5.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("66")){
                    thursdayP6.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }else if(serial.equals("67")){
                    thursdayP7.setText(subjectCode+"\n"+teachershrtname+"\n"+roomNumber);
                }


            }

        }else {
            saturdayP1.setText("");
            saturdayP2.setText("");
            saturdayP3.setText("");
            saturdayP4.setText("");
            saturdayP5.setText("");
            saturdayP6.setText("");
            saturdayP7.setText("");
            sundayP1.setText("");
            sundayP2.setText("");
            sundayP3.setText("");
            sundayP4.setText("");
            sundayP5.setText("");
            sundayP6.setText("");
            sundayP7.setText("");

            mondayP1.setText("");
            mondayP2.setText("");
            mondayP3.setText("");
            mondayP4.setText("");
            mondayP5.setText("");
            mondayP6.setText("");
            mondayP7.setText("");

            tuesdayP1.setText("");
            tuesdayP2.setText("");
            tuesdayP3.setText("");
            tuesdayP4.setText("");
            tuesdayP5.setText("");
            tuesdayP6.setText("");
            tuesdayP7.setText("");

            wednesdayP1.setText("");
            wednesdayP2.setText("");
            wednesdayP3.setText("");
            wednesdayP4.setText("");
            wednesdayP5.setText("");
            wednesdayP6.setText("");
            wednesdayP7.setText("");

            thursdayP1.setText("");
            thursdayP2.setText("");
            thursdayP3.setText("");
            thursdayP4.setText("");
            thursdayP5.setText("");
            thursdayP6.setText("");
            thursdayP7.setText("");





        }





    }









}
