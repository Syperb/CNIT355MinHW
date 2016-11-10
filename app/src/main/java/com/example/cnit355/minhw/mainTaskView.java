package com.example.cnit355.minhw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class mainTaskView extends AppCompatActivity {

    EditTask fragmentA;
    activity_settings fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task_view);

        fragmentA = new EditTask();
        fragmentB = new activity_settings();
    }

    public void newAssignment(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragmentA).commit();
    }

    public void goToSettings(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragmentB).commit();
    }
}
