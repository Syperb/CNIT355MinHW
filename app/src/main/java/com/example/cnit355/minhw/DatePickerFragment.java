package com.example.cnit355.minhw;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DatePickerFragment extends Fragment {

    CalendarView realDate;
    String message;
    SimpleDateFormat date;
    EditTask editTask;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_date_picker_fragment, container,
                false);

        editTask = new EditTask();
        realDate = (CalendarView)rootView.findViewById(R.id.calendarView);
        Log.d("DatePicker", "Inside datepicker onCreateView method");

        //onCLick method for Back Button
        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(1);
            }
        });

        //OnDateChange listener for the CalendarView
        realDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                MainActivity activity = (MainActivity) getActivity();

                //Changes the text of the button to the selected day
                activity.dateString = Integer.toString(month + 1) +
                        "/" + Integer.toString(dayOfMonth) +
                        "/" + Integer.toString(year);
            }
        });

        return rootView;
        }


}
