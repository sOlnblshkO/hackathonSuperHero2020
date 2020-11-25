package com.example.forfedorova.CustomStuff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.forfedorova.R;

public class MyCustomDialog extends LayoutInflater {

    AlertDialog dialog;
    AlertDialog.Builder builder;
    Context context;

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return null;
    }

    public MyCustomDialog(Context context){
        super(context);
        this.context = context;
    }

    public void createDialog(){
        builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.load_custom_dialog, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();

    }

    public void closeDialog(){
        dialog.cancel();
    }

}

