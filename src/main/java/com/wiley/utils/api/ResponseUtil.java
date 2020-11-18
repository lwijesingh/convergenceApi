package com.wiley.utils.api;

import com.jayway.restassured.response.Response;
import com.wiley.common.LoggerUtil;
import com.wiley.common.RestUtil;
import com.wiley.utils.json.JsonUtil;

import java.util.logging.Level;

public class ResponseUtil {

    public static int getResponseCode(Response response) {
        return RestUtil.getResponseCode(response);
    }

    public static String getResponseBodyAsString(Response response) {
        return response.getBody().asString();
    }

    public static <T> T getResponseAsMapped(Response response, Class<T> classToMap) {
        String responseBody = response.getBody().asString();
        return JsonUtil.getObjectFromJsonString(responseBody, classToMap);
    }

    public static <T> T[] getResponseAsMappedArray(Response response, Class<T[]> classToMap) {
        String responseBody = response.getBody().asString();
        return JsonUtil.getObjectArrayFromJsonString(responseBody, classToMap);
    }

    public static String getResponseValue(Response response, String key) {
        return RestUtil.getValue(response, key);
    }
}
