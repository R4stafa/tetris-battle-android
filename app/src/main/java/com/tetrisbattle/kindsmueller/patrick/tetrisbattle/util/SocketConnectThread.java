package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util;

import android.os.Handler;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Patrick on 17.02.2016.
 */
public class SocketConnectThread extends ConnectionThread {

    private String address;
    private Handler handler;
    private Socket socket;

    public SocketConnectThread(String address, Handler handler){
        this.address = address;
        this.handler = handler;
    }

    public void run(){
        try {
            socket = new Socket(address, 2468);
            handler.obtainMessage(Constants.HANDLER_CONNECTED).sendToTarget();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

    }
}
