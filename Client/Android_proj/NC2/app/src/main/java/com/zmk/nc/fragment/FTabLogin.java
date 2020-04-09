package com.zmk.nc.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zmk.nc.activity.R;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;
import com.zmk.nc.interfaceX.ITF_notificationConnectNS;
import com.zmk.nc.object.PersonObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;


public class FTabLogin extends FParent implements ITF_notificationConnectNS {
	private Button id_btn_dangnhap;
	private EditText id_edt_username, id_edt_password;
	private TextView id_tv_status;
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//CommonHelper.showToast(fpActivity,"onCreate");
		fpActivity.itfNotificationConnectNS = this;
		super.onCreate(savedInstanceState);


		fpActivity.mSocket.on(GlobalVariable.S_2_C_LOGIN, onLogin);
		fpActivity.mSocket.on(GlobalVariable.S_2_C_LOGIN_USERNAME_NOT_EXISTING,onLoginUsernameNotExisting);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//CommonHelper.showToast(fpActivity,"onCreateView");
		View v = inflater.inflate(R.layout.fragment_alogin, container, false);
		initComponent(v);
		addListener();

		return v;
	}

	@Override
	public void onDetach() {
		fpActivity.itfNotificationConnectNS = null;
		//CommonHelper.showToast(fpActivity,"onDetach");
		fpActivity.id_tv_title_sub.setText(fpActivity.getString(R.string.xinchao)+" : "+fpActivity.userName);
		fpActivity.mSocket.off(GlobalVariable.S_2_C_LOGIN, onLogin);
		fpActivity.mSocket.off(GlobalVariable.S_2_C_LOGIN_USERNAME_NOT_EXISTING,onLoginUsernameNotExisting);
		super.onDetach();

	}

	@Override
	public void onDestroyView() {
		//CommonHelper.showToast(fpActivity,"onDestroyView");
		super.onDestroyView();
	}

	@Override
	protected void initComponent(View v) {
		id_edt_username = (EditText) v.findViewById(R.id.id_edt_username);
		id_edt_password = (EditText) v.findViewById(R.id.id_edt_password);
		id_btn_dangnhap = (Button)v.findViewById(R.id.id_btn_dangnhap);
		id_tv_status = (TextView)v.findViewById(R.id.id_tv_status);
		disableBackButton(v);

	}
	private void disableBackButton(View v){
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		v.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( keyCode == KeyEvent.KEYCODE_BACK ) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
	private boolean checkValidInfor() {
		boolean chk = true;
		String us = id_edt_username.getText().toString().trim();
		String pw = id_edt_password.getText().toString().trim();
		if (chk && !CommonHelper.checkValidString(us)) {
			chk = false;
			CommonHelper.showWarning(fpActivity, getString(R.string.username_khonghople));
		}
		if (chk && !CommonHelper.checkValidString(pw)) {
			chk = false;
			CommonHelper.showWarning(fpActivity, getString(R.string.password_khonghople));
		}
		if(chk){
			fpActivity.userName = us;
			fpActivity.passWord = pw;
		}else{
			fpActivity.userName = "";
			fpActivity.passWord="";
		}
		return chk;
	}
	@Override
	protected void addListener() {
		id_btn_dangnhap.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_btn_dangnhap:
				if (checkValidInfor()) {
					fpActivity.mSocket.emit(GlobalVariable.C_2_S_LOGIN, fpActivity.userName);

				} else {

				}

				break;
		default:
			break;
		}

	}

	@Override
	public void notificationConnectNS(String info, int color) {
		id_tv_status.setText(info);
		id_tv_status.setTextColor(color);
	}
	private Emitter.Listener onLogin = new Emitter.Listener() {
		@Override
		public void call(Object... args) {
			JSONObject data = (JSONObject) args[0];
			JSONArray jsonArray = null;


			try {
				jsonArray = data.getJSONArray("contactsList");
				for(int i = 0 ; i < jsonArray.length(); i++){
					String userName = jsonArray.get(i).toString();
					if(fpActivity.userName.equalsIgnoreCase(userName)){
						//khong them vao danh sach
					}else{
						PersonObj p = new PersonObj();
						p.setStatus(3);
						p.setUsername(userName);
						GlobalVariable.mArrayPersonName.add(userName);
						GlobalVariable.mArrayPerson.add(p);
					}

				}

			}catch (JSONException e) {
				Log.d(fpActivity.TAG, e.getMessage());
				return;
			}
			fpActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					fpActivity.adapter.refresh();
					dismiss();

				}
			});
		}
	};
	private Emitter.Listener onLoginUsernameNotExisting = new Emitter.Listener() {
		@Override
		public void call(final Object... args) {
			fpActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
					JSONObject data = (JSONObject) args[0];
						CommonHelper.showWarning(fpActivity,data.getString("message"));
					}catch (Exception e) {
						Log.d(fpActivity.TAG, e.getMessage());
						return;
					}
				}
			});
		}
	};
}
