package net.thevpc.common.log;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class JdbcLog extends Log {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String codeColumn="LOGCode";
    private String dateTimeColumn="LOGDate";
    private String messageColumn="LOGMessage";
    private String typeColumn="LOGType";
    private String userIdColumn="LOGUserId";
    private String userNameColumn="LOGUserName";

    private String tableName="LogTbl";

    public JdbcLog(String name) {
        this(name,name,name,DEFAULT_PATTERN,null);
    }

    public JdbcLog(String name, String title, String desc, String pattern, String[] acceptedTypes) {
        super(name, title, desc, "DB", pattern, acceptedTypes);
    }

    public void processLog(String type, String message, int logLevel, Date date, long delta, Thread thread, StackTrace stack,String user_id,String user_name) {
        PreparedStatement ps=getPersistPreparedStatement();
        if(ps!=null){
            try {
                ps.setTimestamp(1,new java.sql.Timestamp(date.getTime()));
                ps.setString(2,user_id);
                ps.setString(3,user_name);
                ps.setString(4,type);
                ps.setString(5,message);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("TableLog Failed : " + e.toString());
            }
        }
    }

    public void setConnection(Connection connection){
        this.connection=connection;
    }

    public Connection getConnection(){
        return connection;
    }

    public PreparedStatement  getPersistPreparedStatement(){
        if (preparedStatement==null){
            try {
                Connection c=getConnection();
                if(c!=null){
                    preparedStatement=c.prepareStatement("Insert Into "+tableName+"(" +
                            dateTimeColumn+","+
                            userIdColumn+","+
                            userNameColumn+","+
                            typeColumn+","+
                            messageColumn+
                            ") values(?,?,?,?,?)"
                    );
                }
            } catch (SQLException e) {
                System.err.println("TableLog getInsertPreparedStatement Failed : " + e.toString());
            }
        }
        return preparedStatement;
    }

//    public void loadConfig() {
//        super.loadConfig();
//    }


    public synchronized void clear() {
        Statement stm=null;
        try {
            stm=getConnection().createStatement();
            stm.executeUpdate("Delete From "+tableName);
        } catch (NullPointerException e) {
            // no connection
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(stm!=null){
                try {
                    stm.close();
                } catch (SQLException e) {
                    //
                }
            }
        }
    }

//    public JConfigModule createConfigurator() {
//        return new FileLogConfigModule(this);
//    }


    public void passivate() {
    }
    public void activate() {
    }

    public String getCodeColumn() {
        return codeColumn;
    }

    public void setCodeColumn(String codeColumn) {
        this.codeColumn = codeColumn;
    }

    public String getDateTimeColumn() {
        return dateTimeColumn;
    }

    public void setDateTimeColumn(String dateTimeColumn) {
        this.dateTimeColumn = dateTimeColumn;
    }

    public String getMessageColumn() {
        return messageColumn;
    }

    public void setMessageColumn(String messageColumn) {
        this.messageColumn = messageColumn;
    }

    public String getTypeColumn() {
        return typeColumn;
    }

    public void setTypeColumn(String typeColumn) {
        this.typeColumn = typeColumn;
    }

    public String getUserIdColumn() {
        return userIdColumn;
    }

    public void setUserIdColumn(String userIdColumn) {
        this.userIdColumn = userIdColumn;
    }

    public String getUserNameColumn() {
        return userNameColumn;
    }

    public void setUserNameColumn(String userNameColumn) {
        this.userNameColumn = userNameColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


}
