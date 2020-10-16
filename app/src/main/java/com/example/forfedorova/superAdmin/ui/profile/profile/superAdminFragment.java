package com.example.forfedorova.superAdmin.ui.profile.profile;

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

import static android.content.Context.MODE_PRIVATE;

public class superAdminFragment extends Fragment {

    private SuperAdminViewModel mViewModel;

    SharedPreferences sPref;
    TextView name, sur, rank;
    public static superAdminFragment newInstance() {
        return new superAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.super_admin_fragment, container, false);

        sPref = getActivity().getSharedPreferences("app", MODE_PRIVATE);

        name = view.findViewById(R.id.superAdmNameText);
        sur = view.findViewById(R.id.superAdmSurText);
        rank = view.findViewById(R.id.superAdmRankText);

        name.setText(sPref.getString("name", "noName"));
        sur.setText(sPref.getString("sur", "noSur"));
        rank.setText("Вы являетесь супер админом!");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SuperAdminViewModel.class);
        // TODO: Use the ViewModel
    }

}
