package com.madfoat.madfoatsdklib.entity.request.payment;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;

public class Repeat implements Parcelable
{

    public static final Creator<Repeat> CREATOR = new Creator<Repeat>()
    {
        @Override
        public Repeat createFromParcel(Parcel source)
        {
            return new Repeat(source);
        }

        @Override
        public Repeat[] newArray(int size)
        {
            return new Repeat[size];
        }
    };
    @Element
    private String amount;
    @Element
    private String interval;
    @Element
    private String period;
    @Element
    private String term;
    @Element
    private String finals;
    @Element
    private String start;




    public Repeat()
    {
    }

    protected Repeat(Parcel in)
    {
        this.amount = in.readString();
        this.interval = in.readString();
        this.period = in.readString();
        this.term = in.readString();
        this.finals = in.readString();
        this.start = in.readString();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getFinals() {
        return finals;
    }

    public void setFinals(String finals) {
        this.finals = finals;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.amount);
        dest.writeString(this.interval);
        dest.writeString(this.period);
        dest.writeString(this.term);
        dest.writeString(this.finals);
        dest.writeString(this.start);

    }

    @Override
    public String toString() {
        return "Repeat{" +
                "amount='" + amount + '\'' +
                ", interval='" + interval + '\'' +
                ", period='" + period + '\'' +
                ", term='" + term + '\'' +
                ", final='" + finals + '\'' +
                ", start='" + start + '\'' +
                '}';
    }
}
