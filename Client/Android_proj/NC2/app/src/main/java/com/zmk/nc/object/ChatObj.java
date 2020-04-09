package com.zmk.nc.object;

/**
 * Created by Kiet on 10/04/2017.
 */

public class ChatObj extends ParentObj {
    private String date;
    private String content;
    private boolean isMe;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
