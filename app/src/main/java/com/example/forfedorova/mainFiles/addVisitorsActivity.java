package com.example.forfedorova.mainFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.forfedorova.CustomClasses.MyCustomDialog;
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
import java.util.ArrayList;

public class addVisitorsActivity extends AppCompatActivity {

    EditText idEdit;
    Button addVisitorBtn;
    MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitors);

        dialog = new MyCustomDialog(addVisitorsActivity.this, "Отправка данных!");

        idEdit = findViewById(R.id.visitorIdEdit);
        addVisitorBtn = findViewById(R.id.addVisitorBtn);


        addVisitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] strArr = idEdit.getText().toString().split(" ");
                String str = "";

                for (String s : strArr){
                    if (!s.equals("")){
                        str += s + " ";
                    }
                }
                str = str.substring(0, str.length() - 1);

                idEdit.setText(str);

                if (!idEdit.getText().toString().equals("")) {
                    new addVis().execute("addVis");
                }
            }
        });
    }
    String response;
    private static final String url = "http://koyash.tmweb.ru/api.php";

    public class addVis extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "addVis":
                    multipartEntity.addPart("code", "addVis");
                    multipartEntity.addPart("idUser", idEdit.getText().toString());
                    String id = getIntent().getStringExtra("idActiv");
                    multipartEntity.addPart("idActiv", id);
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
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("success").equals("1")) {
                    Toast.makeText(getApplicationContext(), "Добавлено", Toast.LENGTH_SHORT).show();
                    idEdit.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
