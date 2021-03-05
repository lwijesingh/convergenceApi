package com.wiley.utils.db;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class ConnectionBase {

    //protected static final Logger LOGGER = Logger.getLogger("devguide");

    protected final Cluster cluster;
    protected final Bucket bucket;

    //=== EDIT THESE TO ADAPT TO YOUR COUCHBASE INSTALLATION ===
    public static final String bucketName = "was";
    public static final String bucketPassword = "";
    public static final List<String> nodes = Arrays.asList("10.221.139.168");
    public static CouchbaseEnvironment environment = DefaultCouchbaseEnvironment.create();

    /*public ConnectionBase() {
        //connect to the cluster by hitting one of the given nodes
        cluster = CouchbaseCluster.create(environment, nodes);
        //get a Bucket reference from the cluster to the configured bucket
        cluster.authenticate("admin","admin1");
        bucket = cluster.openBucket(bucketName, bucketPassword);
    }*/

    public ConnectionBase() throws Exception{
        //connect to the cluster by hitting one of the given nodes
        cluster = CouchbaseCluster.create(nodes);
        //get a Bucket reference from the cluster to the configured bucket
        cluster.authenticate("admin","admin1");
        Thread.sleep(20000);
        bucket = cluster.openBucket(bucketName);
        Thread.sleep(20000);
    }

   /* private void disconnect() {
        //release shared resources and close all open buckets
        cluster.disconnect();
    }

    public void execute() throws Exception{
        //connection has been done in the constructor
        doWork();
        disconnect();
    }
*/
    /**
     * Override this method to showcase specific examples.
     * Make them executable by adding a main method calling new ExampleClass().execute();
     */
    protected void doWork() throws Exception {
        //this one just showcases connection methods, see constructor and shutdown()
      //  LOGGER.info("Connected to the cluster, opened bucket " + bucketName);
    }

    /*public static void main(String[] args) throws Exception {
        new ConnectionBase().execute();
    }*/
}