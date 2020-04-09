package com.zmk.nc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmk.nc.activity.R;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;
import com.zmk.nc.object.PersonObj;

import java.util.ArrayList;


public class PersonAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;

    public PersonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return GlobalVariable.mArrayPerson.size();
    }

    @Override
    public Object getItem(int i) {
        return GlobalVariable.mArrayPerson.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private ViewGroup id_ly_status;
        private TextView id_tv_username;
        private TextView id_tv_chat_content;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        layoutInflater = LayoutInflater.from(context);
        ViewHolder viewholder = null;
        PersonObj item = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_person,
                    null);
            viewholder = new ViewHolder();
            viewholder.id_ly_status = (ViewGroup) convertView
                    .findViewById(R.id.id_ly_status);
            viewholder.id_tv_username = (TextView) convertView
                    .findViewById(R.id.id_tv_username);
            viewholder.id_tv_chat_content = (TextView) convertView
                    .findViewById(R.id.id_tv_chat_content);
            convertView.setTag(viewholder);
        }
        viewholder = (ViewHolder) convertView.getTag();
        if (GlobalVariable.mArrayPerson != null && GlobalVariable.mArrayPerson.size() > 0) {
            item = GlobalVariable.mArrayPerson.get(i);
            String userName = item.getUsername();
            if(!CommonHelper.checkValidString(userName)){
                userName+=item.getRoomName();
                if(item.getmArrUserNames() != null && item.getmArrUserNames().size()>0){
                    for(String name : item.getmArrUserNames()){
                        userName+=" + "+name;
                    }
                }

            }
            viewholder.id_tv_username.setText(userName);
            String message = "";
            int size = item.getmArrChat().size();
            if(item.getmArrChat() != null && size>0){
                message = item.getmArrChat().get(size-1).getContent();
            }
            viewholder.id_tv_chat_content.setText(message);
            if(item.getStatus() == 1){
                viewholder.id_ly_status.setBackgroundColor(Color.GREEN);
            }else if(item.getStatus() == 2){
                viewholder.id_ly_status.setBackgroundColor(Color.YELLOW);
            }else if(item.getStatus() == 3){
                viewholder.id_ly_status.setBackgroundColor(Color.DKGRAY);
            }
        }

        return convertView;
    }

    public void refresh(ArrayList<PersonObj> mArrs) {
        GlobalVariable.mArrayPerson = mArrs;
        notifyDataSetChanged();
    }
    public void refresh(){
        notifyDataSetChanged();
    }
}
