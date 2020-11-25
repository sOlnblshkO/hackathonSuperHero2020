package com.example.forfedorova.LogAndReg;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;

public class registrActivity extends AppCompatActivity {

    EditText regLogEdit, regPassEdit, regNameEdit, regSurEdit;
    Button signUpBtn;

    public final static String url = "http://koyash.tmweb.ru/api.php";

    MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        dialog = new MyCustomDialog(registrActivity.this);

        signUpBtn = findViewById(R.id.signUpBtn);
        regLogEdit = findViewById(R.id.regLogEdit);
        regNameEdit = findViewById(R.id.regNameEdit);
        regSurEdit = findViewById(R.id.regSurEdit);
        regPassEdit = findViewById(R.id.regPassEdit);

        signUpBtn.setOnClickListener(signUp);
    }

    View.OnClickListener signUp = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (!regLogEdit.getText().toString().equals("") && !regPassEdit.getText().toString().equals("")
            && !regNameEdit.getText().toString().equals("") && !regNameEdit.getText().toString().equals("")) {
                new doApi().execute("registr");
            } else {
                Toast.makeText(getApplicationContext(), "Заполните поля!",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class doApi extends AsyncTask<String, Void, Void> {

        String action, response;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "registr":
                    multipartEntity.addPart("code", "registr");
                    multipartEntity.addPart("login", regLogEdit.getText().toString());
                    multipartEntity.addPart("pass", regPassEdit.getText().toString());
                    multipartEntity.addPart("name", regNameEdit.getText().toString());
                    multipartEntity.addPart("sur", regSurEdit.getText().toString());
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
            switch (response){
                case "0":
                    Toast.makeText(getApplicationContext(),"Такой пользователь уже зарегестрирован!", Toast.LENGTH_LONG).show();
                    break;
                case "1":
                    Toast.makeText(getApplicationContext(),"Вы успешно зарегестрировались!" + response, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case "2":
                    Toast.makeText(getApplicationContext(),"Оишбка на сервере. Попробуйте поменять логин или пароль.", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }


    }


}
