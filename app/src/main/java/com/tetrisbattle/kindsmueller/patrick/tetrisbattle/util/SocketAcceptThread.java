package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketAcceptThread extends ConnectionThread {

    private ServerSocket hostSocket;
    private Handler handler;

    public SocketAcceptThread(Handler handler){
        this.handler = handler;
    }

    public void run(){
        try {
            hostSocket = new ServerSocket(2468);
            Socket socket = hostSocket.accept();
            if(socket != null){
                handler.obtainMessage(Constants.HANDLER_CONNECTED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel(){
        try {
            hostSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
