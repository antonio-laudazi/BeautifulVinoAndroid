package com.marte5.beautifulvino.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;

public class Utility {
    public static int getHeightScreen(Activity ac){
        DisplayMetrics metrics = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static Point getScreenSize(Activity ac){
        Display display = ac.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
       return size;
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
