package com.wiley.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectProperties {

    private static String defaultEnvFilePath = System.getProperty("user.dir") + "/src/test/resources/qa_config.properties";

    private static Properties envProperties = null;

    private ProjectProperties() {
    }

    private static void loadProperties() {
        envProperties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(defaultEnvFilePath);
            envProperties.load(input);
        } catch (IOException e) {
            LoggerUtil.log(e);
        }
    }

    public static String getProperty(String key) {
        if (envProperties == null)
            loadProperties();

        String p = System.getProperty(key);
        return p != null ? p : envProperties.getProperty(key);
    }
}
