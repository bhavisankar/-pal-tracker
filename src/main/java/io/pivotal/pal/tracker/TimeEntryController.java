package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repo;


    public TimeEntryController(TimeEntryRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody  TimeEntry timeEntryToCreate) {
           return new ResponseEntity<TimeEntry>(repo.create(timeEntryToCreate), HttpStatus.CREATED);

    }

    @GetMapping("{TIME_ENTRY_ID}")
    public ResponseEntity<TimeEntry> read(@PathVariable("TIME_ENTRY_ID") long timeEntryId) {
        TimeEntry timeEntry = repo.find(timeEntryId);
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
        return new ResponseEntity(updateValue, HttpStatus.OK);
    }

    @DeleteMapping("{TIME_ENTRY_ID}")
    public ResponseEntity delete(@PathVariable("TIME_ENTRY_ID") long timeEntryId) {
        repo.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(repo.list(), HttpStatus.OK);
    }

}
