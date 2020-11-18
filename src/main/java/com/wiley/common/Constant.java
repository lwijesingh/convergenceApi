package com.wiley.common;

public class Constant {
    private static final String  CLIENT_KEY="client.key";
    private static final String CLIENT_SECRET="client.secret";

   public static final String TEST_ENV = ProjectProperties.getProperty("test.env");

    public static final String DEV_BASE_URI = "http://dev.aws.na.abc.net";
    public static final String QA_BASE_URI = "http://qa.aws.na.abc.net";
    public static final String STG_BASE_URI = "http://stg.aws.na.abc.net";
    public static final String BASE_PATH = "";

    public static final String CONSUMER_KEY_CONSUMER1 = System.getProperty(CLIENT_KEY, "wpoH5xyYKsY1fedC2iZDWFqtUHoa");
    public static final String CONSUMER_SECRET_CONSUMER1 = System.getProperty(CLIENT_SECRET, "1Lq3LCU6_JBNMNoJgo3hiAkfLgwa");


    public static final String CONSUMER_KEY_CONSUMER2 = System.getProperty(CLIENT_KEY, "6KmMB2DGd45FNDlzGP8mwxdp758a");
    public static final String CONSUMER_SECRET_CONSUMER2 = System.getProperty(CLIENT_SECRET, "pLQ4vUcL2ZD1TAPxgiKELmInWrQa");

    public static final String CONSUMER_KEY_CONSUMER3 = System.getProperty(CLIENT_KEY, "pNCUcxYRibeVwBxgy4v7ArsBWA8a");
    public static final String CONSUMER_SECRET_CONSUMER3 = System.getProperty(CLIENT_SECRET, "wUGwOVU8VaWEkYuJKBZdOUIDRwca");
}
