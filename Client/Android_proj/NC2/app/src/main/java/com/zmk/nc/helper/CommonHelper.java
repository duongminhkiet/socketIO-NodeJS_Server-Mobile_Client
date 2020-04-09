/**
 * Created by: Kiet.Duong
 * September-11-2015
 **/
package com.zmk.nc.helper;

//import java.util.EnumMap;
//import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.zmk.nc.activity.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonHelper {

    public static void showWarning(Context context, String str_message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(str_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false).setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
        TextView messageView = (TextView) alert
                .findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkValidString(String strx) {
        if (strx == null) {
            return false;
        } else {
            String str = strx.trim();
            return ((!str.equalsIgnoreCase("")) && (!str.isEmpty())
                    && (!str.equalsIgnoreCase("null")) && (!str
                    .equalsIgnoreCase("NULL")));
        }
    }

    public static Date getDateNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String date = df.format(c.getTime());
        return getDateFromString(date, "");
    }

    public static Date getDateFromString(String dateStr, String formatType) {
        DateFormat df;
        if (CommonHelper.checkValidString(formatType)) {
            df = new SimpleDateFormat(formatType);
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }

        Date date = new Date();
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimeNow() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    public static boolean isContainString(String str1, String str2) {
        if (str1.indexOf(str2) != -1) {
            return true;
        } else {
            return false;
        }
    }
}
