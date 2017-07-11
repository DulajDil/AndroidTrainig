package com.igor.vetrov.nerdlauncher;


import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment creFragment() {
        return  NerdLauncherFragment.newInstance();
    }
}
