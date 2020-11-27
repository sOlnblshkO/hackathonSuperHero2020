package com.example.forfedorova.mainFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forfedorova.CustomClasses.MyCustomDialog;
import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class createActivityForOrgActivity extends AppCompatActivity {

    TextView name, desc;
    EditText admin, rep, activName, activDesc, startTime, endTime;
    Button createActiv;
    boolean byAdmin = false;

    SharedPreferences sPref;

    String response;
    private static final String url = "http://koyash.tmweb.ru/api.php";

    MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_for_org);

        dialog = new MyCustomDialog(createActivityForOrgActivity.this);

        sPref = getSharedPreferences("app", MODE_PRIVATE);

        name = findViewById(R.id.orgNameActionTextView);
        desc = findViewById(R.id.orgDescActionTextview);

        name.setText(getIntent().getStringExtra("name"));
        desc.setText(getIntent().getStringExtra("desc"));

        admin = findViewById(R.id.activityAdminEdit);
        activName = findViewById(R.id.activityNameEdit);
        activDesc = findViewById(R.id.activityDescEdit);
        rep = findViewById(R.id.activityPreEdit);
        startTime = findViewById(R.id.startTimeEdit);
        endTime = findViewById(R.id.endTimeEdit);

        createActiv = findViewById(R.id.createActivBtn);
        createActiv.setOnClickListener(createActivity);


        byAdmin = getIntent().getBooleanExtra("byAdmin", false);
        if (byAdmin) {
            admin.setEnabled(false);
            admin.setText(sPref.getString("login", "noLogin!!!"));
        }

        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createDateDialog("start");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog("end");
            }
        });

    }

    public void createDateDialog(final String dateType){

        AlertDialog.Builder builder = new AlertDialog.Builder(createActivityForOrgActivity.this);
        View dialogView = LayoutInflater.from(createActivityForOrgActivity.this).inflate(R.layout.date_custom_dialog, null);
        builder.setView(dialogView);

        final CalendarView activCal = dialogView.findViewById(R.id.activityCalendar);

        final String[] finalDate = {""};

        activCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mYear)
                        .append("-").append(mMonth + 1).append("-").append(mDay)
                        .toString();
                finalDate[0] = selectedDate;
            }
        });

        AlertDialog dateDialog = null;
        Button chooseDialog = dialogView.findViewById(R.id.chooseDateBtn);

        dateDialog = builder.create();
        dateDialog.show();
        final AlertDialog finalDateDialog = dateDialog;
        chooseDialog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (dateType) {
                    case ("start"):
                        startTime.setText(finalDate[0]);
                        finalDateDialog.cancel();
                        break;
                    case ("end"):
                        endTime.setText(finalDate[0]);
                        finalDateDialog.cancel();
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new sendActivity().execute("adminRep");
    }



    View.OnClickListener createActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!activDesc.getText().toString().equals("")
                    && !activName.getText().toString().equals("")
                    && !admin.getText().toString().equals("")
                    && !rep.getText().toString().equals("")) {
                new sendActivity().execute("checkAdmin");
            } else {
                Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class sendActivity extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "adminRep":
                    multipartEntity.addPart("code", "getAdminRep");
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
                    httpPost.setEntity(multipartEntity);
                    break;
                case "checkAdmin":
                    multipartEntity.addPart("code", "checkAdmin");
                    multipartEntity.addPart("admin", admin.getText().toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                case "checkRep":
                    multipartEntity.addPart("code", "checkRep");
                    multipartEntity.addPart("rep", rep.getText().toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                case "createActiv":
                    multipartEntity.addPart("code", "createActiv");
                    multipartEntity.addPart("admin", admin.getText().toString());
                    multipartEntity.addPart("rep", rep.getText().toString());
                    multipartEntity.addPart("name", activName.getText().toString());
                    multipartEntity.addPart("descript", activDesc.getText().toString());
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
                    multipartEntity.addPart("startDate", startTime.getText().toString());
                    multipartEntity.addPart("endDate", endTime.getText().toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                default:
                    break;
            }


            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.createDialog();
        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            dialog.closeDialog();
            switch (action) {
                case "checkAdmin":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            new sendActivity().execute("checkRep");
                        } else {
                            Toast.makeText(getApplicationContext(),"Такого админа в базе данных нет", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case "checkRep":
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            new sendActivity().execute("createActiv");
                        } else {
                            Toast.makeText(getApplicationContext(), "Такого представителя в базе данных нет", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case "createActiv":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            Toast.makeText(getApplicationContext(), "Мероприятие успешно создано", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "adminRep":
                    try{

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.get("success").equals("1")){
                            rep.setText(jsonObject.get("repLogin").toString());
                            admin.setText(jsonObject.get("adminLogin").toString());
                        } else {
                            Toast toast = new Toast(createActivityForOrgActivity.this);
                            toast.setText("Непредвиденная ошибка, попробуйте позже");
                            toast.show();
                            finish();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }


    }

}
