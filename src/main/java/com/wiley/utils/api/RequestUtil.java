package com.wiley.utils.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyData;
import com.jayway.restassured.specification.RequestSpecification;
import com.wiley.common.Constant;
import com.wiley.common.LoggerUtil;
import com.wiley.common.RequestHeader;
import com.wiley.common.RestUtil;
import com.wiley.utils.json.JsonUtil;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class RequestUtil {

    public static final String CONSUMER_1 = "STUDENT_DETAILS";
    public static final String CONSUMER_2 = "COURSE_DETAILS";
    public static final String CONSUMER_3 = "AVG_MARKS";

    // Lahiru : Plan uri location / and Consumer_1 types location

    public static <T> Response sendRequest(String uri, String clientType, T object) {
        return RestUtil.send(RequestHeader.getBearerHeaders(getToken(clientType)), JsonUtil.objectToJson(object), uri, "POST");
    }

    public static <T> Response sendRequest(String uri) {
        return RestUtil.send(RequestHeader.getHeader(Constant.QA_COOKIE), "", uri, "GET");
    }

    public static <T> Response sendRequestAsString(String uri, String clientType,String json) {
        return RestUtil.send(RequestHeader.getBearerHeaders(getToken(clientType)), json, uri, "POST");
    }

    private static String getToken(String clientType) {
        String token = "";
        switch (clientType) {
            case CONSUMER_1:
                token = getAccessToken(Constant.CONSUMER_KEY_CONSUMER1, Constant.CONSUMER_SECRET_CONSUMER1);
                break;
            case CONSUMER_2:
                token = getAccessToken(Constant.CONSUMER_KEY_CONSUMER2, Constant.CONSUMER_SECRET_CONSUMER2);
                break;
            case CONSUMER_3:
                token = getAccessToken(Constant.CONSUMER_KEY_CONSUMER3, Constant.CONSUMER_SECRET_CONSUMER3);
                break;
            default:
                break;
        }

        LoggerUtil.log(Level.INFO, token);
        return token;

    }

    public static String getAccessToken(String clientID, String clientSecret) {
        String body = "grant_type=client_credentials";
        Response response = RestUtil.send(RequestHeader.getHeader(clientID, clientSecret), body, "token", "POST");

        return ResponseUtil.getResponseValue(response, "access_token");
    }

}
