package com.example.cnit355.minhw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class EditTask extends Fragment {
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_edit_task, container,
                false);

        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        ImageView btnSave = (ImageView) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTaskComplete = (EditText) rootView.findViewById(R.id.editCompletion);
                TextView txtTaskClass = (TextView) rootView.findViewById(R.id.txtClass);
                TextView txtTaskDesc = (TextView) rootView.findViewById(R.id.txtDescription);
                TextView txtTaskName = (TextView) rootView.findViewById(R.id.txtName);
                TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.editDueTime);
                SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.progressBar);
                File edittedTask = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/" + txtTaskName);




                if (edittedTask.exists()){
                    //create dialog to ask user if they want to overwrite
                }
                else{
                    try {
                        OutputStreamWriter outFs = new OutputStreamWriter(getActivity().openFileOutput(txtTaskName.getText().toString(), MODE_PRIVATE));
                        //outFs.write();
                        outFs.close();

                    } catch (IOException e) {e.printStackTrace();}
                }




                //Functionality of Save Button


                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        return rootView;
    }

}
