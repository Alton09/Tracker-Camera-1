package com.ninjapiratestudios.trackercamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

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
    private Camera camera;
    private CameraPreview cameraPreview;
    private MediaRecorder mediaRecorder;
    private VideoActivity activity;
    private Camera.Size previewSize;
    private SurfaceTexture surfaceTexture;

    public CameraRecorder(VideoActivity activity, Camera camera,
                          CameraPreview cameraPreview) {
        this.activity = activity;
        this.camera = camera;
        this.cameraPreview = cameraPreview;
    }

    public void cameraPreviewSetup(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
        camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            throw new RuntimeException("Error setting camera preview to " +
                    "texture.");
        }
    }

    public void updatePreview() {
        previewSize = camera.getParameters().getPreviewSize();
        Camera.Parameters param = camera.getParameters();
        param.setPictureSize(previewSize.width, previewSize.height);
        param.set("orientation", "landscape");
        camera.setParameters(param);
        camera.startPreview();
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
            camera.lock();
        }
    }

    public void releaseCameraResource() {
        // Release the camera for other applications
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    /**
     * Prepares the camera for recording
     */
    public void setupCamera() {
        mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile
                .QUALITY_HIGH));
        Log.i(LOG_TAG, "Camera configurations are set.");
        // Internal Directory
//        File filesDir = activity.getDir("files", Context
//                .MODE_WORLD_READABLE);
        //External directory
        File filesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Tracker_Camera");
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
}
