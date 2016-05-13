package com.adamin.copyhelper.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 *
 */
public class UtilsSharedPreferences {

    private SharedPreferences preferences;
    private Editor edit;

    /**
     * 构造方法 默认sp 文件名为FQYData
     *
     * @param context
     */
    public UtilsSharedPreferences(Context context) {
        preferences = context.getSharedPreferences("CopyHelper",
                Context.MODE_PRIVATE);
        edit = preferences.edit();
    }

    public boolean IsOpen() {
        return preferences.getBoolean("serviceisOpen", false);
    }

    public void setIsOpen(boolean b) {
        edit.putBoolean("serviceisOpen", b).commit();
    }


    public void SetJifen(String info){
        edit.putString("jifen",info).commit();
    }
    public String getJifen(){
        return  preferences.getString("jifen","");
    }

}
