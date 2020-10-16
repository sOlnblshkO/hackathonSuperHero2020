package com.example.forfedorova.mainFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forfedorova.R;

public class profilePage extends AppCompatActivity {

    TextView nameTextView, surTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        nameTextView = findViewById(R.id.nameTextView);
        surTextView = findViewById(R.id.surTextView);

        nameTextView.setText(this.getIntent().getStringExtra("name"));
        surTextView.setText(this.getIntent().getStringExtra("sur"));


    }

}
