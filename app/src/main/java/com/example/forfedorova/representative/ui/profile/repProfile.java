package com.example.forfedorova.representative.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forfedorova.R;

public class repProfile extends Fragment {

    private RepProfileViewModel mViewModel;

    public static repProfile newInstance() {
        return new repProfile();
    }
    TextView name, rank, sur;
    SharedPreferences sPref;
    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rep_profile_fragment, container, false);

        sPref = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        name = view.findViewById(R.id.repNameText);
        rank = view.findViewById(R.id.repRankText);
        sur = view.findViewById(R.id.repSurText);

        name.setText(sPref.getString("name", "noName"));
        sur.setText(sPref.getString("sur", "noSur"));
        rank.setText("Вы являетесь представителем!");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RepProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
