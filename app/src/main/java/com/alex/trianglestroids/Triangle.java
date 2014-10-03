package com.alex.trianglestroids;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class Triangle {

    public boolean hasSize = false;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private int COORDS_PER_VERTEX = 3;
    private float triangleCoords[] = {
            0.00f,  0.00f, 0.00f,
            0.00f,  0.00f, 0.00f,
            0.00f,  0.00f, 0.00f
    };

    private int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private int vertexStride = COORDS_PER_VERTEX * 4;

    float color[] = { 1.0f, 0.0f, 0.0f, 0.0f };

    public Triangle() {
    }

    public void initializeTriangle() {
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);

        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();

        vertexBuffer.put(triangleCoords);

        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {

        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void changeSize(boolean sizeUp) {
        if (sizeUp) {
            triangleCoords[1] += 0.06;
            triangleCoords[3] -= 0.05;
            triangleCoords[4] -= 0.03;
            triangleCoords[6] += 0.05;
            triangleCoords[7] -= 0.03;
        } else {
            triangleCoords[1] -= 0.06;
            triangleCoords[3] += 0.05;
            triangleCoords[4] += 0.03;
            triangleCoords[6] -= 0.05;
            triangleCoords[7] += 0.03;
        }
        initializeTriangle();
    }

    public void setLocation(float[] location) {
        triangleCoords = location;
        initializeTriangle();
    }
}