package com.madfoat.madfoatsdklib.entity.request.payment;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Mobile", strict = false)
public class MobileRequestSeamless implements Parcelable {

    @Element
    private String store;
    @Element
    private String key;
    @Element
    private Device device;
    @Element
    private App app;
    @Element
    private Tran tran;
    @Element
    private Split split;
    @Element
    private Billing billing;
    @Element
    private Repeat repeat;
    @Element
    private String custref;
    @Element
    private Paymethod paymethod;

    public String getStore() {
        return store;
    }

    public String getKey() {
        return key;
    }

    public Device getDevice() {
        return device;
    }

    public App getApp() {
        return app;
    }

    public Tran getTran() {
        return tran;
    }

    public Split getSplit() {
        return split;
    }

    public void setSplit(Split split) {
        this.split = split;
    }

    public Billing getBilling() {
        return billing;
    }

    public Repeat getRepeat() {return  repeat;}

    public void setStore(String store) {
        this.store = store;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setTran(Tran tran) {
        this.tran = tran;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public void  setRepeat(Repeat repeat) {this.repeat = repeat;}

    public String getCustref() {
        return custref;
    }

    public void setCustref(String custref) {
        this.custref = custref;
    }

    public Paymethod getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(Paymethod paymethod) {
        this.paymethod = paymethod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.store);
        dest.writeString(this.key);
        dest.writeParcelable(this.device, flags);
        dest.writeParcelable(this.app, flags);
        dest.writeParcelable(this.tran, flags);
        dest.writeParcelable(this.split,flags);
        dest.writeParcelable(this.billing, flags);
        dest.writeParcelable(this.repeat,flags);
        dest.writeString(this.custref);
        dest.writeParcelable(this.paymethod,flags);
    }

    public MobileRequestSeamless() {
    }

    protected MobileRequestSeamless(Parcel in) {
        this.store = in.readString();
        this.key = in.readString();
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.app = in.readParcelable(App.class.getClassLoader());
        this.tran = in.readParcelable(Tran.class.getClassLoader());
        this.split = in.readParcelable(Split.class.getClassLoader());
        this.billing = in.readParcelable(Billing.class.getClassLoader());
        this.repeat = in.readParcelable(Repeat.class.getClassLoader());
        this.custref=in.readString();
        this.paymethod=in.readParcelable(Paymethod.class.getClassLoader());
    }

    public static final Creator<MobileRequestSeamless> CREATOR = new Creator<MobileRequestSeamless>() {
        @Override
        public MobileRequestSeamless createFromParcel(Parcel source) {
            return new MobileRequestSeamless(source);
        }

        @Override
        public MobileRequestSeamless[] newArray(int size) {
            return new MobileRequestSeamless[size];
        }
    };

//    @Override
//    public String toString()
//    {
//        return "MobileRequest{" +
//                "store='" + store + '\'' +
//                ", key='" + key + '\'' +
//                ", device=" + device.toString() +
//                ", app=" + app.toString() +
//                ", tran=" + tran.toString() +
//                ", billing=" + billing.toString() +
//                '}';
//    }





//    @Override
//    public String toString() {
//        return "MobileRequestSeamless{" +
//                "store='" + store + '\'' +
//                ", key='" + key + '\'' +
//                ", device=" + device +
//                ", app=" + app +
//                ", tran=" + tran +
//                ", billing=" + billing +
//                ", custref='" + custref + '\'' +
//                ", paymethod=" + paymethod +
//                '}';
//    }


    @Override
    public String toString() {
        return "MobileRequestSeamless{" +
                "store='" + store + '\'' +
                ", key='" + key + '\'' +
                ", device=" + device +
                ", app=" + app +
                ", tran=" + tran +
                ", split="+split +
                ", billing=" + billing +
                ", repeat=" + repeat +
                ", custref='" + custref + '\'' +
                ", paymethod=" + paymethod +
                '}';
    }
}
