package com.example.forfedorova.representative;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.forfedorova.LogAndReg.loginActivity;
import com.example.forfedorova.R;
import com.example.forfedorova.superAdmin.superAdminActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class repActivitiy extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep);
        Toolbar toolbar = findViewById(R.id.toolbarRep);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_rep);
        NavigationView navigationView = findViewById(R.id.nav_view_rep);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rep_profile, R.id.nav_rep_activities)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sPref = getSharedPreferences("app", MODE_PRIVATE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void exitFromRep(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(repActivitiy.this);
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

}
