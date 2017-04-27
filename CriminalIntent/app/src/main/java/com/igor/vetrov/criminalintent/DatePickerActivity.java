package com.igor.vetrov.criminalintent;


import android.support.v4.app.Fragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_DATE = "com.igor.vetrov.criminalintent.crime_date";

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_DATE);
        return DatePickerFragment2.newInstance(date);
    }
}
