package com.example.forfedorova.administrator.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forfedorova.MultipartEntity;
import com.example.forfedorova.R;
import com.example.forfedorova.mainFiles.addVisitorsActivity;

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

public class activitiesFragment extends Fragment {

    private ActivitiesViewModel mViewModel;

    TextView emptyOrgs;
    ArrayList<activityClass> myActivities = new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String activityId = "-1";

    View v;

    public static activitiesFragment newInstance() {
        return new activitiesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activities_fragment, container, false);

        emptyOrgs = v.findViewById(R.id.textView7);
        recyclerView = v.findViewById(R.id.adminActivRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        sPref = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        new getActivites().execute("getActivByAdmin");

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ActivitiesViewModel.class);
        // TODO: Use the ViewModel
    }

    public class activityClass {
        String name, desc, adminLogin, preLogin;
        int id;

        public activityClass(String name, String desc, int id, String adminLogin, String preLogin) {
            this.name = name;
            this.desc = desc;
            this.id = id;
            this.adminLogin = adminLogin;
            this.preLogin = preLogin;
        }

    }


    public void loadActivities() {
        activitiesAdapter ma = new activitiesAdapter(myActivities);
        recyclerView.setAdapter(ma);
    }




    public class activitiesAdapter extends RecyclerView.Adapter<activitiesAdapter.ActivitiesViewHolder> {
        private List<activityClass> mDataset;


        public class ActivitiesViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView nameTextView, descTextView, activIdTextView1, activAdminText1, activPreText1;
            public Button addBtn;
            ImageButton mImageButton;
            CardView cv;
            public ActivitiesViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.activityCard);

                Random rnd = new Random();
                ArrayList<Integer> colors = new ArrayList<>();
                int colorRed = Color.argb(255, 255, 128, 0);
                int colorGreen = Color.argb(255, 0, 255, 0);
                int colorBlue = Color.argb(255, 66, 145, 255);
                colors.add(colorRed);
                colors.add(colorGreen);
                colors.add(colorBlue);

                cv.setBackgroundColor(colors.get(rnd.nextInt(3)));

                nameTextView = v.findViewById(R.id.nameOfActivText);
                descTextView = v.findViewById(R.id.descOfActivText);
                activIdTextView1 = v.findViewById(R.id.activIdText);
                activAdminText1 = v.findViewById(R.id.activAdminText);
                activPreText1 = v.findViewById(R.id.activPreText);
                addBtn = v.findViewById(R.id.addPartBtn);

                addBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent addVisIntent = new Intent(getActivity(), addVisitorsActivity.class);
                        addVisIntent.putExtra("idActiv", activIdTextView1.getText().toString());
                        startActivity(addVisIntent);
                    }
                });

                mImageButton = v.findViewById(R.id.optImage);


            }
        }

        public activitiesAdapter(List<activityClass> mDataset) {
            this.mDataset = mDataset;
        }


        public void onBindViewHolder(final activitiesAdapter.ActivitiesViewHolder holder, final int i) {
            holder.nameTextView.setText(mDataset.get(i).name);
            holder.descTextView.setText(mDataset.get(i).desc);
            holder.activPreText1.setText("Представитель " + mDataset.get(i).preLogin);
            holder.activAdminText1.setText("Администратор " + mDataset.get(i).adminLogin);
            holder.activIdTextView1.setText(String.valueOf(mDataset.get(i).id));
            holder.mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.inflate(R.menu.menu_for_activity);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.optStat:
                                    activityId = holder.activIdTextView1.getText().toString();
                                    new getActivites().execute("getStat");
                                    Toast.makeText(getContext(), "Статистика", Toast.LENGTH_SHORT).show();
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        @Override
        public activitiesAdapter.ActivitiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activities_card, viewGroup, false);
            activitiesAdapter.ActivitiesViewHolder pvh = new activitiesAdapter.ActivitiesViewHolder(v);
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
    SharedPreferences sPref;


    public class getActivites extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "getActivByAdmin":

                    multipartEntity.addPart("code", "getActivByAdmin");
                    multipartEntity.addPart("idAdmin", sPref.getString("id", "-1").toString());
                    httpPost.setEntity(multipartEntity);
                    break;
                case "getStat":
                    multipartEntity.addPart("code", "getStat");
                    multipartEntity.addPart("idActiv", activityId);
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


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
            switch (action) {
                case "getActivByAdmin":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            emptyOrgs.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("activities");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject activity = jsonArray.getJSONObject(i);
                                myActivities.add(new activityClass(activity.getString("name"),
                                        activity.getString("desc"),
                                        Integer.valueOf(activity.getString("id")),
                                        activity.getString("logAdmin"),
                                        activity.getString("logRep")));
                            }
                            loadActivities();
                        } else {
                            emptyOrgs.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "getStat":
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Статистика");
                            builder.setMessage("Количество участников = " + jsonObject.getString("count"));
                            builder.setPositiveButton("Закрыть",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }


    }

}
