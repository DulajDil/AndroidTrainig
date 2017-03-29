package com.igor.vetrov.artzona;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class EventsListFragment extends Fragment {

    private RecyclerView mEventRecyclerView;
    private EventAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        EventLab eventLab = EventLab.get(getActivity());
        List<Event> events = eventLab.getEvents();
        mAdapter = new EventAdapter(events);
        mEventRecyclerView.setAdapter(mAdapter);
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTitleTextView;
        private Event mEvent;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mEvent.getTitle() + " кликнут!", Toast.LENGTH_SHORT).show();
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new EventHolder(view);
        }
        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = mEvents.get(position);
            holder.mTitleTextView.setText(event.getTitle());
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }
}
