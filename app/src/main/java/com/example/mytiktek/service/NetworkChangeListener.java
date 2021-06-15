package com.example.mytiktek.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.mytiktek.R;

public class NetworkChangeListener  extends BroadcastReceiver {
    AlertDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!NetworkHelper.isConnectedToInternet(context)){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("Error");
            dialogBuilder.setMessage("There is no Internet connection");
            dialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onReceive(context, intent);

                }
            });

         //   dialogBuilder.setNegativeButton(android.R.string.no, null);
            dialogBuilder.setIcon(R.drawable.ic_error_outline_24);

            dialog = dialogBuilder.create();
            dialog.show();
            dialog.setCancelable(false);



        }
    }
}
