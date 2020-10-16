package com.example.forfedorova.administrator.ui.profile;

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

public class adminProf extends Fragment {

    private AdminProfViewModel mViewModel;

    SharedPreferences sPref;

    TextView name, sur, rank;

    public static adminProf newInstance() {
        return new adminProf();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_prof_fragment, container, false);

        sPref = this.getActivity().getSharedPreferences("app", MODE_PRIVATE);

        name = view.findViewById(R.id.adminProfName);
        sur = view.findViewById(R.id.adminProfSurTextView);
        rank = view.findViewById(R.id.adminProfRankTextView);

        name.setText(sPref.getString("name", "noName"));
        sur.setText(sPref.getString("sur", "noSur"));
        rank.setText("Вы являетесь администратором!");

        this.getActivity();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminProfViewModel.class);
        // TODO: Use the ViewModel
    }

}
