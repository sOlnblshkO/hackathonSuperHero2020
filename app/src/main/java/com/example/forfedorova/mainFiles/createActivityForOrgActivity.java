package com.example.forfedorova.mainFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;
import com.example.forfedorova.administrator.adminMenuActivity;
import com.example.forfedorova.schoolkid.ui.schoolKidMenuActivity;
import com.example.forfedorova.superAdmin.superAdminActivity;

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
    EditText admin, rep, activName, activDesc;
    Button createActiv;
    boolean byAdmin = false;

    SharedPreferences sPref;

    String response;
    private static final String url = "http://koyash.tmweb.ru/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_for_org);

        sPref = getSharedPreferences("app", MODE_PRIVATE);

        name = findViewById(R.id.orgNameActionTextView);
        desc = findViewById(R.id.orgDescActionTextview);

        name.setText(getIntent().getStringExtra("name"));
        desc.setText(getIntent().getStringExtra("desc"));

        admin = findViewById(R.id.activityAdminEdit);
        activName = findViewById(R.id.activityNameEdit);
        activDesc = findViewById(R.id.activityDescEdit);
        rep = findViewById(R.id.activityPreEdit);

        createActiv = findViewById(R.id.createActivBtn);
        createActiv.setOnClickListener(createActivity);


        byAdmin = getIntent().getBooleanExtra("byAdmin", false);
        if (byAdmin) {
            admin.setEnabled(false);
            admin.setText(sPref.getString("login", "noLogin!!!"));
        }

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
        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
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
            }
        }


    }

}
