package com.igor.vetrov.criminalintent.future;

import android.support.v4.app.Fragment;

import com.igor.vetrov.criminalintent.R;

public class CrimeListActivity2 extends SingleFragmentActivity2 {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment2();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
