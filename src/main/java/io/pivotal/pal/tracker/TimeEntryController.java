package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repo;
    private DistributionSummary distributionSummary;
    private Counter actionCounter;


    public TimeEntryController(TimeEntryRepository repo, MeterRegistry meterRegistry) {
        this.repo = repo;

        this.distributionSummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody  TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = repo.create(timeEntryToCreate);
        actionCounter.increment();
        distributionSummary.record(repo.list().size());
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.CREATED);

    }

    @GetMapping("{TIME_ENTRY_ID}")
    public ResponseEntity<TimeEntry> read(@PathVariable("TIME_ENTRY_ID") long timeEntryId) {
        TimeEntry timeEntry = repo.find(timeEntryId);
        actionCounter.increment();
        if (timeEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }

    @PutMapping("{TIME_ENTRY_ID}")
    public ResponseEntity update(@PathVariable("TIME_ENTRY_ID") long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updateValue = repo.update(timeEntryId, expected);
        if (updateValue == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity(updateValue, HttpStatus.OK);
    }

    @DeleteMapping("{TIME_ENTRY_ID}")
    public ResponseEntity delete(@PathVariable("TIME_ENTRY_ID") long timeEntryId) {
        repo.delete(timeEntryId);
        actionCounter.increment();
        distributionSummary.record(repo.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(repo.list(), HttpStatus.OK);
    }

}
