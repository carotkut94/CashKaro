package com.death.cashkaro;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by deathcode on 15/04/17.
 */

public class NetworkUtils {
    public static boolean isConnected(Context context) {

        ConnectivityManager cm=(ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo[]=cm.getAllNetworkInfo();

        int i;

        for(i=0;i<networkInfo.length;++i){
            if(networkInfo[i].getState()==NetworkInfo.State.CONNECTED){
                Toast.makeText(context,"Internet Connected",Toast.LENGTH_LONG).show();
                return  true;
            }
        }

        if(i==networkInfo.length){
            Toast.makeText(context,"Internet Not Connected",Toast.LENGTH_LONG).show();
            return false;
        }
        return  false;
    }


    public static int dpToPx(Context context,  int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
