package com.csa.cashsasa.utility;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtil
{
    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;

    private static int getConnectivityStatus(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean getConnectionStatus(Context context)
    {
        int conn = NetworkUtil.getConnectivityStatus(context);

        if (conn == NetworkUtil.TYPE_WIFI)
        {
            return false;
        }
        else if (conn == NetworkUtil.TYPE_MOBILE)
        {
            return false;
        }
        else if (conn == NetworkUtil.TYPE_NOT_CONNECTED)
        {
            Toast.makeText(context, "Not connected to internet", Toast.LENGTH_SHORT).show();

            return true;
        }

        return false;

    }

}
