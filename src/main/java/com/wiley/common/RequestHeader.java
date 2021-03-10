package com.wiley.common;

import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;

public class RequestHeader {

    public RequestHeader() {
}

    public static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }

    public static HashMap<String, String> getBasicHeader(String token) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Authorization", "Basic " + token);
        return headers;
    }

    public static HashMap<String, String> getBasicHeader(String clientID, String clientSecret) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Authorization", "Basic " + getBase64(clientID + ":" + clientSecret));
        return headers;
    }

    public static HashMap<String, String> getHeader(String clientId, String clientSecret) {
        HashMap<String, String> headers = new HashMap();
        String base64 = getBase64(clientId + ":" + clientSecret);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "Basic " + base64);
        headers.put("OTHER HEADERS", "Accept:application/json");
        return headers;
    }

    public static HashMap<String, String> getHeaderQMetry(String token, String jsessionID, String referer) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + token);
        headers.put("cookie", jsessionID);
        headers.put("Referer", referer);
        return headers;
    }

    public static HashMap<String, String> getHeader(String cookies) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("cookie", cookies);
        return headers;
    }


    public static HashMap<String, String> getHeaderQMetry(String clientID, String clientSecret, String jsessionID, String referer) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + getBase64(clientID + ":" + clientSecret));
        headers.put("cookie", jsessionID);
        headers.put("Referer", referer);
        return headers;
    }

    public static HashMap<String, String> getHeaderUrlEncoded() {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    public static HashMap<String, String> getBasicHeaders(String encoding) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "Basic " + encoding);
        return headers;
    }

    public static HashMap<String, String> getBearerHeaders(String accessToken) {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

    public static HashMap<String, String> getXmlHeaders() {
        HashMap<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/xml");
        return headers;
    }

    public static String getBase64(String originalString) {
        return Base64.encodeBase64String(originalString.getBytes());
    }
}
