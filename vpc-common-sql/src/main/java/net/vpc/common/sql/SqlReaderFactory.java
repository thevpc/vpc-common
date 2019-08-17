package net.vpc.common.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SqlReaderFactory {
    public static SqlReaderFactory INSTANCE = new SqlReaderFactory();

    public SqlReader[] resolveReader(ResultSetMetaData md) throws SQLException {
        int columnCount = md.getColumnCount();
        SqlReader[] readers = new SqlReader[columnCount];
        for (int i = 0; i < columnCount; i++) {
            Class<?> clz = null;
            try {
                clz = Class.forName(md.getColumnClassName(i + 1));
            } catch (ClassNotFoundException e) {
                System.err.println(e);
            }
            readers[i] = SqlReaderFactory.INSTANCE.resolveReader(clz == null ? Object.class : clz, md.getColumnType(i + 1));
        }
        return readers;
    }

    public SqlReader resolveReader(Class clz, int sqlType) {
        if (java.sql.Date.class.equals(clz)) {
            return SqlDateReader.INSTANCE;
        }
        if (java.sql.Time.class.equals(clz)) {
            return SqlTimeReader.INSTANCE;
        }
        if (java.sql.Timestamp.class.equals(clz)) {
            return SqlTimestampReader.INSTANCE;
        }
        if (java.util.Date.class.isAssignableFrom(clz)) {
            return SqlUtilDateReader.INSTANCE;
        }
        if (byte[].class.isAssignableFrom(clz)) {
            return SqlBytesReader.INSTANCE;
        }
        return SqlObjectReader.INSTANCE;
    }

    public static class SqlTimestampReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlTimestampReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getTimestamp(column);
        }
    }

    public static class SqlTimeReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlTimeReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getTime(column);
        }
    }

    public static class SqlDateReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlDateReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getDate(column);
        }
    }

    public static class SqlObjectReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlObjectReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getObject(column);
        }
    }

    public static class SqlBytesReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlBytesReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getBytes(column);
        }
    }

    public static class SqlUtilDateReader implements SqlReader {
        public static final SqlReader INSTANCE = new SqlUtilDateReader();

        public Object read(ResultSet r, int column) throws SQLException {
            return r.getTimestamp(column);
        }

    }
}
