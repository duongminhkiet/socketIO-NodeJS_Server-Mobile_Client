package com.zmk.nc.application;

import android.app.Application;
import android.util.Log;

import com.zmk.nc.helper.GlobalVariable;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by duongminhkiet on 4/10/17.
 */

public class NCApplication extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(GlobalVariable.CHAT_SERVER_URL);
        } catch (Exception e) {
            Log.d("abc NCApplication ==== ","-----> "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
