package com.example.forfedorova.mainFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;
import com.example.forfedorova.administrator.adminMenuActivity;
import com.example.forfedorova.schoolkid.ui.schoolKidMenuActivity;
import com.example.forfedorova.superAdmin.superAdminActivity;
import com.example.forfedorova.superAdmin.ui.profile.organizations.superAdminOrgs;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class createOrgActivity extends AppCompatActivity {

    EditText name, desc, admin, rep;
    Button createOrgBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_org);
        name = findViewById(R.id.createOrgNameEdit);
        desc = findViewById(R.id.createOrgDescEdit);

        createOrgBtn = findViewById(R.id.createOrgBtn);

        createOrgBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if ((!(name.getText().toString().equals(""))
                        && !(desc.getText().toString().equals("")))) {
                    new createOrg().execute("createOrg");
                } else {
                    Toast.makeText(getApplicationContext(), "Не все поля заполнены!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static final String url = "http://koyash.tmweb.ru/api.php";
    String response;

    public class createOrg extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "createOrg":
                    multipartEntity.addPart("code", "createOrg");
                    multipartEntity.addPart("name", name.getText().toString());
                    multipartEntity.addPart("descript", desc.getText().toString());
                    multipartEntity.addPart("createdBy", getSharedPreferences("app", MODE_PRIVATE).getString("id", "-1"));
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
                case "createOrg":
                    if (response.equals("1")){
                        finish();
                    }
                default:
                    break;
            }
        }


    }

}
