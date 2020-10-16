package com.example.forfedorova.schoolkid.ui.profile;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

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

import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    SharedPreferences sPref;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        sPref = this.getActivity().getSharedPreferences("app", MODE_PRIVATE);

        reinicialize(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

    }

    public void reinicialize(View view){
        TextView name = view.findViewById(R.id.profNameTextView);
        name.setText(sPref.getString("name", ""));
        TextView sur = view.findViewById(R.id.profSurTextView);
        sur.setText(sPref.getString("sur", ""));
        TextView login = view.findViewById(R.id.profLoginTextView);
        login.setText("Вы являетесь школьником. Ваш id:");
        TextView id = view.findViewById(R.id.profIdTextView);
        id.setText(sPref.getString("id", "noID"));
    }

}
