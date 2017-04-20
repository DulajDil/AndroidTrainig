package com.igor.vetrov.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CRIME = 1;
    private int position;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private UUID mEditCrimeId;
    private Date mEditCrimeDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(position);
        }


//        mCrimeRecyclerView.getAdapter().notifyItemMoved(0, 5);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public TextView mTitleTextView;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
//            mTitleTextView = (TextView) itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_list_item_titleTextView);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_list_item_dateTextView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getStringDateFormat());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(), mCrime.getTitle() + " кликнут!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getActivity(), CrimeActivity.class);

//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());

            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());

            position = CrimeLab.get(getActivity()).getPosition(mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            if (resultCode == 0) {
                mEditCrimeDate = null;
                return;
            }
            mEditCrimeDate = (Date) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_DATE);
            mEditCrimeId = (UUID) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            String stringDateFormat = dateFormat.format(mEditCrimeDate);
            Toast.makeText(getActivity(), String.format("Изменена дата на:\n%s!", stringDateFormat), Toast.LENGTH_SHORT).show();
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
    }
}
