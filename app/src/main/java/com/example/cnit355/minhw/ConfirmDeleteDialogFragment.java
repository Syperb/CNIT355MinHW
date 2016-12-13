package com.example.cnit355.minhw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Brad on 12/5/2016.
 */

public class ConfirmDeleteDialogFragment extends DialogFragment {
    @Override
    //Creates dialog fragment to ask for user input on editing or finishing clicked task
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like to finish or edit this task?")
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete call
                        mListener.onDialogPositiveClick(ConfirmDeleteDialogFragment.this);
                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Edit call
                        mListener.onDialogNegativeClick(ConfirmDeleteDialogFragment.this);
                    }
                });
        return builder.create();
    }

    // Interface for MainActivity to use to receive user input
    public interface ConfirmDeleteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmDeleteDialogListener mListener;

    //Makes sure listener attaches to main activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmDeleteDialogListener) context;
            Log.d("CDDF", "onAttach Method");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement NoticeDialogListener");
        }
    }


}
