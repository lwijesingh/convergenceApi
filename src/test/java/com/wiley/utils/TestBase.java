package com.wiley.utils;

import com.wiley.common.Constant;
import com.wiley.common.LoggerUtil;
import com.wiley.common.RestUtil;
import com.wiley.utils.db.ConnectionBase;
import com.wiley.utils.db.DBConnections;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

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

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        LoggerUtil.log("==================================================================================================");
        LoggerUtil.log("/n");
        LoggerUtil.log("============ Test Class : " + this.getClass().getName().split("tests.")[1] + " ============");
        LoggerUtil.log("/n");
        LoggerUtil.log("==================================================================================================");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        LoggerUtil.log("/n");
        LoggerUtil.log("/n");
        LoggerUtil.log("/n");
        LoggerUtil.log("-------------------------------------------------------------");
        LoggerUtil.log("/n");
        LoggerUtil.log("----------- Test Method :" + method.getName() + " -----------");
        LoggerUtil.log("/n");
        LoggerUtil.log("-------------------------------------------------------------");
        //syscoLabUI = login.loadsLoginPage();

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                LoggerUtil.log("====== TEST PASSED =====");
                break;
            case ITestResult.FAILURE:
                LoggerUtil.log("====== TEST FAILED =====");
                break;
            case ITestResult.SKIP:
                LoggerUtil.log("====== TEST SKIPPED =====");
                break;
            default:
                throw new RuntimeException("====== Invalid status ======");
        }
    }
    @AfterMethod(alwaysRun = true)
    public void closeConnections(ITestResult result) {

    }


}
