package com.example.cnit355.minhw;

import android.content.ContextWrapper;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    mainTaskView fragmentA;
    EditTask fragmentB;
    activity_settings fragmentC;
    File taskDir = new File("/Tasks");
    TextView taskName;
    DatePickerFragment fragmentD;
    TextView btnDate;
    String dateString, realDateString;
    SimpleDateFormat date;
    CalendarView realDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentA = (mainTaskView) getSupportFragmentManager().findFragmentById(R.id.MainFragment);
        fragmentB = new EditTask();
        fragmentC = new activity_settings();
        fragmentD = new DatePickerFragment();
        taskName = (TextView) findViewById(R.id.txtName);
        btnDate = (TextView) findViewById(R.id.btnDate);
        dateString = (String) btnDate.getText();
        realDate = (CalendarView) findViewById(R.id.calendarView);
        date = new SimpleDateFormat("EEEE, MMM dd, YYYY");
        realDateString = date.format(new Date(realDate.getDate()));



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
}
