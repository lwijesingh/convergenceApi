package com.wiley.utils.db;

public class DBConnections {
    public static final String POSTGRES_HOST = "jdbc:postgresql://ev-pg.crkqmj3qk07x.us-east-1.rds.amazonaws.com:5432/";
    public static final String POSTGRES_DB_NAME = "postgres";
    public static final String POSTGRES_USERNAME = "root";
    public static final String POSTGRES_PD = "1BnkvOl6rwlQ6Q";


    public static final String POSTGRES_HOST_SO = "jdbc:postgresql://dev-pg.crkqmj3qk07x.us-east-1.rds.amazonaws.com:5432/";
    public static final String POSTGRES_DB_NAME_SO = "postgres";
    public static final String POSTGRES_USERNAME_SO = "root";
    public static final String POSTGRES_PD_SO = "1BnkvOl6rwlQ6Q";


    public static final String DB2HOST = "jdbc:as400://AS142T.abc.com";
    public static final String DB2USERNAME = "asd";
    public static final String DB2PD = "bcd";


    public static final String POSTGRES_ITEM_MASTER_UPDATE_HOST = "jdbc:postgresql://pdev-pg.crkqmj3qk07x.us-east-1.rds.amazonaws.com:5432/";
    public static final String POSTGRES_ITEM_COST_HOST = "jdbc:postgresql://dev-pg.crkqmj3qk07x.us-east-1.rds.amazonaws.com:5432/";

    public static final String PAYROLL_MYSQL_JDBC_USERNAME = "username";
    public static final String PAYROLL__MYSQL_JDBC_PASSWORD = "password";
    public static final String PAYROLL_CONNECTION_URL = "jdbc:mysql://prod-masterdb2-replica-20190312.ccebt7myzgqy.us-west-1.rds.amazonaws.com/PAYROLL?useSSL=false";


    public static String getPD() {
        return POSTGRES_PD;
    }
}
