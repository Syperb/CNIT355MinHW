package com.example.cnit355.minhw;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
                            implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener{

    mainTaskView fragmentA;
    EditTask fragmentB;
    activity_settings fragmentC;
    File taskDir = new File("/Tasks");
    TextView taskName;
    DatePickerFragment fragmentD;
    String dateString;
    SimpleDateFormat date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentA = (mainTaskView) getSupportFragmentManager().findFragmentById(R.id.MainFragment);
        fragmentB = new EditTask();
        fragmentC = new activity_settings();
        fragmentD = new DatePickerFragment();
        taskName = (TextView) findViewById(R.id.txtName);
        date = new SimpleDateFormat("EEEE, MMM dd, yyyy");



        //tries to make a directory to store files in
        taskDir = new File(this.getApplicationContext().getFilesDir().getAbsolutePath() + taskDir);


        try {
            if (!taskDir.exists())
                taskDir.mkdir();
        } catch (Exception e){e.printStackTrace();}

        //Toast.makeText(this, this.getApplicationContext().getFilesDir().getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentA).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentB).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentC).commit();
        } else if (index == 3){
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentD).commit();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment){

        Log.d("MainActivity", "PositiveClick");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment){
        Log.d("MainActivity", "NegativeClick");
        onFragmentChanged(1);
    }
}
