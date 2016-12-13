package com.example.cnit355.minhw;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;

public class activity_settings extends Fragment {

    //Data Definition
    ViewGroup rootView;
    File settings;
    EditText hoursWorked;
    Switch swNotif;
    Switch swSugg;
    InputStream in;
    String hours, swNotifText, swSuggText;
    Boolean stop = false;
    Activity fragmentContext;

    @Override
    public void onResume() {
        super.onResume();

        //Instantiate MainActivity
        MainActivity activity = (MainActivity) getActivity();
        fragmentContext = this.getActivity();

        try {
            try {
                //Define a file to read from the Settings File
                File file = new File(activity.getFilesDir().getAbsolutePath() + "/Settings");
                in = new FileInputStream(file);

                //Read files and assigning variables from the information
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                hours = reader.readLine();
                swNotifText = reader.readLine();
                swSuggText = reader.readLine();
                reader.close();

                //Setting the three settings based upon the information read from the Settings File
                hoursWorked.setText(hours);
                if (swNotifText.equals("true")) {
                    swNotif.setChecked(true);
                    swSugg.setClickable(true);
                    swSugg.setBackgroundColor(0);
                } else {
                    swNotif.setChecked(false);
                    swSugg.setClickable(false);
                }
                if (swSuggText.equals("true")) {
                    swSugg.setChecked(true);
                    swSugg.setBackgroundColor(0);
                } else {
                    swSugg.setChecked(false);
                }
            } catch (IOException e){e.printStackTrace();}
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Assigning Data to variables
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_settings, container,
                false);
        swNotif = (Switch) rootView.findViewById(R.id.swNotifications);
        swSugg = (Switch) rootView.findViewById(R.id.swHomework);

        //OnClickListener for the Back Button
        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Return to the MainActivity and Do not save any information
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        //OnClickListener for the Save Button
        ImageView btnSave = (ImageView) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();

                //Defining the filepath to save the settings
                settings = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Settings");
                if (settings.exists()) {
                    //Delete the old Settings File
                    settings.delete();
                    try{
                        //Create a new Settings File
                        FileWriter writer = new FileWriter(settings);
                        writer.append(
                                parseHours(hoursWorked.getText().toString()) + "\n"
                                + switchText(swNotif) + "\n"
                                + switchText(swSugg)
                        );
                        writer.close();

                    } catch(IOException e){throw new Error(e);}
                }
                else{
                    try{
                        //Create a new Settings File
                        FileWriter writer = new FileWriter(settings);
                        writer.append(
                                parseHours(hoursWorked.getText().toString()) + "\n"
                                        + switchText(swNotif) + "\n"
                                        + switchText(swSugg)
                        );
                        writer.close();

                    } catch(IOException e){throw new Error(e);}
                }

                //Go back to the MainActivity Fragment
                if (stop){
                } else {
                    activity.onFragmentChanged(0);
                }
            }
        });

        //OnClickListener for the Notification Switch
        swSugg.setClickable(false);
        swNotif.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Sets the Suggestion Switch in Settings to either be allowed to be clicked or not
                //depending on the Notification Switch since the suggestion comes from the notification
                if (swNotif.isChecked()){
                    swSugg.setClickable(true);
                    swSugg.setBackgroundColor(0);
                } else{
                    swSugg.setClickable(false);
                    swSugg.setChecked(false);
                    swSugg.setBackgroundColor(getResources().getColor(R.color.grayedOut));
                }
            }
        });

        //OnCLickListener for the Homework Suggestion Switch
        hoursWorked = (EditText) rootView.findViewById(R.id.txtHPD);
        swSugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Doesn't need to do anything at the moment but the method is here for future work
            }
        });

        return rootView;
    }

    //User Defined Method that will parse the out the number from the Hours Worked row in Settings
    public String parseHours(String n){
        Integer i = 0;
        String s;
        s = n.trim();
        s = s.substring(0, 1);
        s = s.trim();
        try{
            i = Integer.parseInt(s);
            if (n.length() >= 3){
                Integer.parseInt("Hello World!");
            }
            return s;
        }
        catch(NumberFormatException nfe){
            Toast.makeText(fragmentContext, "You must a 1 or 2 digit number for Homework Per Day", Toast.LENGTH_LONG);
            stop = true;
            return "3";
        }
    }

    //returns the the value, true or false, as a string
    public String switchText(Switch sw){
        if (sw.isChecked()){
            return "true";
        } else {
            return "false";
        }
    }
}
