package com.igor.vetrov.criminalintent;


import android.support.v4.app.Fragment;

import java.util.Date;
import java.util.UUID;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_DATE);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        boolean mSubtitleVisible = (boolean) getIntent().getSerializableExtra(CrimeFragment.EXTRA_SUBTITLE);
        return DatePickerFragment2.newInstance(date, crimeId, mSubtitleVisible);
    }
}
