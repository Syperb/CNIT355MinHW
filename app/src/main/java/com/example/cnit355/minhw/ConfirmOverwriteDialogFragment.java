package com.example.cnit355.minhw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * Created by Brad on 12/5/2016.
 */

public class ConfirmOverwriteDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Overwrite file?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Overwrite call
                        mListener.onDialogYesClick(ConfirmOverwriteDialogFragment.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing call
                        mListener.onDialogNoClick(ConfirmOverwriteDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public interface ConfirmOverwriteDialogListener {
        public void onDialogYesClick(DialogFragment dialogFragment);
        public void onDialogNoClick(DialogFragment dialog);
    }

    ConfirmOverwriteDialogFragment.ConfirmOverwriteDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmOverwriteDialogFragment.ConfirmOverwriteDialogListener) context;
            Log.d("CDDF", "onAttach Method");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement NoticeDialogListener");
        }
    }


}
