package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.opengl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.util.*;

/**
 * Created by Patrick on 21.02.2016.
 */
public class Scene {

    private ArrayList<Float> vertices;
    private ArrayList<Float> uvs;
    private ArrayList<Short> indices;

    public Scene(){
        vertices = new ArrayList<Float>();
        uvs = new ArrayList<Float>();
        indices = new ArrayList<Short>();
    }

    public Scene(Sprite sprites[]){
        vertices = new ArrayList<Float>();
        uvs = new ArrayList<Float>();
        indices = new ArrayList<Short>();

        for (Sprite sprite :sprites) {
            addSprite(sprite);
        }
    }

    public void addSprite(Sprite sprite){
        int offset = vertices.size()/3;
		//Log.i("ADD_SPRITE", "offset = "+offset);
        for (short s :sprite.getIndices()) {
            //s += offset;
            indices.add((short) (s+offset));
        }

        for (float f :sprite.getTransformedVertices()) {
            vertices.add(f);
			//Log.i("ADD_SPRITE", "vertex = "+f);
        }

        for (float f :sprite.getUVs()) {
            uvs.add(f);
			//Log.i("ADD_SPRITE", "uv = "+f);
        }


    }

    public float[] getVertices(){
        float[] floatArray = new float[vertices.size()];
        int i = 0;

        for (Float f : vertices) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    public float[] getUVs(){
        float[] floatArray = new float[uvs.size()];
        int i = 0;

        for (Float f : uvs) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    public short[] getIndices(){
        short[] shortArray = new short[indices.size()];
        int i = 0;

        for (Short s : indices) {
            shortArray[i++] = (s != null ? s : (short)0); // Or whatever default you want.
        }
        return shortArray;
    }

}
