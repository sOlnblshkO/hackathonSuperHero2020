package com.example.forfedorova.mainFiles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forfedorova.CustomStuff.MyCustomDialog;
import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class adminRep extends AppCompatActivity {

    EditText admin, rep;
    Button refreshBtn;
    MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rep);
        dialog = new MyCustomDialog(adminRep.this);
        admin = findViewById(R.id.newAdminEdit);
        rep = findViewById(R.id.newRepEdit);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!admin.getText().toString().equals(""))
                    new setAdmRep().execute("setAdmin");
                if (!rep.getText().toString().equals(""))
                    new setAdmRep().execute("setRep");
            }
        });
        dialog.createDialog();
        new setAdmRep().execute("getAdminRep");
    }

    String response;
    private static final String url = "http://koyash.tmweb.ru/api.php";

    public class setAdmRep extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "setAdmin":

                    multipartEntity.addPart("code", "setAdmin");
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
                    multipartEntity.addPart("loginAdmin", admin.getText().toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                case "setRep":
                    multipartEntity.addPart("code", "setRep");
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
                    multipartEntity.addPart("loginRep", rep.getText().toString());;
                    httpPost.setEntity(multipartEntity);
                    break;
                case "getAdminRep":
                    multipartEntity.addPart("code", "getAdminRep");
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
                    httpPost.setEntity(multipartEntity);
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


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            dialog.closeDialog();
            switch (action) {
                case "setAdmin":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.get("success").equals("1")){
                            Toast.makeText(getApplicationContext(), "Администратор обновлен", Toast.LENGTH_SHORT).show();
                            admin.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "Такого админа нет в базе данных", Toast.LENGTH_SHORT).show();
                            rep.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "setRep":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.get("success").equals("1")){
                            Toast.makeText(getApplicationContext(), "Представитель обновлен", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Такого представителя нет в базе данных", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "getAdminRep":
                    try{

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.get("success").equals("1")){
                            rep.setText(jsonObject.get("repLogin").toString());
                            admin.setText(jsonObject.get("adminLogin").toString());
                        } else {
                            Toast toast = new Toast(adminRep.this);
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
