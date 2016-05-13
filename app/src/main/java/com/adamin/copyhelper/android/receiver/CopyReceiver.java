package com.adamin.copyhelper.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.adamin.copyhelper.android.service.CopyService;

/**
 * Created by adamlee on 2016/5/1.
 */
public class CopyReceiver extends BroadcastReceiver {
    CopyService copyService;

    public CopyReceiver(CopyService copyService) {
        this.copyService = copyService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("COPY_SHOW".equals(intent.getAction())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
         copyService.copy();
                }
            }, 300);
        }
        if ("COPY_DISABLE".equals(intent.getAction())) {
            this.copyService.cancel();
        }
        if ("COPY_ACTIVATE".equals(intent.getAction())) {
            this.copyService.noti();
        }


    }
}
