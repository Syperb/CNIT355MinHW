package com.example.cnit355.minhw;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    mainTaskView fragmentA;
    EditTask fragmentB;
    activity_settings fragmentC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentA = (mainTaskView) getSupportFragmentManager().findFragmentById(R.id.MainFragment);
        fragmentB = new EditTask();
        fragmentC = new activity_settings();
    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentA).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentB).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragmentC).commit();
        }
    }
}
