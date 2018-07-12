package com.ums.wifiprobe.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class DBData implements Parcelable {

   //对应返回数据类型为String
    public final static String TRANS_NAME="transName";
    //对应返回数据类型为int
    public final static String TRANS_COUNT="transCount";
    //对应返回数据类型为String,以分为单位
    public final static String TRANS_AMOUNT="transAmount";

    protected DBData(Parcel in) {
    }

    public static final Creator<DBData> CREATOR = new Creator<DBData>() {
        @Override
        public DBData createFromParcel(Parcel in) {
            return new DBData(in);
        }

        @Override
        public DBData[] newArray(int size) {
            return new DBData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
