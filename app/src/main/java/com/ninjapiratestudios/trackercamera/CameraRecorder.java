package com.ninjapiratestudios.trackercamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class will be used to provide an object that will handle all
 * of the functions associated with the device's video camera.
 *
 * @author John Qualls
 * @version 2/27/2016
 */
public class CameraRecorder {
    public final static String LOG_TAG = "CAMERA_RECORDER";
    public final static String FILE_DIRECTORY = "Tracker_Camera";
    private Camera camera;
    private CameraPreview cameraPreview;
    private MediaRecorder mediaRecorder;
    private VideoActivity activity;
    private Camera.Size previewSize;
    private SurfaceTexture surfaceTexture;

    /**
     * Static factory method that gets a reference to the camera, and sets up the
     * camera preview.
     *
     * @param activity The activity used to access the camera and view
     *                 components.
     * @return A new instance of this class, or null if there was an error.
     */
    public static CameraRecorder newInstance(VideoActivity activity) {
        CameraRecorder cameraRecorder = new CameraRecorder();
        cameraRecorder.activity = activity;
        cameraRecorder.camera = getCameraInstance();
        if(cameraRecorder.camera == null) {
            return null;
        }
        cameraRecorder.cameraPreviewSetup();
        return cameraRecorder;
    }

    /**
     * Displays a FileNameDialog to the user.
     */
    public void displayFileNameDialog() {
        FileNameDialog dialog = FileNameDialog.newInstance(this);
        dialog.show(activity.getFragmentManager(), FileNameDialog.FRAGMENT_TAG);
    }

    /**
     * Starts Camera recording.
     */
    public void startRecording() {
        setupCamera();
        mediaRecorder.start();
    }

    /**
     * Stops Camera recording.
     */
    public void stopRecording() {
        mediaRecorder.stop();
        releaseMediaResource();
    }

    public void releaseMediaResource() {

        // Release media recorder
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            Log.i(LOG_TAG, "Media resources released.");
        }
    }

    public void releaseCameraResource() {
        // Release the camera for other applications
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            Log.i(LOG_TAG, "Camera resources released.");
        }
    }

    /**
     * Prepares the camera for recording
     */
    private void setupCamera() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile
                .QUALITY_HIGH));
        Log.i(LOG_TAG, "Camera configurations are set.");
        // Internal Directory
//        File filesDir = activity.getDir(FILE_DIRECTORY, Context
//                .MODE_WORLD_READABLE);
        //External directory
        File filesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), FILE_DIRECTORY);
        if (!filesDir.exists()) {
            filesDir.mkdir();
        }

        File videoFile = new File(filesDir.getPath(), "TestVideo.mp4");
        mediaRecorder.setOutputFile(videoFile.toString());
        mediaRecorder.setVideoSize(cameraPreview.getWidth(), cameraPreview
                .getHeight());
        mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
        Log.i(LOG_TAG, "File saved to: " + videoFile.getPath());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(LOG_TAG, "IllegalStateException preparing MediaRecorder: " + e
                    .getMessage());
            mediaRecorder.release();
            return;
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException preparing MediaRecorder: " + e
                    .getMessage
                            ());
            mediaRecorder.release();
            return;
        }
        Log.i(LOG_TAG, "Camera successfully configured");
    }

    public Camera.Size getPreviewSize() {
        return previewSize;
    }

    /**
     * A safe way to get an instance of the Camera object.
     * reference:http://developer.android.com/guide/topics/media/camera
     * .html#access-camera
     *
     * @return A reference to the device Camera, null if there was an error.
     */
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.e(LOG_TAG, "Camera is not available (in use or does not " +
                    "exist)");
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Initializes the preview view for the camera.
     */
    private void cameraPreviewSetup() {
        cameraPreview = new CameraPreview(activity, camera);
        FrameLayout preview = (FrameLayout) activity.findViewById(R.id
                .camera_preview);
        preview.addView(cameraPreview);
    }
}
