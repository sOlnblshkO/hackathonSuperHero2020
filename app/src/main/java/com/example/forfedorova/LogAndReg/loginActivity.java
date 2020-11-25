package com.example.forfedorova.LogAndReg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.forfedorova.CustomStuff.MyCustomDialog;
import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;
import com.example.forfedorova.administrator.adminMenuActivity;
import com.example.forfedorova.representative.repActivitiy;
import com.example.forfedorova.schoolkid.ui.schoolKidMenuActivity;
import com.example.forfedorova.superAdmin.superAdminActivity;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class loginActivity extends AppCompatActivity {

    EditText loginEdit, passEdit;
    Button sendBtn, regBtn;
    String response;

    SharedPreferences sPref;
    SharedPreferences.Editor sPrefEditor;

    MyCustomDialog dialog;

    private static final String url = "http://koyash.tmweb.ru/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new MyCustomDialog(loginActivity.this);

        loginEdit = findViewById(R.id.loginEdit);
        passEdit = findViewById(R.id.passEdit);
        sendBtn = findViewById(R.id.sendBtn);
        regBtn = findViewById(R.id.regBtn);

        sendBtn.setOnClickListener(login);
        regBtn.setOnClickListener(registr);

        sPref = getSharedPreferences("app", MODE_PRIVATE);
        sPrefEditor = sPref.edit();

        if (sPref.getBoolean("loggined", false) == true){
            if (sPref.getString("rank", "-1").equals("1")) {
                Intent prPage = new Intent(getApplicationContext(), schoolKidMenuActivity.class);
                startActivity(prPage);
            } else if (sPref.getString("rank", "-1").equals("3")){
                Intent prPage = new Intent(getApplicationContext(), adminMenuActivity.class);
                startActivity(prPage);
            } else if (sPref.getString("rank", "-1").equals("4")){
                Intent prPage = new Intent(getApplicationContext(), superAdminActivity.class);
                startActivity(prPage);
            } else if (sPref.getString("rank", "-1").equals("2")){
                Intent prPage = new Intent(getApplicationContext(), repActivitiy.class);
                startActivity(prPage);
            }
            finish();
        }

    }

    View.OnClickListener login = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                if (!loginEdit.getText().toString().equals("") && !passEdit.getText().toString().equals("")) {
                    try {
                        new getTestInfo().execute("login");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Поля пусты!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
    };

    View.OnClickListener registr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent regAct = new Intent(getApplicationContext(), registrActivity.class);
            startActivity(regAct);
        }
    };

    public class getTestInfo extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "login":
                    multipartEntity.addPart("code", "login");
                    multipartEntity.addPart("login", loginEdit.getText().toString());
                    multipartEntity.addPart("pass", passEdit.getText().toString());
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
            try {
                dialog.closeDialog();
                if (!response.equals("0")){
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("code").equals("1")) {
                        sPrefEditor.putBoolean("loggined", true);
                        sPrefEditor.putString("name", jsonObject.get("name").toString());
                        sPrefEditor.putString("sur", jsonObject.get("sur").toString());
                        sPrefEditor.putString("login", loginEdit.getText().toString());
                        sPrefEditor.putString("pass", passEdit.getText().toString());
                        sPrefEditor.putString("rank", jsonObject.get("rank").toString());
                        sPrefEditor.putString("id", jsonObject.get("id").toString());
                        sPrefEditor.apply();
                        if (sPref.getString("rank", "-1").equals("1")) {
                            Intent prPage = new Intent(getApplicationContext(), schoolKidMenuActivity.class);
                            startActivity(prPage);
                        } else if (sPref.getString("rank", "-1").equals("3")) {
                            Intent prPage = new Intent(getApplicationContext(), adminMenuActivity.class);
                            startActivity(prPage);
                        } else if (sPref.getString("rank", "-1").equals("4")){
                            Intent prPage = new Intent(getApplicationContext(), superAdminActivity.class);
                            startActivity(prPage);
                        } else if (sPref.getString("rank", "-1").equals("2")) {
                            Intent prPage = new Intent(getApplicationContext(), repActivitiy.class);
                            startActivity(prPage);
                        }
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Такого пользователя не существует!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Непредвиденная ошибка. " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }


}

