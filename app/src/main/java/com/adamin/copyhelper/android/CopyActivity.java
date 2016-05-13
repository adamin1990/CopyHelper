package com.adamin.copyhelper.android;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adamin.copyhelper.android.utils.CopyManager;
import com.adamin.copyhelper.android.view.CopyOverlayView;

import java.util.ArrayList;
import java.util.List;

public class CopyActivity extends AppCompatActivity implements ActionMode.Callback {
    private List<Cnode> cnodes;
    private List<CopyOverlayView> copyOverlayViews;
    private RelativeLayout relativeLayout;
    private int statusBarHeight;
    private CopyListener copyListener;

    private ActionMode actionMode;
    private boolean m;

    private FloatingActionButton fabcopy; //复制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        copyListener = new CopyImpl(this);
        copyOverlayViews = new ArrayList<>();
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        cnodes = new ArrayList<>();
        cnodes = getIntent().getParcelableArrayListExtra("copy_nodes");
        statusBarHeight = getStatusBarHeight();
        for (Cnode cnode : cnodes) {
            Log.e("字符串", "字符串是" + cnode.getString());
            addNode(cnode, statusBarHeight);
        }
        fabcopy = (FloatingActionButton) findViewById(R.id.fab);

        fabcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copyOverlayViews.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < copyOverlayViews.size(); i++) {
                        stringBuilder.append(((CopyOverlayView) copyOverlayViews.get(i)).getText());
                        if (i + 1 < copyOverlayViews.size()) {
                            stringBuilder.append("\n");
                        }
                    }
                    CopyManager.copy(CopyActivity.this, stringBuilder.toString());
                    Toast.makeText(CopyActivity.this, "成功复制~", Toast.LENGTH_SHORT).show();
                    onBackPressed();


                } else {
                    Toast.makeText(CopyActivity.this, "没有要复制的内容~", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addNode(Cnode cnode, int statusBarHeight) {
        new CopyOverlayView(this, cnode, copyListener).addView(relativeLayout, statusBarHeight);
    }

    private int getStatusBarHeight() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }
        return (int) Math.ceil((double) (25.0f * getResources().getDisplayMetrics().density));
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public interface CopyListener {
        void listen(CopyOverlayView copyOverlayView);
    }

    class CopyImpl implements CopyListener {
        CopyActivity copyActivity;

        public CopyImpl(CopyActivity copyActivity) {
            this.copyActivity = copyActivity;
        }

        @Override
        public void listen(CopyOverlayView copyOverlayView) {
            if (copyOverlayView.isActive()) {
                copyActivity.copyOverlayViews.add(copyOverlayView);
                if (copyActivity.actionMode == null) {
                    copyActivity.actionMode = copyActivity.startActionMode(copyActivity);
                    return;
                }
                return;
            }
            copyActivity.copyOverlayViews.remove(copyOverlayView);
            if (copyActivity.copyOverlayViews.size() < 0 && copyActivity.actionMode != null) {
                copyActivity.m = true;
                copyActivity.actionMode.finish();
            }
        }
    }
}
