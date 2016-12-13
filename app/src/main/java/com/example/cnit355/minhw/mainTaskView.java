package com.example.cnit355.minhw;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.System.in;

public class mainTaskView extends Fragment implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_task_view, container,
                false);

        // New button to take user to EditTask fragment, and Settings button to take user to settings fragment
        ImageView btnNew = (ImageView) rootView.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);
            }
        });

        ImageView btnSettings = (ImageView) rootView.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(2);
            }
        });


        //Declares ArrayList as an array of Strings
        final ArrayList taskList = new ArrayList<String>();

        //Creates a file array for storing file names
        File[] listFiles = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();
        String fileName, extName;
        try {

            String name, date, hour, minute, progress, time;
            InputStream in;

            //Reads every task files information and adds it to the ArrayList
            for (File file : listFiles) {


                in = new FileInputStream(file);

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                name = reader.readLine();
                reader.readLine();
                date = reader.readLine();
                hour = reader.readLine();
                minute = reader.readLine();
                reader.readLine();
                progress = reader.readLine();


                fileName = padRight(name + ":", 20) + date + " at " + hour + ":" + minute + padRight(" ", 10) + progress + "%";
                taskList.add(fileName);
            }

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "No tasks? Should probably add some.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Sets up list view for selection
        final ListView listViewMP3 = (ListView) rootView.findViewById(R.id.listViewTask);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, taskList);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);
        listViewMP3.setDividerHeight(20);
        listViewMP3.setLongClickable(true);


        //Task list onLongClick Listener to ask user whether they want to finish task or edit task through a dialog
        listViewMP3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {


                ConfirmDeleteDialogFragment dialogFragment = new ConfirmDeleteDialogFragment();
                dialogFragment.show(getFragmentManager(), "DialogFragment");
                // ((MainActivity)getActivity().setCheck());

                String filename = taskList.get(pos).toString();

                filename = filename.substring(0, filename.indexOf(":")).replaceAll("\\s", "");
                listener.setFile(filename);


                adapter.notifyDataSetChanged();


                return true;

            }


        });

        return rootView;
    }

    // Padding method to pad strings before placement in ListView
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }


    // Methods required by interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
    }

    //Creates interface for giving main task file names
    public interface retrieveTaskListener {
        public void setFile(String string);
    }

    retrieveTaskListener listener;

    //Makes sure listener attaches to main activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (retrieveTaskListener) context;
        } catch (ClassCastException castException) {
            throw new ClassCastException();
        }
    }
}

