package com.example.forfedorova.superAdmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.forfedorova.LogAndReg.loginActivity;
import com.example.forfedorova.schoolkid.ui.schoolKidMenuActivity;
import com.example.forfedorova.superAdmin.ui.profile.organizations.superAdminOrgs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.forfedorova.R;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class superAdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        Toolbar toolbar = findViewById(R.id.toolbarSuper);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_super);
        NavigationView navigationView = findViewById(R.id.nav_view_super_admin);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_super_admin_prof, R.id.nav_super_admin_orgs, R.id.nav_change_rank)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_super);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sPref = getSharedPreferences("app", MODE_PRIVATE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_super);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void exitFromSuperAdmin(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(superAdminActivity.this);
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
