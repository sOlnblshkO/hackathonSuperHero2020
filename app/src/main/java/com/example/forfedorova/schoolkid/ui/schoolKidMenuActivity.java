package com.example.forfedorova.schoolkid.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.forfedorova.LogAndReg.loginActivity;
import com.example.forfedorova.R;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class schoolKidMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sPref;
    DrawerLayout drawer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        final Toolbar toolbar = findViewById(R.id.toolbarSchool);
        setSupportActionBar(toolbar);

        sPref = getSharedPreferences("app", MODE_PRIVATE);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_my_visits)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener(){
        @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

            }
        });

    }


    public void exit(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(schoolKidMenuActivity.this);
        builder.setTitle("Выход");
        builder.setMessage("Вы хотите выйти из учетной записи?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sPref.edit().clear().apply();
                Intent login = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(login);
                finish();
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
