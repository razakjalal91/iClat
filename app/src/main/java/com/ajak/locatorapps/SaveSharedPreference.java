package com.ajak.locatorapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ajak on 1/6/2017.
 */

public class SaveSharedPreference {
    static final String PREF_USER_NAME="username";
    static final String PREF_IC_NUMBER="icnumber";

    static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setIcNumber(Context ctx,String IcNumber){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_IC_NUMBER,IcNumber);
        editor.commit();
    }

    public static String getIcNumber(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_IC_NUMBER, "");
    }

    public static void setUserName(Context ctx,String UserName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, UserName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
}
