package com.zmk.nc.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.zmk.nc.activity.R;
import com.zmk.nc.adapter.ChatOneAdapter;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;
import com.zmk.nc.interfaceX.ITF_notificationChat;
import com.zmk.nc.interfaceX.ITF_typingChat;
import com.zmk.nc.object.ChatObj;

import org.json.JSONException;
import org.json.JSONObject;


public class FTabChatOne extends FParent implements ITF_notificationChat, ITF_typingChat {
	private ListView id_lv_chat;
	private TextView id_tv_typing;
	private EditText id_edt_message;
	private Button id_btn_send;
	private ChatOneAdapter adapter;

	private String userNameFriend = GlobalVariable.person.getUsername();
	private boolean isRoomChat = false;
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//CommonHelper.showToast(fpActivity,"onCreate");
		isRoomChat = CommonHelper.checkValidString(GlobalVariable.person.getRoomName());
		if(isRoomChat){
			fpActivity.itfNotificationChatRoom = this;
			fpActivity.itfTypingChatRoom = this;
		}else{
			fpActivity.itfNotificationChatOne = this;
			fpActivity.itfTypingChatOne = this;

		}

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//CommonHelper.showToast(fpActivity,"onCreateView");
		View v = inflater.inflate(R.layout.frament_achat_one, container, false);

		initCommonView(v,FTabChatOne.this);
		initComponent(v);
		addListener();
		return v;
	}

	@Override
	public void onDetach() {
		GlobalVariable.person = null;
		fpActivity.itfNotificationChatOne = null;
		fpActivity.itfNotificationChatRoom = null;
		fpActivity.itfTypingChatOne = null;
		fpActivity.itfTypingChatRoom = null;

		if(isTyping){
			fpActivity.mSocket.emit(GlobalVariable.C_2_S_CANCEL_TYPING_CHAT,userNameFriend);
		}

		super.onDetach();
	}

	@Override
	public void onDestroyView() {
		//CommonHelper.showToast(fpActivity,"onDestroyView");
		super.onDestroyView();
	}

	@Override
	protected void initComponent(View v) {
		id_lv_chat = (ListView)v.findViewById(R.id.id_lv_chat);
		id_tv_typing = (TextView)v.findViewById(R.id.id_tv_typing);
		id_tv_typing.setText("");
		id_edt_message = (EditText)v.findViewById(R.id.id_edt_message);
		id_btn_send = (Button) v.findViewById(R.id.id_btn_send);

		adapter = new ChatOneAdapter(getActivity());
		id_lv_chat.setAdapter(adapter);

		if(GlobalVariable.person != null && (GlobalVariable.person.getmArrChat()!= null && (GlobalVariable.person.getmArrChat().size()>0))){
			id_lv_chat.setSelection(GlobalVariable.person.getmArrChat().size() - 1);
		}
		id_tv_title.setText(GlobalVariable.person.getUsername());
		if(isRoomChat){

		}else{
			if(CommonHelper.checkValidString(fpActivity.friendTyping)){
				id_tv_typing.setText(fpActivity.friendTyping+" đang gõ...");
			}else{
				id_tv_typing.setText("");
			}
		}
	}

	boolean isTyping = false;
	@Override
	protected void addListener() {
		id_btn_send.setOnClickListener(this);
		id_edt_message.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				if(s.toString().trim().length()>0){
					if(!isTyping){
						//emit
						//CommonHelper.showToast(getActivity(),"emit: "+isTyping);
						isTyping = true;
						if(isRoomChat){

						}else{
							fpActivity.mSocket.emit(GlobalVariable.C_2_S_TYPING_CHAT,userNameFriend);
						}
					}
				}else{
					if(isTyping){
						//emit
						//CommonHelper.showToast(getActivity(),"emit: "+isTyping);
						isTyping = false;
						if(isRoomChat){

						}else{
							fpActivity.mSocket.emit(GlobalVariable.C_2_S_CANCEL_TYPING_CHAT,userNameFriend);
						}
					}
				}
			}
		});
	}

	boolean checkValid(){
		boolean chk = false;
		String content = id_edt_message.getText().toString().trim();
		if(CommonHelper.checkValidString(content)){
			chk = true;
		}else{
			chk = false;
		}

		return chk;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_btn_send:
				if(checkValid()){
					if(GlobalVariable.person != null){
						String content = id_edt_message.getText().toString().trim();
						ChatObj chat = new ChatObj();
						chat.setContent(content);
						chat.setDate(CommonHelper.getTimeNow());
						chat.setMe(true);
						GlobalVariable.person.getmArrChat().add(chat);
						adapter.refresh();
						id_lv_chat.smoothScrollToPosition(GlobalVariable.person.getmArrChat().size() - 1);
						id_edt_message.setText("");
						if(isRoomChat){
							JSONObject jsonObject = new JSONObject();
							try {
								String roomName = GlobalVariable.person.getRoomName();
								String roomId = GlobalVariable.person.getRoomId();
								jsonObject.put("roomName",roomName);
								jsonObject.put("roomId",roomId);
								jsonObject.put("msg",content);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							fpActivity.mSocket.emit(GlobalVariable.C_2_S_SEND_CHAT_ROOM,jsonObject);
						}else{
							fpActivity.mSocket.emit(GlobalVariable.C_2_S_SEND_CHAT,userNameFriend+"*"+content);
						}

						fpActivity.adapter.refresh();
					}
				}
				break;
			case R.id.id_btn_right:
				showDialog();
				break;
		}
	}
	private String userName = "";
	private void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(fpActivity);
		builder.setTitle(getString(R.string.friend_name));

		final EditText input = new EditText(fpActivity);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				userName = input.getText().toString();
				if(CommonHelper.checkValidString(userName) && GlobalVariable.mArrayPersonName.contains(userName)){
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put("roomName",GlobalVariable.person.getRoomName());
						jsonObject.put("roomId",GlobalVariable.person.getRoomId());
						jsonObject.put("friendName",userName);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					fpActivity.mSocket.emit(GlobalVariable.C_2_S_INVITE_INTO_ROOM_CHAT,jsonObject);
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
	public void notificationChat(ChatObj message) {
		GlobalVariable.person.getmArrChat().add(message);
		adapter.refresh();
		id_lv_chat.smoothScrollToPosition(GlobalVariable.person.getmArrChat().size() - 1);
	}

	@Override
	public void typing(String friendName) {

		id_tv_typing.setText(friendName+" đang gõ...");
	}

	@Override
	public void cancelTyping(String friendName) {
		id_tv_typing.setText("");
	}
}
