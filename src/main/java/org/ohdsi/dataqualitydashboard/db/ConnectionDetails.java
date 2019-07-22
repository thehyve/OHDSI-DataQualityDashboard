package org.ohdsi.dataqualitydashboard.db;

import lombok.Data;

@Data
public class ConnectionDetails {

    private final String dbms;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String schema;
}
