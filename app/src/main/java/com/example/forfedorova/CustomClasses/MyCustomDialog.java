package com.example.forfedorova.CustomClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.example.forfedorova.R;

public class MyCustomDialog extends LayoutInflater {

    AlertDialog dialog;
    AlertDialog.Builder builder;
    Context context;
    String msg = "";

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return null;
    }

    public MyCustomDialog(Context context){
        super(context);
        this.context = context;
    }

    public MyCustomDialog(Context context, String msg){
        super(context);
        this.context = context;
        this.msg = msg;
    }

    public void createDialog(){
        builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.load_custom_dialog, null);
        builder.setView(dialogView);

        TextView loadText = dialogView.findViewById(R.id.loadText);
        if (!msg.equals("")){
            loadText.setText(msg);
        }

        dialog = builder.create();
        dialog.show();

    }

    public void closeDialog(){
        dialog.cancel();
    }

}

