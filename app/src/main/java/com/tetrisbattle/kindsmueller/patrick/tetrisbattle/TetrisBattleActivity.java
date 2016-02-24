package com.tetrisbattle.kindsmueller.patrick.tetrisbattle;

import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.Constants;
import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.SocketAcceptThread;
import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.util.SocketConnectThread;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TetrisBattleActivity extends Activity {


    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris_battle);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);


        tv = (TextView)findViewById(R.id.testText);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void host(View v){
        //Intent intent = new Intent(this, ConnectActivity.class);
        //intent.putExtra(Constants.HOST_OR_CLIENT, Constants.INTENT_HOST);
        //startActivity(intent);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void join(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_join_game);
        dialog.setTitle("Join Game");

        final EditText inputIP = (EditText)dialog.findViewById(R.id.input_ip);

        Button cancel = (Button)dialog.findViewById(R.id.button_cancel);
        Button connect = (Button)dialog.findViewById(R.id.button_connect);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(inputIP.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void connect(String ip){
        Intent intent = new Intent(this, ConnectActivity.class);
        intent.putExtra(Constants.HOST_OR_CLIENT, Constants.INTENT_CLIENT);
        intent.putExtra(Constants.IP_ADDRESS, ip);
        startActivity(intent);
    }




}
