package com.xuanzhi.tools.dbpool;

import java.sql.*;

public interface ConnectionValidator {

    /**
     * called when a SQLException is caught.  returns true if the Connection
     * passed in doesn't work properly.
     */
    public boolean connectionIsBroken(Connection conn, SQLException e);

}
