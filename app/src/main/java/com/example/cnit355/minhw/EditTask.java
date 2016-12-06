package com.example.cnit355.minhw;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class EditTask extends Fragment {

    Button btnCalendar;
    File check;
    File editedTask;
    SeekBar seekBar;
    TimePicker timePicker;
    EditText txtTaskName;
    EditText txtTaskDesc;
    EditText txtTaskClass;
    EditText editTaskComplete;


    String name, date, hour, minute, progress, time, classed, desc, completion;
    InputStream in;


    @Override
    public void onResume() {
        super.onResume();

        MainActivity activity = (MainActivity) getActivity();

        if (activity.dateString != null){
            btnCalendar.setText(activity.dateString);
        }

        try {
            if (((MainActivity)getActivity()).check != null){
                check = ((MainActivity)getActivity()).check;
                File file = new File(activity.getFilesDir().getAbsolutePath() + "/Tasks/" + check);
                in = new FileInputStream(file);


                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                name = reader.readLine();
                classed = reader.readLine();
                date = reader.readLine();
                hour = reader.readLine();
                minute = reader.readLine();
                desc = reader.readLine();
                progress = reader.readLine();
                completion = reader.readLine();

                reader.close();

                txtTaskName.setText(name);
                txtTaskClass.setText(classed);
                btnCalendar.setText(date);
                timePicker.setHour(Integer.parseInt(hour));
                timePicker.setMinute(Integer.parseInt(minute));
                txtTaskDesc.setText(desc);
                seekBar.setProgress(Integer.parseInt(progress));
                editTaskComplete.setText(completion);
            }
        } catch (IOException ioe) {throw new Error(ioe);}

        ((MainActivity)getActivity()).check = null;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_edit_task, container,
                false);

        editTaskComplete = (EditText) rootView.findViewById(R.id.editCompletion);
        txtTaskClass = (EditText) rootView.findViewById(R.id.editClass);
        txtTaskDesc = (EditText) rootView.findViewById(R.id.editDescription);
        txtTaskName = (EditText) rootView.findViewById(R.id.editName);
        timePicker = (TimePicker) rootView.findViewById(R.id.editDueTime);
        seekBar = (SeekBar) rootView.findViewById(R.id.progressBar);
        editedTask = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/" + txtTaskName.getText().toString().replaceAll("\\s",""));
        btnCalendar = (Button) rootView.findViewById(R.id.btnCalendar);

        try {
        if (((MainActivity)getActivity()).check != null){
            check = ((MainActivity)getActivity()).check;
            File file = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/" + check);
            in = new FileInputStream(file);


            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            name = reader.readLine();
            classed = reader.readLine();
            date = reader.readLine();
            hour = reader.readLine();
            minute = reader.readLine();
            desc = reader.readLine();
            progress = reader.readLine();
            completion = reader.readLine();

            reader.close();

//            txtTaskName.setText("");
//            txtTaskClass.setText("");
//            btnCalendar.setText("");
//            timePicker.setHour(0);
//            timePicker.setMinute(0);
//            txtTaskDesc.setText("");
//            seekBar.setProgress(0);
//            editTaskComplete.setText("");
//
            txtTaskName.setText(name);
            txtTaskClass.setText(classed);
            btnCalendar.setText(date);
            timePicker.setHour(Integer.parseInt(hour));
            timePicker.setMinute(Integer.parseInt(minute));
            txtTaskDesc.setText(desc);
            seekBar.setProgress(Integer.parseInt(progress));
            editTaskComplete.setText(completion);
            rootView.refreshDrawableState();
            }
        } catch (IOException ioe) {throw new Error(ioe);}

        btnCalendar = (Button) rootView.findViewById(R.id.btnCalendar);
        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                ((MainActivity)getActivity()).check = null;

                activity.onFragmentChanged(0);
                //getFragmentManager().beginTransaction().remove(getTargetFragment()).commit();
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                Log.d("EditTask", "Before attempt to change fragment to datepicker");
                activity.onFragmentChanged(3);
                Log.d("EditTask", "after attempt to change fragment to datepicker");
            }
        });

        ImageView btnSave = (ImageView) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                editedTask = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/" + txtTaskName.getText().toString().replaceAll("\\s",""));



                if (editedTask.exists()){
                    //create dialog to ask user if they want to overwrite
                    ((MainActivity)getActivity()).overwrite = editedTask;

                    ConfirmOverwriteDialogFragment dialogFragment = new ConfirmOverwriteDialogFragment();
                    dialogFragment.show(getFragmentManager(), "DialogFragment" );
                    //Toast.makeText(getContext(), "Task already exists", Toast.LENGTH_SHORT).show();

                    try {
                        FileWriter writer = new FileWriter(editedTask);
                        writer.append(
                                txtTaskName.getText().toString() + "\n"
                                        + txtTaskClass.getText().toString() + "\n"
                                        + btnCalendar.getText().toString() + "\n"
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
                else{
                    try {
                        FileWriter writer = new FileWriter(editedTask);
                        writer.append(
                                txtTaskName.getText().toString() + "\n"
                                + txtTaskClass.getText().toString() + "\n"
                                + btnCalendar.getText().toString() + "\n"
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

    public void clearAll(){


    }


}
