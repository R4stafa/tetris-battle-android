package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import android.graphics.*;
import android.util.*;


public class MyGLSurfaceView extends GLSurfaceView implements Runnable{

    public static final int GRID_WIDTH = 18;
    public static final int GRID_HEIGHT = 32;

	int viewWidth, viewHeight;
	
    private final MyGLRenderer mRenderer;
    private Thread gameThread;
    private boolean alive;

    private int grid[][];

    private Quatron quatron;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer(context);
		
		
		
        grid = new int[GRID_WIDTH][GRID_HEIGHT];

        
        gameThread = new Thread(this);
        gameThread.start();


        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN){
			if(e.getX() < this.getWidth()/3){
				quatron.push(-1, 0);
			}
			else if(e.getX() < this.getWidth()*2/3){
				quatron.rotate();
			}
			else{
				quatron.push(1, 0);
			}
            
            updateScene();
        }
        mRenderer.processTouchEvent(e);
        return true;
    }

    @Override
    public void onPause(){
        mRenderer.onPause();
        alive = false;
    }

    @Override
    public void run() {
        alive = true;
        long mLastTime = System.currentTimeMillis();
        long nextGameTick = mLastTime + 1000;
		
		
		quatron = new Quatron(1, 2, Quatron.QuadType.L);
		
		
		for(int x=0; x<grid.length; x++){
			for(int y=0; y<grid.length; y++){
				grid[x][y] = 0;
			}
		}

        while(alive){
			
            // Get the current time
            long now = System.currentTimeMillis();

            // We should make sure we are valid and sane
            if (mLastTime > now) return;

            // Get the amount of time the last frame took.
            long elapsed = now - mLastTime;

            if(nextGameTick <= now){
                gameTick();
                nextGameTick += 1000;
            }

            // Save the current time to see how long it took
            mLastTime = now;
        }
    }
	
	private void freezeQuatron(){
		for(Point p :quatron.getTransformedQuads(1)){
			grid[p.x][p.y] = 1;
		}
	}

    private void gameTick(){
        if(!quatron.push(0, -1)){
			freezeQuatron();
			quatron = new Quatron(5, 5, Quatron.QuadType.L);
		}
		
		for(int x=0; x<grid.length; x++){
			for(int y=0; y<grid.length; y++){
				if(grid[x][y] > 0 && quatron.collides(x, y)){
					freezeQuatron();
					quatron = new Quatron(5, 20, Quatron.QuadType.L);
				}
			}
		}
		
        updateScene();
    }

    private void updateScene(){
        Scene scene = new Scene();

        for (Sprite sprite :quatron.getSprites()){
            scene.addSprite(sprite);
        }

        for (int x=0; x<GRID_WIDTH; x++) {
            for (int y=0; y<GRID_HEIGHT; y++){
                if(grid[x][y] > 0){
					Sprite sprite = new Sprite(0, 0, Quatron.QUADSIZE, Quatron.QUADSIZE, Quatron.getTextureRegion(Quatron.RED));
                    sprite.translate(x*Quatron.QUADSIZE, y*Quatron.QUADSIZE);
					scene.addSprite(sprite);
                }
            }
        }
        mRenderer.setScene(scene);
    }
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
		super.onSizeChanged(xNew, yNew, xOld, yOld);

		viewWidth = xNew;
		viewHeight = yNew;
		
		if(viewWidth*16 < viewHeight*9){
			Quatron.QUADSIZE = (float)viewWidth/GRID_WIDTH;
		}
		else{
			Quatron.QUADSIZE = (float)viewHeight/GRID_HEIGHT;
		}
	}
}
