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

public class mainTaskView extends Fragment implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener{
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_task_view, container,
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
        final ArrayList taskList = new ArrayList<String>();

        //Creates a file array for storing file names
        File[] listFiles = new File(rootView.getContext().getFilesDir().getAbsolutePath() + "/Tasks").listFiles();
        Toast.makeText(getContext(), rootView.getContext().getFilesDir().getAbsolutePath().toString() + "/Tasks", Toast.LENGTH_LONG).show();
        String fileName, extName;
        try {

            String name, date, hour, minute, progress, time;
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
                progress = reader.readLine();

//                if (Integer.parseInt(hour) > 12){
//                    hour = (Integer.parseInt(hour) - 12)
//                }


                fileName = padRight(name+":", 20) + date + " at " + hour + ":" + minute + "       " + progress + "%";
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
        final ListView listViewMP3 = (ListView) rootView.findViewById(R.id.listViewTask);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
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


                ConfirmDeleteDialogFragment dialogFragment = new ConfirmDeleteDialogFragment();
                dialogFragment.show(getFragmentManager(), "DialogFragment" );
               // ((MainActivity)getActivity().setCheck());

                String filename = taskList.get(pos).toString();

                filename = filename.substring(0, filename.indexOf(":")).replaceAll("\\s","");
                listener.setFile(filename);


                adapter.notifyDataSetChanged();


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


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment){

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment){
        MainActivity activity = (MainActivity) getActivity();
        Log.d("mainTaskView", "PositiveClick");
        activity.onFragmentChanged(1);


    }

    public interface retrieveTaskListener{
        public void setFile(String string);
    }

    retrieveTaskListener listener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (retrieveTaskListener) context;
        } catch (ClassCastException castException){
            throw new ClassCastException();
        }
    }

    public void refresh(){
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();


        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(this);
        fragTransaction.attach(this);
        fragTransaction.commit();

    }



}

