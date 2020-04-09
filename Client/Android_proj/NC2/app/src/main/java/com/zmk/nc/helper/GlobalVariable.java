/**
 * Created by: Kiet.Duong
 * September-11-2015
 **/
package com.zmk.nc.helper;

import com.zmk.nc.object.PersonObj;

import java.util.ArrayList;

public class GlobalVariable {
    public static final String CHAT_SERVER_URL = "http://10.72.34.94:3000";//http://192.168.1.74:3000";
    public static String PREFERENCES = "NC";

    public static ArrayList<String> mArrayPersonName = new ArrayList<>();
    public static ArrayList<PersonObj> mArrayPerson = new ArrayList<>();
    public static PersonObj person;


    public static final String C_2_S_LOGIN = "c-2-s-login";
    public static final String S_2_C_LOGIN = "s-2-c-login-get-contacts-list";
    public static final String S_2_C_LOGIN_USERNAME_NOT_EXISTING = "s-2-c-login-username-not-existing-in-contacts-list";


    public static final String S_2_C_UPDATE_STATUS_USER_JOINED = "s-2-c-update-status-user-joined";
    public static final String S_2_C_UPDATE_STATUS_USER_JOINED_NEW_LOGIN = "s-2-c-update-status-user-joined-newLogin";

    public static final String C_2_S_SEND_CHAT = "c-2-s-send-chat";
    public static final String S_2_C_SEND_CHAT = "s-2-c-send-chat";//response

    //for chat room
    public static final String C_2_S_CREATE_ROOM_CHAT = "c-2-s-create-room-chat";
    public static final String S_2_C_CREATE_ROOM_CHAT = "s-2-c-create-room-chat";//response

    public static final String C_2_S_INVITE_INTO_ROOM_CHAT = "c-2-s-invite-into-room-chat";
    public static final String S_2_C_INVITE_INTO_ROOM_CHAT = "s-2-c-invite-into-room-chat";

    public static final String C_2_S_ACCEPT_INVITE_JOIN_ROOM = "c-2-s-accept-invite-join-room";
    public static final String S_2_C_ACCEPT_INVITE_JOIN_ROOM = "s-2-c-accept-invite-join-room";

    public static final String C_2_S_SEND_CHAT_ROOM = "c-2-s-send-chat-room";
    public static final String S_2_C_SEND_CHAT_ROOM = "s-2-c-send-chat-room";

    public static final String C_2_S_TYPING_CHAT = "c-2-s-typing-chat";
    public static final String S_2_C_TYPING_CHAT = "s-2-c-typing-chat";

    public static final String C_2_S_CANCEL_TYPING_CHAT = "c-2-s-cancel-typing-chat";
    public static final String S_2_C_CANCEL_TYPING_CHAT = "s-2-c-cancel-typing-chat";

    public static void cancel(){
        person = null;
        mArrayPerson.clear();
        mArrayPersonName.clear();
    }
}
