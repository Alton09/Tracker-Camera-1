package com.ninjapiratestudios.trackercamera;

import android.hardware.Camera;
import android.media.MediaRecorder;

/**
 * This class will be used to provide an object that will handle all
 * of the functions associated with the device's video camera.
 *
 * @author John Qualls
 * @version 2/27/2016
 */
public class CameraRecorder {
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private VideoActivity activity;

    public CameraRecorder(VideoActivity activity) {
        this.activity = activity;
    }

    /**
     * Displays a FileNameDialog to the user.
     */
    public void displayFileNameDialog() {
        FileNameDialog dialog = new FileNameDialog();
        dialog.show(activity.getFragmentManager(), dialog.FRAGMENT_TAG);
    }

    /**
     * Gets the camera resource from the Android system.
     */
    public void acquireCameraResource() {

    }

    /**
     * Initializes the MediaRecorder object in order to prepare Camera for video recording.
     */
    public void setupCamera() {

    }

    /**
     * Starts Camera recording.
     */
    public void startRecording() {

    }

    /**
     * Stops Camera recording.
     */
    public void stopRecording() {

    }

}
