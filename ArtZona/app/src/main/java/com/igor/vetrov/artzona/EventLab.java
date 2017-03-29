package com.igor.vetrov.artzona;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventLab {

    private static EventLab sEventLab;
    private final Context mAppContext;
    private List<Event> mEvents;

    public static EventLab get(Context context) {
        if (sEventLab == null) {
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    private EventLab(Context context) {
        mAppContext = context;
        mEvents = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Event event = new Event();
            event.setmTitle("Event #" + i);
            mEvents.add(event);
        }
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public Event getEvent(UUID id) {
        for (Event event : mEvents) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }
}
