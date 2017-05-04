package com.igor.vetrov.criminalintent;


import android.support.v4.app.Fragment;

import java.util.Date;
import java.util.UUID;

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_DATE = "com.igor.vetrov.criminalintent.crime_date";

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_DATE);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return DatePickerFragment2.newInstance(date, crimeId);
    }
}
