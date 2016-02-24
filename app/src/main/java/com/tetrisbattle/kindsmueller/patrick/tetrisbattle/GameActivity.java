package com.tetrisbattle.kindsmueller.patrick.tetrisbattle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.opengl.MyGLSurfaceView;


public class GameActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
