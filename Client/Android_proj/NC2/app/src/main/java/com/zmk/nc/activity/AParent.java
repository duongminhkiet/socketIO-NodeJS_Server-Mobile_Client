package com.zmk.nc.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.zmk.nc.adapter.PersonAdapter;
import com.zmk.nc.application.NCApplication;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;
import com.zmk.nc.interfaceX.ITF_notificationChat;
import com.zmk.nc.interfaceX.ITF_notificationConnectNS;
import com.zmk.nc.interfaceX.ITF_typingChat;
import com.zmk.nc.object.ChatObj;
import com.zmk.nc.object.PersonObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public abstract class AParent extends FragmentActivity implements OnClickListener {
    protected ListView id_lv_person;
    public PersonAdapter adapter;

    public String userName;
    public String passWord;

    public ITF_notificationConnectNS itfNotificationConnectNS;

    public ITF_notificationChat itfNotificationChatOne,itfNotificationChatRoom;
    public ITF_typingChat itfTypingChatOne, itfTypingChatRoom;
    public String friendTyping = "";

    public Socket mSocket;

    private Boolean isConnected = true;
    public String TAG = "abc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NCApplication app = (NCApplication)getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(GlobalVariable.S_2_C_UPDATE_STATUS_USER_JOINED, onUpdateStatusUserJoined);
        mSocket.on(GlobalVariable.S_2_C_UPDATE_STATUS_USER_JOINED_NEW_LOGIN, onUpdateStatusUserJoinedNewLogined);
        mSocket.on(GlobalVariable.S_2_C_SEND_CHAT, onGetChat);
        mSocket.on(GlobalVariable.S_2_C_CREATE_ROOM_CHAT, onCreateRoomChat);
        mSocket.on(GlobalVariable.S_2_C_INVITE_INTO_ROOM_CHAT,onInviteIntoRoomChat);
        mSocket.on(GlobalVariable.S_2_C_ACCEPT_INVITE_JOIN_ROOM,onAcceptInviteIntoRoomChat);
        mSocket.on(GlobalVariable.S_2_C_SEND_CHAT_ROOM, onGetChatRoom);
        mSocket.on(GlobalVariable.S_2_C_TYPING_CHAT, onTypingChatOne);
        mSocket.on(GlobalVariable.S_2_C_CANCEL_TYPING_CHAT, onCancelTypingChatOne);
        mSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(GlobalVariable.S_2_C_UPDATE_STATUS_USER_JOINED, onUpdateStatusUserJoined);
        mSocket.off(GlobalVariable.S_2_C_UPDATE_STATUS_USER_JOINED_NEW_LOGIN, onUpdateStatusUserJoinedNewLogined);
        mSocket.off(GlobalVariable.S_2_C_SEND_CHAT, onGetChat);
        mSocket.off(GlobalVariable.S_2_C_CREATE_ROOM_CHAT, onCreateRoomChat);
        mSocket.off(GlobalVariable.S_2_C_INVITE_INTO_ROOM_CHAT,onInviteIntoRoomChat);
        mSocket.off(GlobalVariable.S_2_C_ACCEPT_INVITE_JOIN_ROOM,onAcceptInviteIntoRoomChat);
        mSocket.off(GlobalVariable.S_2_C_SEND_CHAT_ROOM, onGetChatRoom);
        mSocket.off(GlobalVariable.S_2_C_TYPING_CHAT, onTypingChatOne);
        mSocket.off(GlobalVariable.S_2_C_CANCEL_TYPING_CHAT, onCancelTypingChatOne);
    }
    private Emitter.Listener onCancelTypingChatOne = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    Log.d(TAG, data.toString());
                    try {
                        String friendName = data.getString("friendName");
                        //CommonHelper.showToast(getApplicationContext(),"cancel typing");
                        friendTyping = "";
                        if(GlobalVariable.person != null && itfTypingChatOne != null){
                            itfTypingChatOne.cancelTyping(friendName);
                        }

                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onTypingChatOne = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    Log.d(TAG, data.toString());
                    try {
                        String friendName = data.getString("friendName");

                        friendTyping = friendName;
                        if(GlobalVariable.person != null && itfTypingChatOne != null){
                            itfTypingChatOne.typing(friendName);
                        }

                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onGetChatRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    Log.d(TAG, data.toString());
                    try {
                        String username = data.getString("username");
                        String message = data.getString("message");
                        String time = data.getString("time");
                        String roomName = data.getString("roomName");

                        ChatObj chat = new ChatObj();
                        chat.setContent(username+" : "+message);
                        chat.setDate(time);
                        chat.setMe(false);


                        if(GlobalVariable.person != null && itfNotificationChatRoom != null && (GlobalVariable.person.getRoomName().equalsIgnoreCase(roomName))){
                            itfNotificationChatRoom.notificationChat(chat);
                        }else{
                            int i = GlobalVariable.mArrayPersonName.indexOf(roomName);
                            if(i != -1){
                                GlobalVariable.mArrayPerson.get(i).getmArrChat().add(chat);
                            }
                            //CommonHelper.showToast(getApplicationContext(),"i: "+i+ " + "+username+" : "+message);

                        }
                        adapter.refresh(GlobalVariable.mArrayPerson);

                        //CommonHelper.showToast(getApplicationContext(), data.toString());
                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onAcceptInviteIntoRoomChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String roomId = data.getString("roomId");
                        final String roomName = data.getString("roomName");
                        JSONArray friendNames = data.getJSONArray("friendNames");
                        if(GlobalVariable.mArrayPersonName.contains(roomName)){
                            //doi voi thang di moi -> da co room roi thi chi cap nhat friend name
                            PersonObj p = GlobalVariable.mArrayPerson.get(GlobalVariable.mArrayPersonName.indexOf(roomName));
                            for(int i = 0 ; i < friendNames.length(); i++){
                                String fName = friendNames.get(i).toString();
                                if(!p.getmArrUserNames().contains(fName)){
                                    p.getmArrUserNames().add(fName);
                                }
                            }
                        }else{
                            PersonObj p = new PersonObj();
                            p.setRoomId(roomId);
                            p.setRoomName(roomName);
                            for(int i = 0 ; i < friendNames.length(); i++){
                                String fName = friendNames.get(i).toString();
                                if(!p.getmArrUserNames().contains(fName)){
                                    p.getmArrUserNames().add(fName);
                                }
                            }
                            GlobalVariable.mArrayPerson.add(p);
                            GlobalVariable.mArrayPersonName.add(roomName);
                        }

                        adapter.refresh();



                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onInviteIntoRoomChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final String roomId = data.getString("roomId");
                        final String roomName = data.getString("roomName");
                        String inviter = data.getString("inviter");

                        AlertDialog.Builder builder = new AlertDialog.Builder(AParent.this);
                        builder.setMessage(inviter+" mời bạn tham gia vào Room: "+roomName)
                                .setCancelable(false)
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("roomName",roomName);
                                            jsonObject.put("roomId",roomId);
                                            jsonObject.put("friendName",userName);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    mSocket.emit(GlobalVariable.C_2_S_ACCEPT_INVITE_JOIN_ROOM,jsonObject);
                                    }
                                })
                                .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onCreateRoomChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    //CommonHelper.showToast(getApplicationContext(),data.toString());
                    try {
                        String rooId = data.getString("rooId");
                        String roomName = data.getString("roomName");
                        PersonObj p = new PersonObj();
                        p.setRoomId(rooId);
                        p.setRoomName(roomName);

                        p.getmArrUserNames().add(userName);
                        GlobalVariable.mArrayPerson.add(p);
                        GlobalVariable.mArrayPersonName.add(roomName);
                        adapter.refresh();

                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onGetChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    Log.d(TAG, data.toString());
                    try {
                        String username = data.getString("username");
                        String message = data.getString("message");
                        String time = data.getString("time");

                        ChatObj chat = new ChatObj();
                        chat.setContent(message);
                        chat.setDate(time);
                        chat.setMe(false);


                        if(GlobalVariable.person != null && itfNotificationChatOne != null){
                            itfNotificationChatOne.notificationChat(chat);
                        }else{
                            int i = GlobalVariable.mArrayPersonName.indexOf(username);
                            GlobalVariable.mArrayPerson.get(i).getmArrChat().add(chat);
                        }
                        adapter.refresh();

                        //CommonHelper.showToast(getApplicationContext(), data.toString());
                    }catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onUpdateStatusUserJoinedNewLogined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.d(TAG, data.toString());
                    try {
                        String userNameX = data.getString("userName");
//                        CommonHelper.showWarning(AParent.this,"userName: "+userNameX+" + myname: "+userName);
                        if(CommonHelper.checkValidString(userNameX) && CommonHelper.checkValidString(userName)){
                            if(userName.equalsIgnoreCase(userNameX)){
                                //khong them vao danh sach
                            }else{
                                int index = GlobalVariable.mArrayPersonName.indexOf(userNameX);
                                if( index != -1){
                                    GlobalVariable.mArrayPerson.get(index).setStatus(1);
                                }else{
                                    PersonObj p = new PersonObj();
                                    p.setStatus(1);
                                    p.setUsername(userNameX);
                                    GlobalVariable.mArrayPersonName.add(userNameX);
                                    GlobalVariable.mArrayPerson.add(p);
                                }
                            }
                        }

                        adapter.refresh();

                        //CommonHelper.showToast(getApplicationContext(), data.toString());
                    }catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onUpdateStatusUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.d(TAG, data.toString());
                    try {
                        JSONArray jsonArray = data.getJSONArray("userNameLoginedList");
//                        CommonHelper.showWarning(AParent.this,"userNameLoginedList: "+jsonArray.toString());
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            String userNameX = jsonArray.get(i).toString();
                            if(CommonHelper.checkValidString(userNameX) && CommonHelper.checkValidString(userName)){
                                if(userName.equalsIgnoreCase(userNameX)){
                                    //-> la minh => khong lam gi them
                                }else{
                                    int index = GlobalVariable.mArrayPersonName.indexOf(userNameX);
                                    if( index != -1){
                                        GlobalVariable.mArrayPerson.get(index).setStatus(1);
                                    }else{
                                        PersonObj p = new PersonObj();
                                        p.setStatus(1);
                                        p.setUsername(userNameX);
                                        GlobalVariable.mArrayPersonName.add(userNameX);
                                        GlobalVariable.mArrayPerson.add(p);
                                    }
                                }
                            }


                        }
                        //String username = data.getString("userNameLoginedList");

                        adapter.refresh();

                        //CommonHelper.showToast(getApplicationContext(), data.toString());
                    }catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("abc","-----> "+args.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(itfNotificationConnectNS != null){
                        itfNotificationConnectNS.notificationConnectNS(getString(R.string.connect), Color.GREEN);
                    }
                    if(!isConnected) {
                        if(null!=userName)
                            mSocket.emit(GlobalVariable.C_2_S_LOGIN, userName);
                        Toast.makeText(getApplicationContext(),
                                R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;

                    }else{

                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                    if(itfNotificationConnectNS != null){
                        itfNotificationConnectNS.notificationConnectNS(getString(R.string.disconnect), Color.RED);
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                    if(itfNotificationConnectNS != null){
                        itfNotificationConnectNS.notificationConnectNS(getString(R.string.error_connect), Color.RED);
                    }
                }
            });
        }
    };
    @Override
    public void onResume() {
        super.onResume();

    }

    protected ViewGroup id_ly_topbar, id_ly_left, id_ly_center, id_ly_right;
    protected Button id_btn_left, id_btn_left2, id_btn_left3, id_btn_left4, id_btn_right, id_btn_right2, id_btn_right3, id_btn_right4;
    public TextView id_tv_title, id_tv_title_sub;

    protected void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    protected void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    protected void initTopbarView() {
        id_ly_topbar = (ViewGroup) findViewById(R.id.id_ly_topbar);
        id_ly_left = (ViewGroup) findViewById(R.id.id_ly_left);
        id_ly_center = (ViewGroup) findViewById(R.id.id_ly_center);
        id_ly_right = (ViewGroup) findViewById(R.id.id_ly_right);
        id_btn_right2 = (Button) findViewById(R.id.id_btn_right2);
        id_btn_right3 = (Button) findViewById(R.id.id_btn_right3);
        id_btn_right4 = (Button) findViewById(R.id.id_btn_right4);

        id_btn_left = (Button) findViewById(R.id.id_btn_left);
        id_btn_left2 = (Button) findViewById(R.id.id_btn_left2);
        id_btn_left3 = (Button) findViewById(R.id.id_btn_left3);
        id_btn_left4 = (Button) findViewById(R.id.id_btn_left4);
        id_btn_right = (Button) findViewById(R.id.id_btn_right);

        id_tv_title = (TextView) findViewById(R.id.id_tv_title);
        id_tv_title_sub = (TextView) findViewById(R.id.id_tv_title_sub);

        id_btn_left.setOnClickListener(this);
        id_btn_left2.setOnClickListener(this);
        id_btn_right.setOnClickListener(this);
        id_btn_right2.setOnClickListener(this);
        id_btn_right3.setOnClickListener(this);
        id_btn_left3.setOnClickListener(this);

        id_btn_left.setBackgroundResource(android.R.drawable.ic_media_previous);
        id_btn_right.setBackgroundResource(android.R.drawable.ic_input_add);
        showView(id_tv_title_sub);
        hideView(id_btn_left2);
        hideView(id_btn_left3);
        hideView(id_btn_left4);
        showView(id_btn_right);
        hideView(id_btn_right2);
        hideView(id_btn_right3);
        hideView(id_btn_right4);
        id_btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initCommonView(AParent classX) {
        if(classX instanceof MainActivity){
            id_tv_title.setText(getString(R.string.danhsach));
        }

    }

    protected abstract void initComponent();

    protected abstract void addListener();


    private ProgressDialog dialogLoading;

    public void showLoading() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
        if (dialogLoading == null) {
            dialogLoading = new ProgressDialog(this);
        }
        dialogLoading.setTitle(this.getResources().getString(R.string.app_name));
        dialogLoading.setMessage(getString(R.string.processing));
        dialogLoading.setCanceledOnTouchOutside(false);
        dialogLoading.setCancelable(false);
        dialogLoading.show();
    }

    public void disMissLoading() {

        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }

}
