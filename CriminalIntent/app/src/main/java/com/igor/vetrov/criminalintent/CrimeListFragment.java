package com.igor.vetrov.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CRIME = 1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private int position;

    private Button mAddCrime;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private UUID mEditCrimeId;
    private Date mEditCrimeDate;
    private Callbacks mCallbacks;

    /**
     * Обязательный интерфейс для активности-хоста.
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime, boolean mSubtitleVisible);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent2 = getActivity().getIntent();
        if (intent2.getExtras() != null) {
            if (intent2.getSerializableExtra(CrimeFragment.EXTRA_SUBTITLE) != null) {
                mSubtitleVisible = (boolean) intent2.getSerializableExtra(CrimeFragment.EXTRA_SUBTITLE);
            }
        }
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * получаем список прустплений
     */
    private List<Crime> getCrimes() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        return crimes;
    }

    /**
     * отображение кнопки добавления нового преступления
     * если список с преступлениями не пустой кнопка скрыта
     * если список пустой добавляем кнопку и слушателя
     */
    private void showAddCrimeButton(List<Crime> crimes) {
        if (crimes.size() > 0) {
            mAddCrime.setVisibility(View.GONE);
        } else {
            mAddCrime.setVisibility(View.VISIBLE);
            mAddCrime.setOnClickListener(v -> {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId(), mSubtitleVisible);
//                startActivity(intent);
                mCallbacks.onCrimeSelected(crime, mSubtitleVisible);
                updateUI();
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<Crime> crimes = getCrimes();
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mAddCrime = (Button) view.findViewById(R.id.add_crime);
        showAddCrimeButton(crimes);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId(), mSubtitleVisible);
//                startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime, mSubtitleVisible);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * обновление загалока каунтера количества текущих пруступлений
     */
    private void updateSubtitle() {
        int crimeSize = getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    /**
     * обновляем UI
     * getCrimes() получаем список перступлений
     * showAddCrimeButton отображение кпоки добавления преступлений
     */
    public void updateUI() {
        List<Crime> crimes = getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyItemChanged(position);
        }
        updateSubtitle();
        showAddCrimeButton(crimes);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public TextView mTitleTextView;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
//            mTitleTextView = (TextView) itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_list_item_titleTextView);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_list_item_dateTextView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.crime_list_item_timeTextView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getStringDateFormat());
            mTimeTextView.setText(mCrime.getTime());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Назначение флага раскрытия преступления
                mCrime.setSolved(isChecked);
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.updateCrime(mCrime);
//                updateUI();
            });
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(), mCrime.getTitle() + " кликнут!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getActivity(), CrimeActivity.class);
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());

//            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId(), mSubtitleVisible);
            position = CrimeLab.get(getActivity()).getPosition(mCrime.getId());
//            startActivityForResult(intent, REQUEST_CRIME);
            mCallbacks.onCrimeSelected(mCrime, mSubtitleVisible);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position2) {
            Crime crime = mCrimes.get(position2);
            if (position == position2) {
                if (mEditCrimeDate != null) {
                    crime.setDate(mEditCrimeDate);
                }
            }
//            holder.mDateTextView.setText(crime.getTitle());
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            if (resultCode == 0) {
                mEditCrimeDate = null;
                return;
            } else if (resultCode == CrimeFragment.RESULT_CHANGE_TITLE) {
                position = (int) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_POSITION);
            } else {
                mEditCrimeDate = (Date) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_DATE);
                mEditCrimeId = (UUID) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
                mSubtitleVisible = (boolean) data.getSerializableExtra(CrimeFragment.EXTRA_SUBTITLE);
//                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
//            String stringDateFormat = dateFormat.format(mEditCrimeDate);
//            Toast.makeText(getActivity(), String.format("Изменена дата на:\n%s!", stringDateFormat), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
