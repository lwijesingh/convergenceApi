package com.wiley.utils.db;

import com.wiley.common.LoggerUtil;
import com.wiley.common.TableUtils;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DBUtil {
    private static JdbcDatabaseTester databaseTester;
    private static IDatabaseConnection dbUnitConnection;
    private Connection conn = null;
    private String host;
    private String dbName;
    private String user;
    private String pass;

    public static final String DB2 = "DB2";
    public static final String MYSQL = "MYSQL";
    public static final String AWS = "AWS";
    public static final String AWS_SO = "AWS_SO";
    public static final String ITEM_MASTER = "ITEM_MASTER";
    public static final String ITEM_COST = "ITEM_COST";
    public static final String POSTGRES_SQL_DRIVER = "org.postgresql.Driver";
    public static final String AS400_DERIVER = "com.ibm.as400.access.AS400JDBCDriver";
    public static final String JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final String DB2_CONNECTION_MESSAGE = "Connected To DB2";
    private static final String DB_CONNECTION_ERROR = "Error occurs while creating database connection  {0} ";


    private DBUtil getConnection(String connectionType) {
        LoggerUtil.log(Level.INFO, connectionType);
        try {
            if (conn == null) {
                switch (connectionType.toUpperCase()) {
                    case DB2:
                        Class.forName(AS400_DERIVER);
                        conn = DriverManager.getConnection(DBConnections.DB2HOST, DBConnections.DB2USERNAME, DBConnections.DB2PD);
                        break;
                    case AWS:
                        Class.forName(POSTGRES_SQL_DRIVER);
                        conn = DriverManager.getConnection(DBConnections.POSTGRES_HOST + DBConnections.POSTGRES_DB_NAME, DBConnections.POSTGRES_USERNAME, DBConnections.getPD());
                        break;
                    case AWS_SO:
                        Class.forName(POSTGRES_SQL_DRIVER);
                        conn = DriverManager.getConnection(DBConnections.POSTGRES_HOST_SO + DBConnections.POSTGRES_DB_NAME, DBConnections.POSTGRES_USERNAME, DBConnections.getPD());
                        break;
                    case ITEM_MASTER:
                        Class.forName(POSTGRES_SQL_DRIVER);
                        conn = DriverManager.getConnection(DBConnections.POSTGRES_ITEM_MASTER_UPDATE_HOST + DBConnections.POSTGRES_DB_NAME, DBConnections.POSTGRES_USERNAME, DBConnections.getPD());
                        break;
                    case ITEM_COST:
                        Class.forName(POSTGRES_SQL_DRIVER);
                        conn = DriverManager.getConnection(DBConnections.POSTGRES_ITEM_COST_HOST + DBConnections.POSTGRES_DB_NAME, DBConnections.POSTGRES_USERNAME, DBConnections.getPD());
                        break;
                    default:
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return this;
    }

    private DBUtil getDbunitConnection(String connectionType) throws Exception {
        LoggerUtil.log(Level.INFO, connectionType);
        try {
            if (dbUnitConnection == null) {
                switch (connectionType.toUpperCase()) {
                    case MYSQL:
                        databaseTester = new JdbcDatabaseTester(DBUtil.MYSQL, DBConnections.PAYROLL_CONNECTION_URL, DBConnections.PAYROLL_MYSQL_JDBC_USERNAME, DBConnections.PAYROLL__MYSQL_JDBC_PASSWORD);
                        dbUnitConnection = databaseTester.getConnection();
                        break;
                    default:
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return this;
    }

    public ITable getDBUnitQueryData(String dbType,String scenario, String sqlQuery) {
        LoggerUtil.log(Level.INFO, sqlQuery);
        ITable iTable = null;
        try {
            getDbunitConnection(dbType);
            iTable = dbUnitConnection.createQueryTable(scenario,sqlQuery);
        } catch (Exception e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        } finally {
            if (dbUnitConnection != null) {
                try {
                    dbUnitConnection.close();
                } catch (SQLException e) {
                    LoggerUtil.log(Level.WARNING, e.getMessage());
                }
            }
        }
        return iTable;
    }

    public <T> T getQueryData(String dbType, String sqlQuery, Class<T> mappingClass) {
        LoggerUtil.log(Level.INFO, sqlQuery);
        List<T> list = new ArrayList<>();
        try (Statement statement = getConnection(dbType).conn.createStatement(); ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            list = TableUtils.getTableData(resultSet, mappingClass);
        } catch (SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LoggerUtil.log(Level.WARNING, e.getMessage());
                }
            }
        }
        return list.get(0);
    }

    public int updateQuery(String dbType, String sqlQuery) {
        LoggerUtil.log(Level.INFO, sqlQuery);
        int resultSet = 0;
        try (Statement statement = getConnection(dbType).conn.createStatement()) {
            resultSet = statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LoggerUtil.log(Level.WARNING, e.getMessage());
                }
            }
        }
        return resultSet;
    }

    public boolean deleteQuery(String dbType, String sqlQuery) {
        LoggerUtil.log(Level.INFO, sqlQuery);
        boolean resultSet = false;
        try (Statement statement = getConnection(dbType).conn.createStatement()) {
            resultSet = statement.execute(sqlQuery);
        } catch (SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LoggerUtil.log(Level.WARNING, e.getMessage());
                }
            }
        }
        return resultSet;
    }

    public ResultSet execQuery(String query) {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            if (connectDB2()) {
                LoggerUtil.log(DB2_CONNECTION_MESSAGE);
                statement = conn.createStatement();
                resultSet = statement.executeQuery(query);
            }
        } catch (ClassNotFoundException | SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return resultSet;
    }

    public boolean connectDB2() throws SQLException, ClassNotFoundException {
        if (host.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            throw new SQLException("DB2 Database credentials missing");
        }
        Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        this.conn = DriverManager.getConnection(
                this.host,
                this.user, this.pass);
        return true;

    }
}
