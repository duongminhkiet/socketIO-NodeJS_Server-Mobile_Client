package com.zmk.nc.object;

import java.util.ArrayList;

/**
 * Created by Kiet on 10/04/2017.
 */

public class PersonObj extends ParentObj {
    //for Room
    private String roomId;
    private String roomName="";
    private ArrayList<String> mArrUserNames = new ArrayList<>();
    //for Person
    private String username="";
    private String password;
    private int status;//1: online, 2: take away, 3: offline
    private ArrayList<ChatObj> mArrChat = new ArrayList<>();

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<String> getmArrUserNames() {
        return mArrUserNames;
    }

    public void setmArrUserNames(ArrayList<String> mArrUserNames) {
        this.mArrUserNames = mArrUserNames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ChatObj> getmArrChat() {
        return mArrChat;
    }

    public void setmArrChat(ArrayList<ChatObj> mArrChat) {
        this.mArrChat = mArrChat;
    }
    public void addChat2Array(ChatObj chat){
        mArrChat.add(chat);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
