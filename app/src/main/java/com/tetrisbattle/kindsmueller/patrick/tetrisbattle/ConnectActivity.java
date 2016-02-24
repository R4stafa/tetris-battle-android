package com.tetrisbattle.kindsmueller.patrick.tetrisbattle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.ConnectionThread;
import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.Constants;
import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.SocketAcceptThread;
import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.SocketConnectThread;

public class ConnectActivity extends Activity {

    ConnectionThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Intent intent = getIntent();

        switch(intent.getIntExtra(Constants.HOST_OR_CLIENT, -1)){
            case Constants.INTENT_HOST:
                host();
                break;
            case Constants.INTENT_CLIENT:
                connect(intent.getStringExtra(Constants.IP_ADDRESS));
                break;
            default:
                finishActivity(0);
                break;
        }
    }

    @Override
    protected void onStop() {
        thread.cancel();
        super.onStop();
    }

    public void host(){
        thread = new SocketAcceptThread(msgHandler);
        thread.start();
    }

    public void connect(String ip_address){
        thread = new SocketConnectThread(ip_address, msgHandler);
        thread.start();
    }

    Handler msgHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_CONNECTED:
                    Intent intent = new Intent(ConnectActivity.this, GameActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };

    private void handleMsg(String msg){

    }

}
