package com.example.forfedorova.superAdmin.ui.profile.organizations;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;
import com.example.forfedorova.mainFiles.createActivityForOrgActivity;
import com.example.forfedorova.mainFiles.createOrgActivity;
import com.example.forfedorova.mainFiles.orgEditActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class superAdminOrgs extends Fragment {

    private SuperAdminOrgsViewModel mViewModel;

    SharedPreferences sPref;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<orgsData> myOrgs;
    Button createOrg;

    public static superAdminOrgs newInstance() {
        return new superAdminOrgs();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.super_admin_orgs_fragment, container, false);

        sPref = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.recyclerViewSuperAdminOrgs);
        recyclerView.setHasFixedSize(true);

        createOrg = view.findViewById(R.id.createOrgRepEdit);
        createOrg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent createOrg = new Intent(getActivity(), createOrgActivity.class);
                startActivity(createOrg);
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        myOrgs = new ArrayList<>();
        loadOrgs();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SuperAdminOrgsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class orgsData{
        String name, desc;
        int idOrg;
        public orgsData(String name, String desc, int idOrg){
            this.name = name;
            this.desc = desc;
            this.idOrg = idOrg;
        }
    }

    public void loadOrgs(){
        new getOrgs().execute("getOrgs");
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.OrgViewHolder> {
        private List<orgsData> mDataset;

        public class OrgViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView nameTextView, descTextView, orgIdTextView;
            public Button addActionBtn, editOrgBtn;
            CardView cv;
            public OrgViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.orgCard);
                Random rnd = new Random();
                ArrayList<Integer> colors = new ArrayList<>();
                int colorRed = Color.argb(255, 255, 128, 0);
                int colorGreen = Color.argb(255, 0, 255, 0);
                int colorBlue = Color.argb(255, 66, 145, 255);
                colors.add(colorRed);
                colors.add(colorGreen);
                colors.add(colorBlue);

                cv.setBackgroundColor(colors.get(rnd.nextInt(3)));

                nameTextView = v.findViewById(R.id.orgNameTextView);
                descTextView = v.findViewById(R.id.orgDescTextView);
                orgIdTextView = v.findViewById(R.id.orgIdTextView);
                addActionBtn = v.findViewById(R.id.orgAddActionBtn);
                editOrgBtn = v.findViewById(R.id.orgEditBtn);


                addActionBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent addInt = new Intent(getActivity(), createActivityForOrgActivity.class);
                        addInt.putExtra("name", nameTextView.getText().toString());
                        addInt.putExtra("desc", descTextView.getText().toString());
                        addInt.putExtra("idOrg", orgIdTextView.getText().toString());
                        startActivity(addInt);
                    }
                });


                editOrgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editInt = new Intent(getActivity(), orgEditActivity.class);
                        editInt.putExtra("idOrg", orgIdTextView.getText().toString());
                        startActivity(editInt);
                    }
                });

            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<orgsData> mDataset) {
            this.mDataset = mDataset;
        }
        @Override
        public void onBindViewHolder(OrgViewHolder holder, int i) {
            holder.nameTextView.setText(mDataset.get(i).name);
            holder.descTextView.setText(mDataset.get(i).desc);
            holder.orgIdTextView.setText(String.valueOf(mDataset.get(i).idOrg));
        }

        @Override
        public MyAdapter.OrgViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.org_card, viewGroup, false);
            OrgViewHolder pvh = new OrgViewHolder(v);
            return pvh;
        }


        @Override
        public int getItemCount() {
           return mDataset.size();
        }
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    String response;
    private static final String url = "http://koyash.tmweb.ru/api.php";

    public class getOrgs extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "getOrgs":
                    multipartEntity.addPart("code", "getOrgs");
                    multipartEntity.addPart("userId", sPref.getString("id", "-1"));
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


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.get("success").toString().equals("1")){
                    JSONArray orgs = jsonObject.getJSONArray("orgs");
                    for (int i = 0; i < orgs.length(); i++) {
                        JSONObject organization = orgs.getJSONObject(i);
                        myOrgs.add(new orgsData(organization.get("name").toString(), organization.get("descript").toString(), organization.getInt("id")));
                    }
                }
                MyAdapter ma = new MyAdapter(myOrgs);
                recyclerView.setAdapter(ma);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }



}
