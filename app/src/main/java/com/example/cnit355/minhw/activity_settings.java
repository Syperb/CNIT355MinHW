package com.example.cnit355.minhw;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class activity_settings extends Fragment {

    File settings;
    EditText hoursWorked;
    Switch swNotif;
    Switch swSugg;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_settings, container,
                false);
        swNotif = (Switch) rootView.findViewById(R.id.swNotifications);
        swSugg = (Switch) rootView.findViewById(R.id.swHomework);

        //OnClickListener for the Back Button
        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                if (settings.exists()) {
                    settings.delete();
                    try{
                        FileWriter writer = new FileWriter(settings);
                        writer.append(
                                parseHours(hoursWorked.getText().toString()) + "\n"
                                + swNotif.getText().toString() + "\n"
                                + swSugg.getText().toString()
                        );
                        writer.close();

                    } catch(IOException e){throw new Error(e);}
                }
                else{
                    try{
                        FileWriter writer = new FileWriter(settings);
                        writer.append(
                                parseHours(hoursWorked.getText().toString()) + "\n"
                                        + swNotif.getText().toString() + "\n"
                                        + swSugg.getText().toString()
                        );
                        writer.close();

                    } catch(IOException e){throw new Error(e);}
                }

                activity.onFragmentChanged(0);
            }
        });

        //OnClickListener for the Notification Switch
        swSugg.setClickable(false);
        swNotif.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (swNotif.isChecked()){
                    swSugg.setClickable(true);
                    swSugg.setBackgroundColor(0);
                    swNotif.setText("true");
                    swNotif.setShowText(false);
                } else{
                    swSugg.setClickable(false);
                    swSugg.setChecked(false);
                    swSugg.setBackgroundColor(getResources().getColor(R.color.grayedOut));
                    swNotif.setText("false");
                    swNotif.setShowText(false);
                }
            }
        });

        //OnCLickListener for the Homework Suggestion Switch
        settings = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks/Settings");
        hoursWorked = (EditText) rootView.findViewById(R.id.txtHPD);
        swSugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swSugg.isChecked()){
                    swSugg.setText("true");
                    swSugg.setShowText(false);
                } else{
                    swSugg.setText("false");
                    swSugg.setShowText(true);
                }
            }
        });

        return rootView;
    }

    public String parseHours(String n){
        Integer i = 0;
        String s;
        n = n.trim();
        n = n.substring(0, 1);
        n = n.trim();
        try{
            i = Integer.parseInt(n);
            return n;
        }
        catch(NumberFormatException nfe){
            Toast.makeText(getContext(), "You must a 1 or 2 digit number for Homework Per Day", Toast.LENGTH_LONG);
            return n;
        }
    }
}
