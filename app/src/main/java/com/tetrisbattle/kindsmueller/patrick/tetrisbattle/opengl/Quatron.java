package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.opengl;

import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.*;

/**
 * Created by Patrick on 21.02.2016.
 */
public class Quatron {

    public static float QUADSIZE;
	
	public static enum QuadType{
		L,
		Li,
		Z,
		Zi,
		T,
		O,
		I
	}
	
    public static final int RED = 1;
	
	private Point quads[];
	
    private int x, y;
	private QuadType type;

    public Sprite[] getSprites(){
		Sprite sprites[] = new Sprite[4];
        for(int i=0; i<sprites.length; i++){
			sprites[i] = new Sprite(quads[i].x*QUADSIZE, quads[i].y*QUADSIZE, QUADSIZE, QUADSIZE, getTextureRegion(RED));
            sprites[i].translation = new PointF(x*QUADSIZE, y*QUADSIZE);
        }
        return sprites;
    }
	
	public Point[] getTransformedQuads(int modY){
		Point p[] = new Point[quads.length];
		for(int i=0; i<quads.length; i++){
			p[i] = new Point(x + quads[i].x, y + quads[i].y + modY);
		}
		return p;
	}

    public boolean collides(int x, int y){
        for(Point p :quads){
			if(x == this.x+p.x && y == this.y+p.y)return true;
        }
        return false;
    }
	
	private void keepInsideGrid(){
		for(Point p :quads){
			int d = x+p.x;
			if(d < 0){
				x -= d;
			}
			d = x+p.x-MyGLSurfaceView.GRID_WIDTH;
			if(d > 0){
				x -= d;
			}
		}
	}

    public boolean push(int x, int y){
        this.x += x;
        this.y += y;
		
		keepInsideGrid();
		
		if(this.y < 0)return false;
		return true;
    }

    public boolean rotate(){
		boolean r = true;
		for(int i=0; i<quads.length; i++){
			PointF p = new PointF(quads[i].x-1.5f, quads[i].y-1.5f);
			quads[i].x = (int)(1.5f+p.y);
			quads[i].y = (int)(1.5f-p.x);
			if(quads[i].y+y < 0)r = false;
		}
		keepInsideGrid();
		return r;
    }

    public Quatron(int x, int y, QuadType type){
        this.x = x;
        this.y = y;
		this.type = type;
        this.quads = getQuads();
    }
	
	private Point[] getQuads(){
		switch(type){
			case L:
				return new Point[]{
					new Point(1,0),
					new Point(1,1),
					new Point(1,2),
					new Point(2,2)			
				};
			case Li:
				return new Point[]{
					new Point(2,0),
					new Point(2,1),
					new Point(2,2),
					new Point(1,2)
				};
			case Z:
				return new Point[]{
					new Point(0,1),
					new Point(1,1),
					new Point(1,2),
					new Point(2,2)
				};
			case Zi:
				return new Point[]{
					new Point(0,2),
					new Point(1,2),
					new Point(1,1),
					new Point(2,1)
				};
			case T:
				return new Point[]{
					new Point(0,1),
					new Point(1,1),
					new Point(2,1),
					new Point(1,2)
				};
			case O:
				return new Point[]{
					new Point(1,1),
					new Point(1,2),
					new Point(2,1),
					new Point(2,2)
				};
			case I:
				return new Point[]{
					new Point(1,0),
					new Point(1,1),
					new Point(1,2),
					new Point(1,3)
				};
		}
		return null;
	}

//    public static Quatron getQuatron_L(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q. = new Sprite[]{
//                getSprite(1, getTextureRegion(RED)),
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED)),
//                getSprite(6, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_L_i(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(2, getTextureRegion(RED)),
//                getSprite(4, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED)),
//                getSprite(6, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_Z(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(1, getTextureRegion(RED)),
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(4, getTextureRegion(RED)),
//                getSprite(6, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_Z_i(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(1, getTextureRegion(RED)),
//                getSprite(4, getTextureRegion(RED)),
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_I(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(1, getTextureRegion(RED)),
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED)),
//                getSprite(7, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_O(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(4, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED)),
//                getSprite(6, getTextureRegion(RED))
//        };
//        return q;
//    }
//
//    public static Quatron getQuatron_T(int x, int y){
//        Quatron q = new Quatron(x, y);
//        q.sprites = new Sprite[]{
//                getSprite(1, getTextureRegion(RED)),
//                getSprite(3, getTextureRegion(RED)),
//                getSprite(4, getTextureRegion(RED)),
//                getSprite(5, getTextureRegion(RED))
//        };
//        return q;
//    }


    private static Sprite getSprite(int i, RectF texture_region){
        switch (i){
            case 1:
                return new Sprite(-QUADSIZE, QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
            case 2:
                return new Sprite(0, QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
            case 3:
                return new Sprite(-QUADSIZE, 0, QUADSIZE, QUADSIZE, texture_region);
            case 4:
                return new Sprite(0, 0, QUADSIZE, QUADSIZE, texture_region);
            case 5:
                return new Sprite(-QUADSIZE, -QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
            case 6:
                return new Sprite(0, -QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
            case 7:
                return new Sprite(-QUADSIZE, -2*QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
            case 8:
                return new Sprite(0, -2*QUADSIZE, QUADSIZE, QUADSIZE, texture_region);
        }
        return null;
    }

    public static RectF getTextureRegion(int i){
        switch(i){
            case Quatron.RED:
                return new RectF(0.0f, 0.0f, 1.0f, 1.0f);

        }
        return null;
    }
}
