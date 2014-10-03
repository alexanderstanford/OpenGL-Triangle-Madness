package com.alex.trianglestroids;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    public static ConcurrentHashMap<String, Triangle> mTriangles = new ConcurrentHashMap<String, Triangle>();

    private int counter = 0;

    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] scratch = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public static void initializeTriangles(String id) {
        mTriangles.put(id, new Triangle());
        for (String element : mTriangles.keySet()) {
            Log.d("we have a: ", "triangle");
            for (int j = 0; j < 6; j++) {
                mTriangles.get(element).changeSize(true);
                mTriangles.get(element).hasSize = true;
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        if (counter >= 360) {
            counter -= 360;
        } else {
            counter += 9;
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mRotationMatrix, 0, -counter, 0, 0, -1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        try {
            for (String element : mTriangles.keySet()) {
                Log.d("these shitty triangles", "aren't being drawn");
                mTriangles.get(element).draw(scratch);
            }
        } catch (NullPointerException e) {
            Log.d("ERROR", "EXTERMINATE EXTERMINATE EXTERMINATE");
        }

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}