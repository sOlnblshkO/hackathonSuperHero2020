package com.example.forfedorova.superAdmin.ui.profile.rank;

import androidx.lifecycle.ViewModelProviders;

import android.animation.TimeAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class changeRankFragment extends Fragment {

    private ChangeRankViewModel mViewModel;

    public static changeRankFragment newInstance() {
        return new changeRankFragment();
    }

    View v;
    Button changeRankBtn;
    EditText rankLoginEdit;
    RadioGroup radioGroup;
    private int rank = 0;

    String response;
    private final static String url = "http://koyash.tmweb.ru/api.php";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.change_rank_fragment, container, false);
        changeRankBtn = v.findViewById(R.id.changeRankBtn);
        rankLoginEdit = v.findViewById(R.id.rankLoginEdit);
        radioGroup = v.findViewById(R.id.rankRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getActivity(), "Вы не выбрали ранг", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.repRadioBtn:
                        rank = 2;
                        break;
                    case R.id.adminRadioBtn:
                        rank = 3;
                        break;
                    case R.id.superRadioBtn:
                        rank = 4;
                        break;
                }
            }
        });

        changeRankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(rank == 0) && !(rankLoginEdit.getText().toString().equals(""))) {
                     new changeRank().execute("checkLogin");
                } else {
                    Toast.makeText(getActivity(), "Не все пункты заполнены", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChangeRankViewModel.class);
        // TODO: Use the ViewModel
    }

    public class changeRank extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "checkLogin":
                    multipartEntity.addPart("code", "checkLogin");
                    multipartEntity.addPart("login", rankLoginEdit.getText().toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                case "changeRank1":
                    multipartEntity.addPart("code", "changeRank");
                    multipartEntity.addPart("login", rankLoginEdit.getText().toString());
                    multipartEntity.addPart("newRank", String.valueOf(rank));
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
                case "checkLogin":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            new changeRank().execute("changeRank1");
                        } else {
                            Toast.makeText(getActivity(), "Такой пользователь не найден", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "changeRank1":
                    try {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                           Toast.makeText(getActivity(), "Ранг изменен.", Toast.LENGTH_SHORT).show();
                           rankLoginEdit.setText("");
                           radioGroup.clearCheck();
                        } else {
                            Toast.makeText(getActivity(), "Что то пошло не так. Ранг не был изменен", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


    }

}
