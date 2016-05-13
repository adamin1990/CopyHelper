package com.adamin.copyhelper.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.DragEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.adamin.copyhelper.android.utils.AccessibillityManagerC;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.umeng.analytics.MobclickAgent;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    SwitchCompat switchCompat;
    private AlertDialog dialog;
    NotificationCompat.Builder builder ;
private AppCompatButton btn_qq,btn_weibo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_qq= (AppCompatButton) findViewById(R.id.btn_qq);
        btn_weibo= (AppCompatButton) findViewById(R.id.btn_weibo);
        switchCompat= (SwitchCompat) findViewById(R.id.switchcompact);
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("点击复制")
                .setContentText("复制你想要的内容").setAutoCancel(false)
                .setOngoing(true)
                .setShowWhen(true);
        builder.setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent("COPY_SHOW"), PendingIntent.FLAG_CANCEL_CURRENT));
        dialog=new AlertDialog.Builder(MainActivity.this).setTitle("开启复制服务")
                .setMessage("请确保“复制助手”辅助服务开启，\n点击确定到辅助功能内开启“复制助手”服务")
                .setCancelable(false)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                        finish();
                    }
                }).create();

        if(AccessibillityManagerC.isAccessibilitySettingsOn(getApplicationContext())){
            switchCompat.setChecked(true);
            switchCompat.setText("关闭复制功能");

        }else {
            switchCompat.setChecked(false);
            switchCompat.setText("开启复制功能");

        }


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    if(!AccessibillityManagerC.isAccessibilitySettingsOn(getApplicationContext())){
                      dialog.show();
                    }else {
                        switchCompat.setText("关闭复制功能");
                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,builder.build());
                    }
                }else {

                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);

                    switchCompat.setText("开启复制功能");
                }
            }
        });
        btn_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://m.weibo.cn/u/3132916047?from=1063195010&wm=4260_0001&sourceType=qq&uid=3132916047"));
                startActivity(intent);
            }
        });
        btn_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="mqqwpa://im/chat?chat_type=wpa&uin=14846869";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("adamin1990", "CopyHelper")
                .setIcon(R.mipmap.ic_launcher)
                .setDialogTitleWhenUpdateAvailable("版本更新了~")
                .setDialogButtonDoNotShowAgain("")
                .setDialogDescriptionWhenUpdateAvailable("复制助手又更新啦！\n赶紧下载最新版本的App吧~")
                .setDialogButtonUpdate("立即更新")
                .start();
        }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if(AccessibillityManagerC.isAccessibilitySettingsOn(getApplicationContext())){
            switchCompat.setChecked(true);
            switchCompat.setText("关闭复制功能");
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,builder.build());

        }else {
            switchCompat.setChecked(false);
            switchCompat.setText("开启复制功能");
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
