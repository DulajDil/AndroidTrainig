package com.igor.vetrov.artzona;

import android.support.v4.app.Fragment;

public class JobsListActivity extends SingleFragmentActivity{

    @Override
    public Fragment createFragment() {
        return new JobsListFragment();
    }
}
