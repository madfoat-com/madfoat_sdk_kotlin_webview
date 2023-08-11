package com.madfoat.madfoatsdklib.entity.request.payment;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Divya on 30/07/20.
 */

@Root(strict = false)
public class Name implements Serializable {

    @Element
    private String title;
    @Element
    private String first;
    @Element
    private String last;

    public String getTitle() {
        return title;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
