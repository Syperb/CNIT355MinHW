package com.example.cnit355.minhw;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

            String date, hour, minute, progress, time;
            InputStream in;

            for (File file : listFiles) {
                //Line 3: Date, Line 4: hour, Line 5: minute, Line 7: progress

                in = new FileInputStream(file);

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                reader.readLine();
                reader.readLine();
                date = reader.readLine();
                hour = reader.readLine();
                minute = reader.readLine();
                reader.readLine();
                progress = reader.readLine();

//                if (Integer.parseInt(hour) > 12){
//                    hour = (Integer.parseInt(hour) - 12)
//                }

                fileName = file.getName();

                fileName = padRight(fileName, 25) + padRight(date + " at " + hour + ":" + minute, 30) + progress + "%";
                taskList.add(fileName);
            }

        }
        catch (NullPointerException e) {
            Toast.makeText(getContext(), "No tasks? Should probably add some.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //Sets up list view for selection
        ListView listViewMP3 = (ListView) rootView.findViewById(R.id.listViewTask);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, taskList);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setItemChecked(0, true);
        listViewMP3.setDividerHeight(20);
        listViewMP3.setLongClickable(true);


        listViewMP3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);

                return true;

            }
        });





        return rootView;


    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }




}

