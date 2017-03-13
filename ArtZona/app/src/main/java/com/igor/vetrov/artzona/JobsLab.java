package com.igor.vetrov.artzona;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class JobsLab {

    private static JobsLab sJobsLab;
    private final Context mAppContext;
    private List<Jobs> mJobses;

    public static JobsLab get(Context context) {
        if (sJobsLab == null) {
            sJobsLab = new JobsLab(context);
        }
        return sJobsLab;
    }

    private JobsLab(Context context) {
        mAppContext = context;
        mJobses = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Jobs jobs = new Jobs();
            jobs.setmTitle("Jobs #" + i);
            mJobses.add(jobs);
        }
    }

    public List<Jobs> getmJobses() {
        return mJobses;
    }
}
