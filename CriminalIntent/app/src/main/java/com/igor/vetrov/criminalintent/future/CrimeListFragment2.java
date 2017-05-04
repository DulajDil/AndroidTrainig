//package com.igor.vetrov.criminalintent.future;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import com.igor.vetrov.criminalintent.Crime;
//import com.igor.vetrov.criminalintent.CrimeFragment;
//import com.igor.vetrov.criminalintent.CrimeLab;
//import com.igor.vetrov.criminalintent.CrimePagerActivity;
//import com.igor.vetrov.criminalintent.R;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//public class CrimeListFragment2 extends Fragment {
//
//    private static final int REQUEST_CRIME = 1;
//    private int position;
//
//    private RecyclerView mCrimeRecyclerView;
//    private CrimeListFragment2.CrimeAdapter mAdapter;
//    private UUID mEditCrimeId;
//    private Date mEditCrimeDate;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
//        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
//        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        updateUI();
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        updateUI();
//    }
//
//    private void updateUI() {
//        CrimeLab crimeLab = CrimeLab.get(getActivity());
//        List<Crime> crimes = crimeLab.getCrimes();
//
//        if (mAdapter == null) {
//            mAdapter = new CrimeListFragment2.CrimeAdapter(crimes);
//            mCrimeRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.notifyItemChanged(position);
//        }
//    }
//
//    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        private TextView mTitleTextView;
//        private TextView mDateTextView;
//        private TextView mTimeTextView;
//        private CheckBox mSolvedCheckBox;
//        private Crime mCrime;
//
//        public CrimeHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_list_item_titleTextView);
//            mDateTextView = (TextView) itemView.findViewById(R.id.crime_list_item_dateTextView);
//            mTimeTextView = (TextView) itemView.findViewById(R.id.crime_list_item_timeTextView);
//            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
//        }
//
//        public void bindCrime(Crime crime) {
//            mCrime = crime;
//            mTitleTextView.setText(mCrime.getTitle());
//            mDateTextView.setText(mCrime.getStringDateFormat());
//            mTimeTextView.setText(mCrime.getTime());
//            mSolvedCheckBox.setChecked(mCrime.isSolved());
//        }
//
//        @Override
//        public void onClick(View v) {
//            Fragment fragment = CrimeFragment.newInstance(mCrime.getId());
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            fm.beginTransaction()
//                    .add(R.id.detail_fragment_container, fragment)
//                    .commit();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CRIME) {
//            if (resultCode == 0) {
//                mEditCrimeDate = null;
//                return;
//            }
//            mEditCrimeDate = (Date) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_DATE);
//            mEditCrimeId = (UUID) data.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
//        }
//    }
//
//    private class CrimeAdapter extends RecyclerView.Adapter<CrimeListFragment2.CrimeHolder> {
//
//        private List<Crime> mCrimes;
//
//        public CrimeAdapter(List<Crime> crimes) {
//            mCrimes = crimes;
//        }
//
//        @Override
//        public CrimeListFragment2.CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
//            return new CrimeListFragment2.CrimeHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(CrimeListFragment2.CrimeHolder holder, int position2) {
//            Crime crime = mCrimes.get(position2);
//            if (position == position2) {
//                if (mEditCrimeDate != null) {
//                    crime.setDate(mEditCrimeDate);
//                }
//            }
//            holder.bindCrime(crime);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mCrimes.size();
//        }
//    }
//
//}
