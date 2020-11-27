package com.example.forfedorova.schoolkid.ui.ui.visits;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import com.example.forfedorova.CustomClasses.ActivityUnit;
import com.example.forfedorova.CustomClasses.MyColors;
import com.example.forfedorova.CustomClasses.MyCustomDialog;
import com.example.forfedorova.CustomClasses.VisitUnit;
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

public class VisitsFragment extends Fragment {

    private VisitsViewModel mViewModel;

    ArrayList<VisitUnit> visits = new ArrayList<>();

    public static final String url = "http://koyash.tmweb.ru/api.php";
    public String response;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public SharedPreferences sPref;
    TextView noVisitsText;

    MyCustomDialog dialog;

    public static VisitsFragment newInstance() {
        return new VisitsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visits_fragment, container, false);
        dialog = new MyCustomDialog(getContext(), "Подождите! Идёт загрузка ваших посещений!");
        new getVisits().execute("getVisits");
        sPref = getActivity().getSharedPreferences("app" , Context.MODE_PRIVATE);
        noVisitsText = view.findViewById(R.id.noVisitsText);

        recyclerView = view.findViewById(R.id.visitsRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VisitsViewModel.class);
        // TODO: Use the ViewModel
    }


    public class visitsAdapter extends RecyclerView.Adapter<visitsAdapter.ActivitiesViewHolder> {
        private List<VisitUnit> mDataset;

        public class ActivitiesViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            String id;
            TextView nameActiv, descActiv, startDate, endDate;
            CardView cv;
            public ActivitiesViewHolder(View v) {
                super(v);
                nameActiv = v.findViewById(R.id.visitNameActText);
                descActiv = v.findViewById(R.id.visitDescActText);
                cv = v.findViewById(R.id.visitCardView);
                startDate = v.findViewById(R.id.visitStartDateText);
                endDate = v.findViewById(R.id.visitEndDateText);
            }
        }

        public visitsAdapter(List<VisitUnit> mDataset) {
            this.mDataset = mDataset;
        }
        @Override
        public void onBindViewHolder(visitsAdapter.ActivitiesViewHolder holder,final int i) {
            holder.descActiv.setText(mDataset.get(i).desc);
            holder.nameActiv.setText(mDataset.get(i).name);
            holder.startDate.setText(mDataset.get(i).startDate);
            holder.endDate.setText(mDataset.get(i).endDate);
            holder.id = (mDataset.get(i).id);
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
        public visitsAdapter.ActivitiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visit_card, viewGroup, false);
            visitsAdapter.ActivitiesViewHolder pvh = new visitsAdapter.ActivitiesViewHolder(v);
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

    public class getVisits extends AsyncTask<String, Void, Void> {

        String action;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity();
            action = params[0];
            switch (action) {
                case "getVisits":
                    multipartEntity.addPart("code", "getVisits");
                    multipartEntity.addPart("userId", sPref.getString("id", "-1"));
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
                if (jsonObject.get("success").equals("1")) {
                    noVisitsText.setVisibility(View.INVISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("visits");
                    visits = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        visits.add(new VisitUnit(jsonObject1.getString("name"),
                                jsonObject1.getString("desc"),
                                jsonObject1.getString("id"),
                                jsonObject1.getString("startDate"),
                                jsonObject1.getString("endDate")));
                    }
                    visitsAdapter va = new visitsAdapter(visits);
                    recyclerView.setAdapter(va);
                }
                else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
