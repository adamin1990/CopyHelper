package com.adamin.copyhelper.android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by adamlee on 2016/5/12.
 */
public class CopyManager {
    /**
     * 复制内容
     * @param context
     * @param str
     */
    public static void copy(Context context, String str) {
        if (str != null) {
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(null, str));
        }
    }

}
