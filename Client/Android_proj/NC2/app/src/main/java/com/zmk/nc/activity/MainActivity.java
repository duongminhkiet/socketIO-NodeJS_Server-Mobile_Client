package com.zmk.nc.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zmk.nc.adapter.PersonAdapter;
import com.zmk.nc.fragment.FParent;
import com.zmk.nc.fragment.FTabChatOne;
import com.zmk.nc.fragment.FTabLogin;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;

import java.util.List;

public class MainActivity extends AParent implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTopbarView();
        initCommonView(MainActivity.this);
        initComponent();
        addListener();

        FTabLogin frg2 = new FTabLogin();
        startFragmentFromActivity(frg2);
    }
    protected void startFragmentFromActivity(FParent fp){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.id_fr_main,fp);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    protected void initComponent() {
        GlobalVariable.mArrayPerson.clear();
        id_lv_person = (ListView)findViewById(R.id.id_lv_person);
/*
        for(int i = 0 ; i < 10; i++){
            PersonObj p = new PersonObj();
            for(int j = 0 ; j < 10 ; j ++){
                ChatObj c = new ChatObj();
                c.setContent("Noi dung tin chat thu: "+j);
                c.setDate(CommonHelper.getTimeNow());
                if(j%2==0){
                    c.setMe(true);
                }else{
                    c.setMe(false);
                }
                p.getmArrChat().add(c);
            }
            p.setChatLatest("Chat latest: "+1);
            p.setPassword("pass"+i);
            p.setUsername("UserName"+i);
            p.setStatus(i%3 +1);
            GlobalVariable.mArrayPerson.add(p);
        }
        */
        adapter = new PersonAdapter(MainActivity.this);
        id_lv_person.setAdapter(adapter);
    }

    @Override
    protected void addListener() {
        id_lv_person.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_right:
                showDialog();
                break;
        }
    }
    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
    private String roomName = "";
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.room_name));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomName = input.getText().toString();
                if(CommonHelper.checkValidString(roomName)){
                    if(!containsCaseInsensitive(roomName,GlobalVariable.mArrayPersonName)){
                        mSocket.emit(GlobalVariable.C_2_S_CREATE_ROOM_CHAT,roomName);
                    }else{
                        CommonHelper.showWarning(MainActivity.this,getString(R.string.room_name_existing));
                    }

                }else{
                    CommonHelper.showWarning(MainActivity.this,getString(R.string.room_name_not_empty));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //mSocket.emit(GlobalVariable.C_2_S_CHAT_WITH,GlobalVariable.mArrayPersonName.get(position));
        GlobalVariable.person = GlobalVariable.mArrayPerson.get(position);
        FTabChatOne frg2 = new FTabChatOne();
        startFragmentFromActivity(frg2);


    }
}
