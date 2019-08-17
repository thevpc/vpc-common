package net.vpc.common.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlReader {
    Object read(ResultSet r, int column) throws SQLException;
}
