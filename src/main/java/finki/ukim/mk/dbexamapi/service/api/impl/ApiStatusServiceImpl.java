package finki.ukim.mk.dbexamapi.service.api.impl;

import finki.ukim.mk.dbexamapi.service.api.ApiStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
public class ApiStatusServiceImpl implements ApiStatusService {

    private static final int VALIDATION_TIMEOUT_SECONDS = 3;

    private final DataSource dataSource;

    public ApiStatusServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isDatabaseReachable() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(VALIDATION_TIMEOUT_SECONDS);
        } catch (SQLException exception) {

            log.warn("Database reachability check failed", exception);
            return false;
        }
    }
}
