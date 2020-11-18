package com.wiley.utils;

import com.wiley.common.Constant;
import com.wiley.common.RestUtil;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    @BeforeMethod(alwaysRun = true)
    public static void init() {
        switch (Constant.TEST_ENV) {
            case "dev":
                RestUtil.API_HOST = Constant.DEV_BASE_URI;
                break;
            case "qa":
                RestUtil.API_HOST = Constant.QA_BASE_URI;
                break;
            default:
                RestUtil.API_HOST = Constant.STG_BASE_URI;
        }
        RestUtil.BASE_PATH = Constant.BASE_PATH;
    }

}
