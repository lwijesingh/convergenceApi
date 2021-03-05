package com.wiley.utils.db;

import com.wiley.common.LoggerUtil;
import com.wiley.common.TableUtils;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DBUtil {

    /**
     * Postgresql QA env configurations
     */
    public static final String RESULT_CONTEXT_QA="jdbc:postgresql://resultcontext.aws.wiley.com:5432/";
    public static final String RESULT_CONTEXT_QA_DBNAME="result_context_qa?currentSchema=result_context";
    public static final String RESULT_CONTEXT_QA_USERNAME="result_context";
    public static final String RESULT_CONTEXT_QA_PASSWORD="Result_247_Context";

    public static String getPD(){
        return RESULT_CONTEXT_QA_PASSWORD;
    }

    /**
     * Couchbase QA env configurations
     */
    public static final String COUCH_USERNAME = "admin";
    public static final String COUCH_PASSWORD = "admin1";
    public static final String BUCKET_NAME = "was";
    public static final String BUCKET_PASSWORD = "";
    public static final List<String> NODES = Arrays.asList("10.221.139.168");
    public static String getCouchPD(){
        return COUCH_PASSWORD;
    }

}
