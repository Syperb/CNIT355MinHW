package com.example.cnit355.minhw;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class mainTaskView extends Fragment {
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_task_view, container,
                false);

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

        //Declares mp3 list as an array of mp3s
        ArrayList taskList = new ArrayList<String>();

        //Creates a file array for storing file names
        File[] listFiles = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();
        String fileName, extName;
        try {
            for (File file : listFiles) {
                fileName = file.getName();
                taskList.add(fileName);
            }

        }
        catch (NullPointerException e) {
            Toast.makeText(getContext(), "No tasks? Should probably add some.", Toast.LENGTH_SHORT).show();
        }

        //Sets up list view for selection
        ListView listViewMP3 = (ListView) rootView.findViewById(R.id.listViewTask);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_single_choice, taskList);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);



        return rootView;


    }





}

