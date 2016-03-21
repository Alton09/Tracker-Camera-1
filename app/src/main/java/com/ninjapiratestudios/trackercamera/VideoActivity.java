package com.ninjapiratestudios.trackercamera;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends Activity { // implements TextureView
    public final static String LOG_TAG = "VIDEO_ACTIVITY";
    private CameraRecorder cameraRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Instantiate CameraRecorder for future camera use
        cameraRecorder = CameraRecorder.newInstance(this);

        // When record button is clicked
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