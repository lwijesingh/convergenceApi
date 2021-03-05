package com.wiley.utils.db;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.wiley.common.LoggerUtil;
import com.wiley.common.TableUtils;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.experimental.categories.ExcludeCategories;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DBConnections {

    private String host;
    private String dbName;
    private String user;
    private String pass;
    private Connection conn;
    private static JdbcDatabaseTester databaseTester;
    private static IDatabaseConnection dbUnitConnection;
    public static final String POSTGRES_QA = "POSTGRES_QA";
    public static final String COUCHBASE = "COUCHBASE";
    private static final String DB2_CONNECTION_MESSAGE = "Connected To DB2";
    public static final String MYSQL = "MYSQL";
    public static final String DB2 = "DB2";
    public static final String POSTGRES_SQL_DRIVER = "org.postgresql.Driver";

    //###Couche base

    protected Cluster cluster;
    protected Bucket bucket;

    //=== EDIT THESE TO ADAPT TO YOUR COUCHBASE INSTALLATION ===

    public static CouchbaseEnvironment environment = DefaultCouchbaseEnvironment.create();

    //####Couch

    public DBConnections() {

    }

    public DBConnections(String host, String user, String pass, Cluster cluster) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public DBConnections(String host, String dbName, String user, String pass) {
        this.host = host;
        this.user = user;
        this.dbName = dbName;
        this.pass = pass;
    }

    private DBConnections getCouchBaseConnection(String connectionType) {
        LoggerUtil.log(Level.INFO, connectionType);
        try {
            if (bucket == null) {
                switch (connectionType.toUpperCase()) {
                    case COUCHBASE:
                       // Class.forName(POSTGRES_SQL_DRIVER);
                        cluster = CouchbaseCluster.create(DBUtil.NODES);
                        //get a Bucket reference from the cluster to the configured bucket
                        cluster.authenticate(DBUtil.COUCH_USERNAME,DBUtil.getCouchPD());
                        Thread.sleep(20000);
                        bucket = cluster.openBucket(DBUtil.BUCKET_NAME);
                        Thread.sleep(20000); // conn = DriverManager.getConnection(DBUtil.RESULT_CONTEXT_QA + DBUtil.RESULT_CONTEXT_QA_DBNAME, DBUtil.RESULT_CONTEXT_QA_USERNAME, DBUtil.getPD());
                        break;
                }
            }
        } catch ( InterruptedException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return this;
    }
    private void disconnect() {
        //release shared resources and close all open buckets
        cluster.disconnect();
    }
    private DBConnections getConnection(String connectionType) {
        LoggerUtil.log(Level.INFO, connectionType);
        try {
            if (conn == null) {
                switch (connectionType.toUpperCase()) {
                    case POSTGRES_QA:
                        Class.forName(POSTGRES_SQL_DRIVER);
                        conn = DriverManager.getConnection(DBUtil.RESULT_CONTEXT_QA + DBUtil.RESULT_CONTEXT_QA_DBNAME, DBUtil.RESULT_CONTEXT_QA_USERNAME, DBUtil.getPD());
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return this;
    }

    private DBConnections getDbunitConnection(String connectionType) throws Exception {
        LoggerUtil.log(Level.INFO, connectionType);
        try {
            if (dbUnitConnection == null) {
                switch (connectionType.toUpperCase()) {
                    case MYSQL:
                        // databaseTester = new JdbcDatabaseTester(DBUtil.MYSQL, DBConnections.PAYROLL_CONNECTION_URL, DBConnections.PAYROLL_MYSQL_JDBC_USERNAME, DBConnections.PAYROLL__MYSQL_JDBC_PASSWORD);
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

    public ITable getDBUnitQueryData(String dbType, String scenario, String sqlQuery) {
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
        List<T> list = new ArrayList<>();
        try (Statement statement = getConnection(dbType).conn.createStatement(); ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            list = TableUtils.getTableData(resultSet, mappingClass);
        }  catch (SQLException e) {
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

    public Bucket openBucket(String connectionType) {
        try  {
            bucket = getCouchBaseConnection(connectionType).bucket;
        } catch(Exception e) {
            if (bucket != null) {
                bucket.close();
            }
        }
        return bucket;
    }

    public void closeCluster() {
      disconnect();
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
}
