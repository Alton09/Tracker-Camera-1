package com.ninjapiratestudios.trackercamera;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Handles all of the functionality for the DialogFragment used to get the user
 * specified name for the video to record.
 *
 * @author John Qualls
 * @version 3/5/2016
 */
public class FileNameDialog extends DialogFragment {
    // Fragment tag name For FragmentManager
    public final static String LOG_TAG = "FILE_NAME_DIALOG";
    protected final static String FRAGMENT_TAG = "DIALOG_FRAGMENT";
    private CameraRecorder cameraRecorder;


    /**
     * Static factory method that is required for fragments to create new
     * objects.
     *
     * @param cameraRecorder The object responsible for all camera operations.
     * @return A new instance of this class.
     */
    public static FileNameDialog newInstance(CameraRecorder cameraRecorder) {
        FileNameDialog fileNameDialog = new FileNameDialog();
        fileNameDialog.cameraRecorder = cameraRecorder;
        return fileNameDialog;
    }

    /**
     * Sets the style for the DialogFragment.
     *
     * @param savedInstanceState Irrelevant to this dialog, Required to
     *                           override the super class method.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dialog style
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style
                .Theme_Holo_Dialog);
    }

    /**
     * Creates a View for the dialog specified in fragment_file_name_dialog.xml
     * and sets the button listeners.
     *
     * @param inflater           Creates a View out of the xml file.
     * @param container          Parent View container needed to create View.
     * @param savedInstanceState Irrelevant to this dialog, Required to
     *                           override the super class method.
     * @return The created view specified in the xml file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate xml view
        View v = inflater.inflate(R.layout.fragment_file_name_dialog,
                container, false);

        // Set button listeners
        v.findViewById(R.id.fn_dialog_save_button).setOnClickListener
                (new ButtonClick());
        v.findViewById(R.id.fn_dialog_cancel_button).setOnClickListener
                (new ButtonClick());

        return v;
    }

    /**
     * Handles all the save and cancel button actions for this dialog
     */
    protected class ButtonClick implements View.OnClickListener {
        /**
         * Handles Handles all the save and cancel button actions for this
         * dialog
         *
         * @param v The button view that was clicked by the user.
         */
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fn_dialog_save_button) {
                cameraRecorder.startRecording();
            } else {
                cameraRecorder.stopRecording();
            }
        }
    }
}
