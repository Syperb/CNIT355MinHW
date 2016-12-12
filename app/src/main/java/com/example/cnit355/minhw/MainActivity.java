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
                            ConfirmOverwriteDialogFragment.ConfirmOverwriteDialogListener{

    mainTaskView fragmentA;
    EditTask fragmentB;
    activity_settings fragmentC;
    File taskDir = new File("/Tasks");
    TextView taskName;
    DatePickerFragment fragmentD;
    String dateString;
    SimpleDateFormat dateFormat;
    File check;
    File overwrite;
    String Info;
    FragmentManager mFragmentManager;
    NotificationCompat.Builder notifBuilder;
    NotificationManager notifManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        notifBuilder.setContentText("Assignments Due Today");
        inbox.setBigContentTitle("Assignments:");


        File[] listFiles = new File(this.getApplicationContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();
        File settingsFile = new File(this.getApplication().getFilesDir().getAbsolutePath() + "/Settings");
        String swNotifText = "true";
        String swSuggText = "true";

        try {
            String name, date, hour, minute, progress, time;
            Integer timeInt;
            Integer timeTotal = 0;
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
                reader.readLine();
                time = reader.readLine();

                inbox.addLine(name + " is due " + date + " at " + hour + ":" + minute);


               // Calendar upweek = new GregorianCalendar();
                //upweek.add(Calendar.DATE, 7);
                Calendar calendarDate = Calendar.getInstance(TimeZone.getTimeZone("EST"), Locale.US);
                calendarDate.set(calendarDate.YEAR, calendarDate.MONTH, calendarDate.DATE + 7);
                Date currentDate = calendarDate.getTime();
                dateString = dateFormat.format(currentDate);

//                This is meant to grab the current date and add 7 days to it.
//                Then we will be able to compare the date of the assignment and see if it is within the current week
//                Can you try to figure out how to get the current date and add a week to it?




                try {
                    if (dateFormat.parse(date).before(dateFormat.parse(dateString))) {
                        timeInt = Integer.parseInt(time);
                        timeTotal = timeTotal + timeInt;
                    }
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }

            String hours;
            Integer hoursInt, suggTime;
            in2 = new FileInputStream(settingsFile);
            BufferedReader newreader = new BufferedReader(new InputStreamReader(in2));
            hours = newreader.readLine();
            swNotifText = newreader.readLine();
            swSuggText = newreader.readLine();

            hoursInt = Integer.parseInt(hours);
            suggTime = timeTotal / hoursInt;

            if (((hoursInt * 7) > timeTotal) && swSuggText == "true"){
                inbox.addLine("");
                inbox.addLine("You have " + Integer.toString(timeTotal) + " hours of homework due in the next week and you " +
                        "only want to work for " + Integer.toString((hoursInt * 7)) + " total hours this week." +
                        "I suggest you work " + Integer.toString(suggTime) + " hours per day for the rest of the week.");
            }

            notifBuilder.setStyle(inbox);

        } catch (NullPointerException e){
            notifBuilder.setContentText("You have no Assignments Due Today.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        //Send Notification if settings allows it
        if (swNotifText.equals("true")){
            notifManager.notify(notificationID, notifBuilder.build());
        }

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
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentA, "MainTaskView").commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentB, "EditTask").commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentC, "Settings").commit();
        } else if (index == 3){
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentD, "DatePicker").commit();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment){

        Log.d("MainActivity", "PositiveClick");
        File deleteLocation = new File(taskDir + "/" + check);
        if (deleteLocation.exists())
            deleteLocation.delete();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment newInstance = recreateFragment(fragmentA);
        ft.remove(fragmentA);
        ft.add(R.id.activity_main, newInstance);
        ft.commit();
        fragmentA = (mainTaskView) newInstance;
        //getSupportFragmentManager().beginTransaction().remove(fragmentA).commit();
      //  getSupportFragmentManager().beginTransaction().add(R.id.activity_main, rec)

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment){
        Log.d("MainActivity", "NegativeClick");
        onFragmentChanged(1);
    }

    public void setFile(String string){
        check = new File(string);
    }

    private Fragment recreateFragment(Fragment f)
    {
        try {
            Fragment.SavedState savedState = getSupportFragmentManager().saveFragmentInstanceState(f);

            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        }
        catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
        }
    }




    @Override
    public void onDialogYesClick(DialogFragment dialogFragment) {

        overwrite.delete();
        check = null;

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

    @Override
    public void onDialogNoClick(DialogFragment dialogFragment) {
        //Do nothing :0
    }
}
