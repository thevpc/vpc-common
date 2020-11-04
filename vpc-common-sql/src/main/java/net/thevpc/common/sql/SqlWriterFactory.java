package net.thevpc.common.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class SqlWriterFactory {
    public static final SqlWriter STRING_SQL_WRITER = new SqlWriter() {
        @Override
        public void write(PreparedStatement ps, int index, Object value) throws SQLException {
            ps.setString(index, value == null ? null : String.valueOf(value));
        }
    };
    public static final SqlWriter TIMESTAMP_SQL_WRITER = new SqlWriter() {
        @Override
        public void write(PreparedStatement ps, int index, Object value) throws SQLException {
            ps.setTimestamp(index, (Timestamp) value);
        }
    };
    public static final SqlWriter DOUBLE_SQL_WRITER = new SqlWriter() {
        @Override
        public void write(PreparedStatement ps, int index, Object value) throws SQLException {
            if (value == null) {
                ps.setNull(index, Types.INTEGER);
            } else {
                ps.setDouble(index, (Double) value);
            }
        }
    };
    public static final SqlWriter LONG_SQL_WRITER = new SqlWriter() {
        @Override
        public void write(PreparedStatement ps, int index, Object value) throws SQLException {
            if (value == null) {
                ps.setNull(index, Types.INTEGER);
            } else {
                ps.setLong(index, (Long) value);
            }
        }
    };
}
