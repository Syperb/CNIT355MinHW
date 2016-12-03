package com.example.cnit355.minhw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
                EditText txtTaskClass = (EditText) rootView.findViewById(R.id.editClass);
                EditText txtTaskDesc = (EditText) rootView.findViewById(R.id.editDescription);
                EditText txtTaskName = (EditText) rootView.findViewById(R.id.editName);
                TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.editDueTime);
                SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.progressBar);
                File editedTask = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/" + txtTaskName.getText().toString());




                if (editedTask.exists()){
                    //create dialog to ask user if they want to overwrite
                    Toast.makeText(getContext(), "Task already exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        FileWriter writer = new FileWriter(editedTask);
                        writer.append(
                                txtTaskName.getText().toString() + "\n"
                                + txtTaskClass.getText().toString() + "\n"
                                + "dateformatplaceholder" + "\n"
                                + timePicker.getHour() + "\n"
                                + timePicker.getMinute() + "\n"
                                + txtTaskDesc.getText().toString() + "\n"
                                + seekBar.getProgress() + "\n"
                                + editTaskComplete.getText().toString()


                        );

                        writer.close();
                        //OutputStreamWriter outFs = new OutputStreamWriter(getActivity().openFileOutput(txtTaskName.getText().toString(), MODE_PRIVATE));
                        //outFs.write();
                        //outFs.close();

                    } catch (IOException e) {throw new Error(e);}
                }


                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        return rootView;
    }


}
