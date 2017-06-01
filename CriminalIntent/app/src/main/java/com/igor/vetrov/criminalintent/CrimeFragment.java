package com.igor.vetrov.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_SUBTITLE = "subtitle_visible";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    public static final String EXTRA_CRIME_DATE =
            "com.igor.vetrov.criminalintent.crime_date";
    public static final String EXTRA_CRIME_ID =
            "com.igor.vetrov.criminalintent.crime_id";
    public static final String EXTRA_SUBTITLE =
            "com.igor.vetrov.criminalintent.subtitle_visible";
    public static final String EXTRA_CRIME_POSITION =
            "com.igor.vetrov.criminalintent.crime_position";
    public static final int RESULT_CHANGE_TITLE = 7;
    private static final int REQUEST_CONTACT = 9;
    private static final int REQUEST_CONTACT_CALL = 11;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private boolean mSubtitleVisible;

    private String titleBefore;
    private String titleAfter;
    private String phoneNumber;
    private String phoneID;
    private int position;

    public static CrimeFragment newInstance(UUID crimeId, boolean subtitleVisible) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putSerializable(ARG_SUBTITLE, subtitleVisible);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime = new Crime();
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mSubtitleVisible = (boolean) getArguments().getSerializable(ARG_SUBTITLE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                titleBefore = c.toString();
            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());            }

            @Override
            public void afterTextChanged(Editable c) {
                titleAfter = c.toString();
                if (!titleBefore.equals(titleAfter)) {
                    mCrime.setTitle(titleAfter);
                    CrimeLab.get(getActivity()).changeCrime(mCrime);
                    position = CrimeLab.get(getActivity()).getPosition(mCrime.getId());
                    Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                    intent.putExtra(EXTRA_CRIME_POSITION, position);
                    getActivity().setResult(RESULT_CHANGE_TITLE, intent);
                } else {
                    return;
                }
            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        if (mDateButton != null) {
//            mDateButton.setText(mCrime.getStringDateFormat());
            updateDate();
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    DatePickerFragment2 dialog = DatePickerFragment2.newInstance(mCrime.getDate(), mCrime.getId(), mSubtitleVisible);
                    dialog.setTargetFragment(CrimeFragment.this, 0);
                    dialog.show(manager, DIALOG_DATE);
                }
            });
        }

        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        if (mTimeButton != null) {
            mTimeButton.setText(mCrime.getTime());
            mTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, 0);
                    dialog.show(manager, DIALOG_TIME);
                }
            });
        }

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Назначение флага раскрытия преступления
                 mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(v1 -> {
            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setType("text/plain")
                    .setSubject(getString(R.string.crime_report_subject))
                    .setText(getCrimeReport())
                    .startChooser();
                });
//        mReportButton.setOnClickListener(v1 -> {
//            Intent i = new Intent(Intent.ACTION_SEND);
//            i.setType("text/plain");
//            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//            i = Intent.createChooser(i, getString(R.string.send_report));
//            startActivity(i);
//        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(v1 -> {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        });
        if (mCrime.getSuspect() != null) {
            mSolvedCheckBox.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

//        Uri call = Uri.parse("tel: 79152794565");
        final Intent pickContact2 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mCallButton = (Button) v.findViewById(R.id.crime_call);
        mCallButton.setOnClickListener(v1 -> {
            startActivityForResult(pickContact2, REQUEST_CONTACT_CALL);
        });
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Intent i = new Intent(getActivity(), CrimeListActivity.class);
                startActivity(i);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment2.DATE_REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(EXTRA_CRIME_DATE);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            String stringDateFormat = dateFormat.format(date);
            Toast.makeText(getActivity(), String.format("Дата изменена на: %s", stringDateFormat), Toast.LENGTH_SHORT).show();
            mCrime.setDate(date);
            updateDate();
            returnResult(date, mCrime.getId());
        } else if (requestCode == TimePickerFragment.TIME_REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
            returnResult(date, mCrime.getId());
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            getContactName(data);
        } else if (requestCode == REQUEST_CONTACT_CALL && data != null) {
            getContactInfo(data);
        }
    }

    private void getContactName(Intent intent) {
        Uri contactUri = intent.getData();
        // Определение полей, значения которых должны быть
        // возвращены запросом.
        String[] queryFields = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME
        };
        // Выполнение запроса - contactUri здесь выполняет функции
        // условия "where"
        Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
        try {
            // Проверка получения результатов
            if (c.getCount() == 0) {
                return;
            }
            // Извлечение первого столбца данных - имени подозреваемого.
            c.moveToFirst();
            String suspect = c.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
        } finally {
            c.close();
        }
    }

    private void getContactInfo(Intent intent) {
        Uri contactUri = intent.getData();
        String[] queryFields = new String[]{ContactsContract.Contacts._ID};
        Cursor cursorID = getActivity().getContentResolver().query(contactUri, queryFields
                , null, null, null);
        try {
            if (cursorID.getCount() == 0) {
                return;
            }
            cursorID.moveToFirst();
            phoneID = cursorID.getString(0);
        } finally {
            cursorID.close();
        }
        Log.d(TAG, "Contact ID: " + phoneID);

        String[] queryFields2 = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursorPhone = getActivity().getContentResolver().query
                        ( ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        , queryFields2
                        , ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ phoneID
                        , null
                        , null );

        try {
            if (cursorPhone.getCount() == 0) {
                return;
            }
            cursorPhone.moveToFirst();
            phoneNumber = cursorPhone.getString(0);
        } finally {
            cursorPhone.close();
        }
//        Cursor cursorPhone = getActivity().getContentResolver().query
//                ( ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//                , queryFields2
//                , null
//                , new String[]{phoneID}
//                , null );
//        if(cursorPhone.moveToNext()) {
//            phoneNumber = cursorPhone.getString(cursorPhone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        }
    }

    public void returnResult(Date date, UUID id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME_DATE, date);
        intent.putExtra(EXTRA_CRIME_ID, id);
        intent.putExtra(EXTRA_SUBTITLE, mSubtitleVisible);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void updateDate() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        String stringDateFormat = dateFormat.format(mCrime.getDate());
        mDateButton.setText(stringDateFormat);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updateTime() {
        mTimeButton.setText(mCrime.getTime());
    }
}
