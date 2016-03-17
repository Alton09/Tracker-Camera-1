package com.ninjapiratestudios.trackercamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends Activity { // implements TextureView
    // .SurfaceTextureListener{
    private CameraRecorder cameraRecorder;
    public final static String LOG_TAG = "VIDEO_ACTIVITY";
    private Camera camera;
    private CameraPreview cameraPreview;
    //CamPreview camPreview;
    GLCamView glCamView;
    MediaRecorder mediaRecorder;
    Overlay overlay;

    //buttons
    Button recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        camera = Camera.open();

        // Create our Preview view and set it as the content of our activity.
        cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        cameraRecorder = new CameraRecorder(this, camera,
                cameraPreview);

//        cameraRecorder = new CameraRecorder(this);
//        glCamView = new GLCamView(this, cameraRecorder);
//        overlay = new Overlay(this);
//        setContentView(glCamView);

        recordButtonListenerTemp();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraRecorder.releaseMediaResource();
        cameraRecorder.releaseCameraResource();
        Log.i(LOG_TAG, "onPause()");
    }

    // TODO Remove once CameraFragment component is finished
    private void recordButtonListenerTemp() {
        cameraRecorder.displayFileNameDialog();
    }
}