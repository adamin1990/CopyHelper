package com.adamin.copyhelper.android;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adamlee on 2016/5/11.
 */
public class Cnode implements Parcelable {

    private Rect rect;
    private String string;

    public Cnode(Rect rect, String string) {
        this.rect = rect;
        this.string = string;
    }

    public Rect getRect() {
        return rect;

    }


    public String getString() {
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rect, flags);
        dest.writeString(this.string);
    }

    public Cnode() {
    }

    protected Cnode(Parcel in) {
        this.rect = in.readParcelable(Rect.class.getClassLoader());
        this.string = in.readString();
    }

    public static final Parcelable.Creator<Cnode> CREATOR = new Parcelable.Creator<Cnode>() {
        @Override
        public Cnode createFromParcel(Parcel source) {
            return new Cnode(source);
        }

        @Override
        public Cnode[] newArray(int size) {
            return new Cnode[size];
        }
    };
}
