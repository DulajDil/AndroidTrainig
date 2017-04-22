package com.igor.vetrov.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "com.igor.vetrov.criminalintent.time";
    private static final String ARG_TIME = "time";
    private static final int CRIME_TIME = -2;

    private TimePicker mTimePicker;
    private Calendar mCalendar;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_date_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(mCalendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
//                .setPositiveButton(android.R.string.ok, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = mTimePicker.getHour();
                                mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                                int minute = mTimePicker.getMinute();
                                mCalendar.set(Calendar.MINUTE, minute);
                                sendResult(CRIME_TIME, mCalendar.getTime());
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
