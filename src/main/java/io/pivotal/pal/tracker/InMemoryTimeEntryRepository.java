package io.pivotal.pal.tracker;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements  TimeEntryRepository {

    private Map<Long, TimeEntry> store;
    private AtomicLong counter;

    public InMemoryTimeEntryRepository() {
        this.store = new HashMap<>();
        this.counter = new AtomicLong();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() == 0) {
            timeEntry.setId(counter.incrementAndGet());
        }
        this.store.put(timeEntry.getId(), timeEntry);
        return  timeEntry;
    }

    public TimeEntry find(long id) {
        return  this.store.get(id);
    }

    public List<TimeEntry> list() {
        return this.store.values().stream().collect(Collectors.toList());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry prevEntry = this.store.get(id);
        if (prevEntry == null) {
            return prevEntry;
        }
        timeEntry.setId(id);
        this.store.put(timeEntry.getId(), timeEntry);
        return  timeEntry;
    }

    public void delete(long id) {
        this.store.remove(id);
    }
}
