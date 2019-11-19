package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.List;

public interface TimeEntryRepository {

    public TimeEntry create(TimeEntry any);

    public TimeEntry find(long timeEntryId);

    public List<TimeEntry> list();


    public TimeEntry update(long id, TimeEntry value);

    public void delete(long timeEntryId);
}
