package com.alex.trianglestroids;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class MyActivity extends Activity {

    private GLSurfaceView mGLView;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
        mGLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGLRenderer.initializeTriangles(String.valueOf(counter));
                counter++;
            }
        });
    }
}