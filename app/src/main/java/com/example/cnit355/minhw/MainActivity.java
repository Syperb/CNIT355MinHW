package com.example.cnit355.minhw;

import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener,
        mainTaskView.retrieveTaskListener,
        ConfirmOverwriteDialogFragment.ConfirmOverwriteDialogListener {

    //Fragment and view declarations
    mainTaskView fragmentA;
    EditTask fragmentB;
    activity_settings fragmentC;
    TextView taskName;
    DatePickerFragment fragmentD;
    String dateString, todayString;
    SimpleDateFormat dateFormat;
    FragmentManager mFragmentManager;
    NotificationCompat.Builder notifBuilder;
    NotificationManager notifManager;

    // Variable declarations
    String Info;
    File check, overwrite, taskDir = new File("/Tasks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment and Data Definition
        fragmentA = (mainTaskView) getSupportFragmentManager().findFragmentById(R.id.MainFragment);
        fragmentB = new EditTask();
        fragmentC = new activity_settings();
        fragmentD = new DatePickerFragment();
        taskName = (TextView) findViewById(R.id.txtName);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        notifBuilder = new NotificationCompat.Builder(this);
        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = 1;
        NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();

        //Create Notification
        notifBuilder.setSmallIcon(R.drawable.calendar);
        notifBuilder.setContentTitle("MinHW");
        notifBuilder.setContentText("Assignments Due");
        inbox.setBigContentTitle("Assignments Due Today:");

        //Create files to read from
        File[] listFiles = new File(this.getApplicationContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();
        File settingsFile = new File(this.getApplication().getFilesDir().getAbsolutePath() + "/Settings");

        //Two String values for reading from the Settings file
        String swNotifText = "true";
        String swSuggText = "true";

        try {
            //Variables for reading from the Task Files
            String name, date, hour, minute, progress, time;
            Double timeInt;
            Double timeTotal = 0.0;
            Integer i = 0;
            InputStream in, in2;

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
                time = reader.readLine();

                //Define Todays Date and increment it by a week for the Notification
                Calendar calendarDate = Calendar.getInstance(TimeZone.getTimeZone("EST"), Locale.US);
                calendarDate.setTime(new Date());
                todayString = dateFormat.format(calendarDate.getTime());
                calendarDate.add(Calendar.DATE, 7);
                dateString = dateFormat.format(calendarDate.getTime());

                try {
                    //Adding up total hours to work based upon if they are Due within a week
                    if (dateFormat.parse(date).before(dateFormat.parse(dateString))) {
                        timeInt = (Double.parseDouble(time)) * (1 - (Double.parseDouble(progress) / 100));
                        timeTotal = timeTotal + timeInt;
                    }

                    //Only adding those assignments Due today to the Notification
                    if (dateFormat.parse(date).equals(dateFormat.parse(todayString))) {
                        inbox.addLine(name + " is due today at " + hour + ":" + minute);
                        i++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String hours;
            Double hoursInt, suggTime;
            in2 = new FileInputStream(settingsFile);
            BufferedReader newreader = new BufferedReader(new InputStreamReader(in2));
            hours = newreader.readLine();
            swNotifText = newreader.readLine();
            swSuggText = newreader.readLine();

            hoursInt = Double.parseDouble(hours);
            suggTime = timeTotal / 7;
            inbox.setSummaryText("You have " + Integer.toString(i) + " Assignments Due Today");

            if (((hoursInt * 7) < timeTotal) && swSuggText.equals("true")) {
                inbox.addLine(" ");
                inbox.addLine("You have " + Double.toString(Math.round(timeTotal)) + " hours of homework due in the next week.");
                inbox.addLine("You only want to work for " + Double.toString(Math.round(hoursInt * 7)) + " total hours this week.");
                inbox.addLine("I suggest you work " + Double.toString(Math.round(suggTime)) + " hours per day for a week to catch up.");
            }

            notifBuilder.setStyle(inbox);

        } catch (NullPointerException e) {
            notifBuilder.setContentText("You have no Assignments Due Today.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Send Notification if settings allows it
        if (swNotifText.equals("true")) {
            notifManager.notify(notificationID, notifBuilder.build());
        }

        //tries to make a directory to store files in
        taskDir = new File(this.getApplicationContext().getFilesDir().getAbsolutePath() + taskDir);


        try {
            if (!taskDir.exists())
                taskDir.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Toast.makeText(this, this.getApplicationContext().getFilesDir().getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    // Fragment changing method
    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentA, "MainTaskView").commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentB, "EditTask").commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentC, "Settings").commit();
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentD, "DatePicker").commit();
        }
    }

    // Method to catch user confirm finish task, deletes file
    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {

        // Determines if file exists, if so deletes
        File deleteLocation = new File(taskDir + "/" + check);
        if (deleteLocation.exists())
            deleteLocation.delete();

        // Creates a new fragment transaction to refresh fragment after task delete
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Creates a new fragmentA, removes the old one, adds the new one, commits teh transaction, establishes fragmentA as a mainTaskView
        Fragment newInstance = recreateFragment(fragmentA);
        ft.remove(fragmentA);
        ft.add(R.id.activity_main, newInstance);
        ft.commit();
        fragmentA = (mainTaskView) newInstance;
    }

    // Method to catch user edit task, sends to EditTask fragment
    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        Log.d("MainActivity", "NegativeClick");
        onFragmentChanged(1);
    }

    // Gets clicked file name from mainTaskView
    public void setFile(String string) {
        check = new File(string);
    }

    // Method called to create a new fragment
    private Fragment recreateFragment(Fragment f) {
        try {
            Fragment.SavedState savedState = getSupportFragmentManager().saveFragmentInstanceState(f);

            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        } catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
        }
    }


    // Method to handle user confirm overwrite form EditTask fragment
    @Override
    public void onDialogYesClick(DialogFragment dialogFragment) {

        // Deletes file, sets sets clicked file to finished state
        overwrite.delete();
        check = null;

        // Creates new file
        try {
            FileWriter writer = new FileWriter(overwrite);
            writer.append(Info);
            writer.close();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            Fragment newInstance = recreateFragment(fragmentA);
            ft.remove(fragmentA);
            ft.add(R.id.activity_main, newInstance);
            ft.commit();
            fragmentA = (mainTaskView) newInstance;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle user stop overwrite, does nothing
    @Override
    public void onDialogNoClick(DialogFragment dialogFragment) {
        //Do nothing :0
    }
}
