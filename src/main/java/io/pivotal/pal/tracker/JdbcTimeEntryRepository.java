package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_QRY = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
    private static final String LIST_QRY = "SELECT id, project_id, user_id, date, hours FROM time_entries";
    private static final String FIND_QRY = LIST_QRY + " WHERE id = ?";
    private static final String DELETE_QRY = "DELETE FROM time_entries WHERE id = ?";
    private static final String UPDATE_QRY = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?,  hours = ? WHERE id = ?";

    final RowMapper<TimeEntry> mapper = (rs, rNo) -> {

       return new TimeEntry(
        rs.getLong("id"),
        rs.getLong("project_id"),
        rs.getLong("user_id"),
        rs.getDate("date").toLocalDate(),
        rs.getInt("hours"));

    };

    final ResultSetExtractor<TimeEntry> extractor = (rs) -> {
        return rs.next() ? mapper.mapRow(rs, rs.getRow()) : null;
    };

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keys = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement statement = con.prepareStatement(INSERT_QRY, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            return statement;
        };
        jdbcTemplate.update(preparedStatementCreator, keys);
        return find(keys.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return this.jdbcTemplate.query(FIND_QRY, new Object[]{timeEntryId}, extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(LIST_QRY, mapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        jdbcTemplate.update(UPDATE_QRY,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update(DELETE_QRY, timeEntryId);
    }
}
