package com.adamin.copyhelper.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

import com.adamin.copyhelper.android.Cnode;
import com.adamin.copyhelper.android.CopyActivity;
import com.adamin.copyhelper.android.R;

/**
 * Created by adamlee on 2016/5/11.
 */
public class CopyOverlayView extends View {

    private Rect rect;
    private String string;
    private Paint paint;
    private boolean active;


    public CopyOverlayView(Context context, Cnode cnode, CopyActivity.CopyListener copyListener) {
        super(context);
        this.active = false;
        this.rect = cnode.getRect();
        this.string = cnode.getString();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5.0f);
        setOnClickListener(new OverLayClick(copyListener,this));
    }

    public String getText() {
        return string;
    }

    public void setActive(boolean active) {
        this.active = active;
        invalidate();
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (active) {
            paint.setColor(getContext().getResources().getColor(R.color.transparentactive));
        } else {
            paint.setColor(0);
        }
        canvas.drawRect(0.0f,0.0f,(float) rect.width(),(float)rect.height(),paint);
    }

    public void addView(RelativeLayout relativeLayout,int marginHeight){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.rect.width(), this.rect.height());
        layoutParams.leftMargin = this.rect.left;
        layoutParams.topMargin = Math.max(0, this.rect.top - marginHeight);
        relativeLayout.addView(this, layoutParams);

    }

    class OverLayClick implements OnClickListener {
        CopyActivity.CopyListener copyListener = null;
        CopyOverlayView copyOverlayView = null;

        public OverLayClick(CopyActivity.CopyListener copyListener, CopyOverlayView copyOverlayView) {
            this.copyListener = copyListener;
            this.copyOverlayView = copyOverlayView;
        }

        @Override
        public void onClick(View v) {
            copyOverlayView.setActive(!copyOverlayView.active);
            copyListener.listen(copyOverlayView);


        }
    }
}
