/**
 * Created by: Kiet.Duong
 * Dec-29-2014
 **/
package com.zmk.nc.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zmk.nc.activity.AParent;
import com.zmk.nc.activity.MainActivity;
import com.zmk.nc.activity.R;
import com.zmk.nc.helper.CommonHelper;
import com.zmk.nc.helper.GlobalVariable;

public class FParent extends Fragment implements OnClickListener,
        OnTouchListener {
    protected AParent fpActivity;

    protected void changeColor() {

    }

    protected void changeText() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public FParent() {
        setRetainInstance(true);
    }

    public void refreshData() {

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fpActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        System.gc();
        fpActivity = null;
        super.onDetach();
    }

    protected void initComponent(View v) {
    }

    ;

    protected void addListener() {
    }

    ;

    @Override
    public void onClick(View v) {

    }

    protected ViewGroup id_ly_topbar, id_ly_left, id_ly_center, id_ly_right;
    protected Button id_btn_left, id_btn_left2, id_btn_left3, id_btn_left4, id_btn_right, id_btn_right2, id_btn_right3, id_btn_right4;
    protected TextView id_tv_title, id_tv_title_sub;

    protected void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    protected void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    protected void setTitle(String title) {

    }

    //FParent classX;
    protected void initCommonView(View v, FParent classX) {
        id_ly_topbar = (ViewGroup) v.findViewById(R.id.id_ly_topbar);
        id_ly_left = (ViewGroup) v.findViewById(R.id.id_ly_left);
        id_ly_center = (ViewGroup) v.findViewById(R.id.id_ly_center);
        id_ly_right = (ViewGroup) v.findViewById(R.id.id_ly_right);


        id_btn_left = (Button) v.findViewById(R.id.id_btn_left);
        id_btn_left2 = (Button) v.findViewById(R.id.id_btn_left2);
        id_btn_left3 = (Button) v.findViewById(R.id.id_btn_left3);
        id_btn_left4 = (Button) v.findViewById(R.id.id_btn_left4);

        id_btn_right = (Button) v.findViewById(R.id.id_btn_right);
        id_btn_right2 = (Button) v.findViewById(R.id.id_btn_right2);
        id_btn_right3 = (Button) v.findViewById(R.id.id_btn_right3);
        id_btn_right4 = (Button) v.findViewById(R.id.id_btn_right4);

        id_tv_title = (TextView) v.findViewById(R.id.id_tv_title);
        id_tv_title_sub = (TextView) v.findViewById(R.id.id_tv_title_sub);

        id_btn_left.setOnClickListener(this);
        id_btn_left2.setOnClickListener(this);
        id_btn_left3.setOnClickListener(this);
        id_btn_left4.setOnClickListener(this);

        id_btn_right.setOnClickListener(this);
        id_btn_right2.setOnClickListener(this);
        id_btn_right3.setOnClickListener(this);
        id_btn_right4.setOnClickListener(this);
        id_btn_left.setBackgroundResource(android.R.drawable.ic_media_previous);
        id_btn_right.setBackgroundResource(android.R.drawable.ic_input_add);
        //left
        showView(id_btn_left);
        hideView(id_btn_left2);
        hideView(id_btn_left3);
        hideView(id_btn_left4);
        //right
        hideView(id_btn_right);
        hideView(id_btn_right2);
        hideView(id_btn_right3);
        hideView(id_btn_right4);
        //title
        hideView(id_tv_title_sub);
        id_btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (classX instanceof FTabChatOne) {
            if(GlobalVariable.person != null && CommonHelper.checkValidString(GlobalVariable.person.getRoomName())){
                showView(id_btn_right);
            }
            showView(id_btn_left);

        }


    }
    protected void dismiss(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private ProgressDialog dialogLoading;

    public void showLoading() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
        if (dialogLoading == null) {
            dialogLoading = new ProgressDialog(fpActivity);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
