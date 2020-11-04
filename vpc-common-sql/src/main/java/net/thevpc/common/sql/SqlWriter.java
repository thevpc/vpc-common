package net.thevpc.common.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlWriter {
    void write(PreparedStatement ps, int index, Object value) throws SQLException;
}
