package com.igor.vetrov.criminalintent.old;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.igor.vetrov.criminalintent.R;

public class CrimeActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new CrimeFragment2();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        Log.i("onCreate", "onCreate CrimeActivity2");
    }
}
