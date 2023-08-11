package com.madfoat.madfoatsdklib.entity.request.payment;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;


public class Paymethod implements Parcelable {
    @Element
    private String type;
    @Element
    private String cardtoken;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardtoken() {
        return cardtoken;
    }

    public void setCardtoken(String cardtoken) {
        this.cardtoken = cardtoken;
    }

    public static Creator<Paymethod> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.cardtoken);

    }

    public Paymethod() {
    }

    protected Paymethod(Parcel in) {
        this.type = in.readString();
        this.cardtoken = in.readString();


    }

    public static final Creator<Paymethod> CREATOR = new Creator<Paymethod>() {
        @Override
        public Paymethod createFromParcel(Parcel source) {
            return new Paymethod(source);
        }

        @Override
        public Paymethod[] newArray(int size) {
            return new Paymethod[size];
        }
    };

    @Override
    public String toString() {
        return "Paymethod{" +
                "type='" + type + '\'' +
                ", cardtoken='" + cardtoken + '\'' +
                '}';
    }
}
