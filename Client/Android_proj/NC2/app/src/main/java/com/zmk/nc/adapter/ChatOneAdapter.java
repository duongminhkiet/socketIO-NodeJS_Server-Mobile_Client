/**
 * Created by: Kiet.Duong
 * Dec-29-2014
 **/
package com.zmk.nc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmk.nc.activity.R;
import com.zmk.nc.helper.GlobalVariable;
import com.zmk.nc.object.ChatObj;


public class ChatOneAdapter extends BaseAdapter {
    private Context mContext;
    private String urlAvat50;
    private boolean isChooseLots = false;

    public ChatOneAdapter(Context ct) {
        this.mContext = ct;
    }
    @Override
    public int getCount() {
        return GlobalVariable.person.getmArrChat().size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }
    @Override
    public Object getItem(int position) {
        return GlobalVariable.person.getmArrChat().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        ViewGroup id_ly_guest;
        TextView id_tv_time_guest,message_text_guest;

        ViewGroup id_ly_me;
        TextView id_tv_time_me,message_text_me;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        final ChatObj message = GlobalVariable.person.getmArrChat().get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_chat_one, null);
            holder = new ViewHolder();

            holder.id_ly_guest = (ViewGroup) convertView.findViewById(R.id.id_ly_guest);
            holder.id_tv_time_guest = (TextView)convertView.findViewById(R.id.id_tv_time_guest);
            holder.message_text_guest = (TextView)convertView.findViewById(R.id.message_text_guest);

            holder.id_ly_me = (ViewGroup) convertView.findViewById(R.id.id_ly_me);
            holder.id_tv_time_me = (TextView)convertView.findViewById(R.id.id_tv_time_me);
            holder.message_text_me = (TextView)convertView.findViewById(R.id.message_text_me);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (message != null) {
            try {
                if (message.isMe()) {
                    holder.id_ly_guest.setVisibility(View.GONE);
                    holder.id_ly_me.setVisibility(View.VISIBLE);

                    holder.id_tv_time_me.setText(message.getDate());
                    holder.message_text_me.setText(message.getContent());
                } else {
                    holder.id_ly_guest.setVisibility(View.VISIBLE);
                    holder.id_ly_me.setVisibility(View.GONE);

                    holder.id_tv_time_guest.setText(message.getDate());
                    holder.message_text_guest.setText(message.getContent());
                }

            } catch (Exception e) {

            }
        }
        return convertView;
    }
}
