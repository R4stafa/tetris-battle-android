package com.tetrisbattle.kindsmueller.patrick.tetrisbattle.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.tetrisbattle.kindsmueller.patrick.tetrisbattle.R;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 768;

    // Misc
    Context mContext;
    long mLastTime;

    public MyGLRenderer(Context c)
    {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;

    }

    public void onPause()
    {
        /* Do stuff to pause the renderer */
    }

    public void onResume()
    {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        if(vertices != null){
            // Update our example
            updateScene();

            // Render our example
            Render(mtrxProjectionAndView);
        }

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;

    }

    private void Render(float[] m) {

        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(ShaderHelper.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(ShaderHelper.sp_Image,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ShaderHelper.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (ShaderHelper.sp_Image,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Create the image information
        SetupImage();

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Create the shaders, solid color
        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER,
                ShaderHelper.vs_SolidColor);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER,
                ShaderHelper.fs_SolidColor);

        ShaderHelper.sp_SolidColor = GLES20.glCreateProgram();
        GLES20.glAttachShader(ShaderHelper.sp_SolidColor, vertexShader);
        GLES20.glAttachShader(ShaderHelper.sp_SolidColor, fragmentShader);
        GLES20.glLinkProgram(ShaderHelper.sp_SolidColor);

        // Create the shaders, images
        vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER,
                ShaderHelper.vs_Image);
        fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER,
                ShaderHelper.fs_Image);

        ShaderHelper.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(ShaderHelper.sp_Image, vertexShader);
        GLES20.glAttachShader(ShaderHelper.sp_Image, fragmentShader);
        GLES20.glLinkProgram(ShaderHelper.sp_Image);

        // Set our shader programm
        GLES20.glUseProgram(ShaderHelper.sp_Image);
    }

    public void setScene(Scene scene){
        vertices = scene.getVertices();
        uvs = scene.getUVs();
        indices = scene.getIndices();
    }

    public void updateScene()
    {
        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        // The texture buffer
        ByteBuffer tb = ByteBuffer.allocateDirect(uvs.length * 4);
        tb.order(ByteOrder.nativeOrder());
        uvBuffer = tb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    public void processTouchEvent(MotionEvent event)
    {

    }

    public void SetupImage()
    {
        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tetrisblock_red);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

    }
}
