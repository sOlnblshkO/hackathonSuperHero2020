package com.example.forfedorova.mainFiles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.forfedorova.CustomClasses.ActivityUnit;
import com.example.forfedorova.CustomClasses.MyColors;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class orgEditActivity extends AppCompatActivity {

    TextView emptyOrgs;
    ArrayList<ActivityUnit> myActivities = new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String activityId = "-1";
    Button adminRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_edit);
        emptyOrgs = findViewById(R.id.emptyOrgsText);
        recyclerView = findViewById(R.id.orgsEditRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adminRep = findViewById(R.id.adminRepBtn);

        setTitle("Мероприятия в " + getIntent().getStringExtra("nameOrg"));

        adminRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminRep = new Intent(getApplicationContext(), adminRep.class);
                adminRep.putExtra("idOrg", getIntent().getStringExtra("idOrg"));
                startActivity(adminRep);
            }
        });

        new getActivites().execute("getActiv");

    }




    public void loadActivities() {
        activitiesAdapter ma = new activitiesAdapter(myActivities);
        recyclerView.setAdapter(ma);
    }

    public class activitiesAdapter extends RecyclerView.Adapter<activitiesAdapter.ActivitiesViewHolder> {
        private List<ActivityUnit> mDataset;

        public class ActivitiesViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView nameTextView, descTextView, activAdminText1, activPreText1, startDateText, endDateText;
            public Button addBtn;
            CardView cv;
            ImageButton optBtn;
            public ActivitiesViewHolder(View v) {
                super(v);
                cv = v.findViewById(R.id.activityCard);

                nameTextView = v.findViewById(R.id.nameOfActivText);
                descTextView = v.findViewById(R.id.descOfActivText);
                activAdminText1 = v.findViewById(R.id.activAdminText);
                activPreText1 = v.findViewById(R.id.activPreText);
                addBtn = v.findViewById(R.id.addPartBtn);
                optBtn = v.findViewById(R.id.optImage);
                startDateText = v.findViewById(R.id.startDateText);
                endDateText = v.findViewById(R.id.endDateText);




            }
        }

        public activitiesAdapter(List<ActivityUnit> mDataset) {
            this.mDataset = mDataset;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final ActivitiesViewHolder holder, final int i) {
            holder.nameTextView.setText(mDataset.get(i).name);
            holder.descTextView.setText(mDataset.get(i).desc);
            holder.activPreText1.setText("Представитель " + mDataset.get(i).preLogin);
            holder.activAdminText1.setText("Администратор " + mDataset.get(i).adminLogin);
            holder.startDateText.setText(mDataset.get(i).startDate);
            holder.endDateText.setText(mDataset.get(i).endDate);
            holder.optBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(orgEditActivity.this, v);
                    popupMenu.inflate(R.menu.menu_for_activity);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.optStat:
                                    activityId = String.valueOf(mDataset.get(i).id);
                                    new getActivites().execute("getStat");
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            holder.addBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent addVisIntent = new Intent(getApplicationContext(), addVisitorsActivity.class);
                    addVisIntent.putExtra("idActiv", String.valueOf(mDataset.get(i).id));
                    startActivity(addVisIntent);
                }
            });

            try {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(mDataset.get(i).startDate);
                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(mDataset.get(i).endDate);
                Date dateNow = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDate.now()));
                MyColors myColors = new MyColors();
                if (dateNow.compareTo(date1) < 0) {
                    holder.cv.setBackgroundColor(myColors.colorBlue);
                } else if (dateNow.compareTo(date2) > 0) {
                    holder.cv.setBackgroundColor(myColors.colorRed);
                } else {
                    holder.cv.setBackgroundColor(myColors.colorGreen);
                }
            } catch (Exception e){}
        }

        @Override
        public ActivitiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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
                case "getActiv":

                    multipartEntity.addPart("code", "getActiv");
                    multipartEntity.addPart("idOrg", getIntent().getStringExtra("idOrg"));
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
            switch (action) {
                case "getActiv":
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("1")) {
                            emptyOrgs.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("activities");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject activity = jsonArray.getJSONObject(i);
                                myActivities.add(new ActivityUnit(activity.getString("name"),
                                        activity.getString("desc"),
                                        Integer.valueOf(activity.getString("id")),
                                        activity.getString("logAdmin"),
                                        activity.getString("logRep"),
                                        activity.getString("startDate"),
                                        activity.getString("endDate")));
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(orgEditActivity.this);
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
