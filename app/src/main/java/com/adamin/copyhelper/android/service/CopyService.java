package com.adamin.copyhelper.android.service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.adamin.copyhelper.android.Cnode;
import com.adamin.copyhelper.android.CopyActivity;
import com.adamin.copyhelper.android.R;
import com.adamin.copyhelper.android.receiver.CopyReceiver;

import java.util.ArrayList;

/**
 * Created by adamlee on 2016/5/1.
 */
public class CopyService extends AccessibilityService {

    Rect rect;
    CopyReceiver copyReceiver;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        copyReceiver = new CopyReceiver(this);
        IntentFilter intentFilter = new IntentFilter("COPY_SHOW");
        intentFilter.addAction("COPY_DISABLE");
        intentFilter.addAction("COPY_ACTIVATE");
        registerReceiver(copyReceiver, intentFilter);
        noti();

    }

    public void cancel() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
    }

    public void noti() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("点击复制")
                .setContentText("复制你想要的内容").setAutoCancel(false)
                .setOngoing(true)
                .setShowWhen(true);
        builder.setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent("COPY_SHOW"), PendingIntent.FLAG_CANCEL_CURRENT));
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, builder.build());

    }


    public void copy() {
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            ArrayList<Cnode> cnodes = getCnode(rootInActiveWindow);
            if (cnodes.size() > 0) {
                Intent intent = new Intent(this, CopyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putParcelableArrayListExtra("copy_nodes", cnodes);
                startActivity(intent, ActivityOptions.makeCustomAnimation(getBaseContext(), R.anim.abc_fade_in, R.anim.abc_fade_out).toBundle());

            } else {
                Toast.makeText(this, "没有需要拷贝的内容呀", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList getCnode(AccessibilityNodeInfo accessibilityNodeInfo) {
        ArrayList<Cnode> arrayList = new ArrayList();
        if (accessibilityNodeInfo == null) {
            return arrayList;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            arrayList.addAll(getCnode(accessibilityNodeInfo.getChild(i)));
        }
        if (accessibilityNodeInfo.getContentDescription() != null) {
            rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            arrayList.add(new Cnode(rect, accessibilityNodeInfo.getContentDescription().toString()));
        }
        if (accessibilityNodeInfo.getText() != null) {
            rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            arrayList.add(new Cnode(rect, accessibilityNodeInfo.getText().toString()));
        }
        return arrayList;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(copyReceiver);
        super.onDestroy();
    }
}
