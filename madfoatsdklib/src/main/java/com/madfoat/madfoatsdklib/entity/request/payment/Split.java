package com.madfoat.madfoatsdklib.entity.request.payment;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;

public class Split  implements Parcelable {
    @Element
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Creator<Split> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);


    }

    public Split() {
    }

    protected Split(Parcel in) {
        this.id = in.readInt();

    }

    public static final Creator<Split> CREATOR = new Creator<Split>() {
        @Override
        public Split createFromParcel(Parcel source) {
            return new Split(source);
        }

        @Override
        public Split[] newArray(int size) {
            return new Split[size];
        }
    };

    @Override
    public String toString() {
        return "split{" +
                "id='" + id + '\''+
                '}';
    }
}
