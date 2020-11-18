package com.wiley.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {

    private LoggerUtil() { throw new IllegalAccessError();
    }

    public static final String LOGGER_ERROR_MESSAGE = "ERROR! ";
    public static final String LOGGER_EXCEPTION_MESSAGE = ". EXCEPTION: ";

    private static Logger logger = Logger.getAnonymousLogger();
    public static int logLevel = 3;

    public static void log(Exception e) {
        LoggerUtil.log(LOGGER_ERROR_MESSAGE + e.getMessage() + LOGGER_EXCEPTION_MESSAGE + e);
    }

    public static void log(Level level,String e) {
        LoggerUtil.log(" Level : "+level +" : "+ e + LOGGER_EXCEPTION_MESSAGE + e);
    }

    public static void log(Throwable t) {
        LoggerUtil.log(LOGGER_ERROR_MESSAGE + t);
    }

    //Enable Logger from property file
    public static void log(String msg) {
        //if (Constants.IS_LOGGER_ENABLED) {
            Logger logger = Logger.getLogger("logger");
            logger.info(msg);

    }
}
