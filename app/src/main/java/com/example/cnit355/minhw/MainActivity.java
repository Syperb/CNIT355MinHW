package com.example.cnit355.minhw;

import android.app.Notification;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    SimpleDateFormat date;
    NotificationCompat.Builder notifBuilder;
    File check;
    File overwrite;
    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Data Definition
        fragmentA = (mainTaskView) getSupportFragmentManager().findFragmentById(R.id.MainFragment);
        fragmentB = new EditTask();
        fragmentC = new activity_settings();
        fragmentD = new DatePickerFragment();
        taskName = (TextView) findViewById(R.id.txtName);
        date = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        notifBuilder = new NotificationCompat.Builder(this);
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();
        int notificationID = 1;

        //Create Notification
        notifBuilder.setSmallIcon(R.drawable.calendar);
        notifBuilder.setContentTitle("MinHW");
        notifBuilder.setContentText("Assignments Due Today");
        inbox.setBigContentTitle("Assignments:");

        File[] listFiles = new File(this.getApplicationContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();

        try {

            String name, date, hour, minute, progress, time;
            Integer timeInt;
            Integer timeTotal = 0;
            InputStream in;

            for (File file : listFiles) {
                //Line 3: Date, Line 4: hour, Line 5: minute, Line 7: progress

                in = new FileInputStream(file);

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                name = reader.readLine();
                reader.readLine();
                date = reader.readLine();
                hour = reader.readLine();
                minute = reader.readLine();
                reader.readLine();
                time = reader.readLine();

                inbox.addLine(name + " is due " + date + " at " + hour + ":" + minute);

                timeInt = parseHours(time);
                timeTotal = timeTotal + timeInt;

            }

            //Get Suggested number of hours worked per day from Settings
            //Compare that number to the timeTotal
            //Add onto the notification with a space and then the suggestion
            notifBuilder.setStyle(inbox);
        }

        catch (NullPointerException e) {
            notifBuilder.setContentText("You have no Assignments Due Today.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Send Notification
        notifManager.notify(notificationID, notifBuilder.build());

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

    public Integer parseHours(String n){
        Integer i = 0;
        n = n.trim();
        n = n.substring(0, 1);
        n = n.trim();
        try {
            i = Integer.parseInt(n);
            return i;
        }
        catch(NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), "You must enter a 1 or 2 digit number for Time to Completion", Toast.LENGTH_LONG).show();
            return i;
        }
    }


    @Override
    public void onDialogYesClick(DialogFragment dialogFragment) {

        overwrite.delete();
        check = null;
    }

    @Override
    public void onDialogNoClick(DialogFragment dialogFragment) {
        //Do nothing :0
    }
}
