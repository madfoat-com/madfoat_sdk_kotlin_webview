package com.madfoat.madfoatsdklib.entity.request.payment;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Divya on 30/07/20.
 */
@Root(name = "Mobile", strict = false)
public class MobileRequest implements Parcelable {

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
    private Billing billing;


    @Element
    private String custref;
//    @Element
//    private Paymethod paymethod;

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

    public Billing getBilling() {
        return billing;
    }

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

    public String getCustref() {
        return custref;
    }

    public void setCustref(String custref) {
        this.custref = custref;
    }

//    public Paymethod getPaymethod() {
//        return paymethod;
//    }

//    public void setPaymethod(Paymethod paymethod) {
//        this.paymethod = paymethod;
//    }

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
        dest.writeParcelable(this.billing, flags);
        dest.writeString(this.custref);
//        dest.writeParcelable(this.paymethod,flags);
    }

    public MobileRequest() {
    }

    protected MobileRequest(Parcel in) {
        this.store = in.readString();
        this.key = in.readString();
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.app = in.readParcelable(App.class.getClassLoader());
        this.tran = in.readParcelable(Tran.class.getClassLoader());
        this.billing = in.readParcelable(Billing.class.getClassLoader());
        this.custref=in.readString();
//        this.paymethod=in.readParcelable(Paymethod.class.getClassLoader());
    }

    public static final Creator<MobileRequest> CREATOR = new Creator<MobileRequest>() {
        @Override
        public MobileRequest createFromParcel(Parcel source) {
            return new MobileRequest(source);
        }

        @Override
        public MobileRequest[] newArray(int size) {
            return new MobileRequest[size];
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


    @Override
    public String toString() {
        return "MobileRequest{" +
                "store='" + store + '\'' +
                ", key='" + key + '\'' +
                ", device=" + device +
                ", app=" + app +
                ", tran=" + tran +
                ", billing=" + billing +
                ", custref='" + custref + '\'' +
                '}';
    }


//    @Override
//    public String toString() {
//        return "MobileRequest{" +
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
}
