package com.igor.vetrov.artzona;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class JobsListFragment extends Fragment {

    private RecyclerView mJobsRecyclerView;
    private JobsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs_list, container, false);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.jobs_recycler_view);
        mJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        JobsLab jobsLab = JobsLab.get(getActivity());
        List<Jobs> jobses = jobsLab.getmJobses();
        mAdapter = new JobsAdapter(jobses);
        mJobsRecyclerView.setAdapter(mAdapter);
    }

    private class JobsHolder extends RecyclerView.ViewHolder {

        public TextView mTitleTextView;

        public JobsHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }

    private class JobsAdapter extends RecyclerView.Adapter<JobsHolder> {

        private List<Jobs> mJobses;

        public JobsAdapter(List<Jobs> jobses) {
            mJobses = jobses;
        }

        @Override
        public JobsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new JobsHolder(view);
        }
        @Override
        public void onBindViewHolder(JobsHolder holder, int position) {
            Jobs jobs = mJobses.get(position);
            holder.mTitleTextView.setText(jobs.getmTitle());
        }
        @Override
        public int getItemCount() {
            return mJobses.size();
        }
    }
}
