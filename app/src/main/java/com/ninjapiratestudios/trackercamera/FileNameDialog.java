package com.ninjapiratestudios.trackercamera;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Handles all of the functionality for the DialogFragment used to get the user
 * specified name for the video to record.
 * @author John Qualls
 * @version 3/5/2016
 */
public class FileNameDialog extends DialogFragment {
    // Fragment tag name For FragmentManager
    protected final String FRAGMENT_TAG = "DIALOG_FRAGMENT";

    /**
     * Initialize the style and button listeners for the DialogFragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style
                .Theme_Holo_Dialog);

        // Set button listeners
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_name_dialog,
                container, false);
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("File Name")
//                .setMessage("MESSAGE")
//
//                        // Save button listener
//                .setPositiveButton("Save", new
//                        DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int
//                                    which) {
//
//                            }
//                        })
//
//                        // Cancel button listener
//                .setNegativeButton("Cancel", new
//                        DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int
//                                    which) {
//
//                            }
//                        }).create();
//    }
}
