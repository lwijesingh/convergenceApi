package com.wiley.utils.json;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.wiley.common.LoggerUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private static final Gson GSON = new Gson();
    private static List<String> result = null;

    static {
        try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir") + "/src/test/resources/data"))) {

            result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

        } catch (IOException e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }

    }

    public static <T> T[] getJSONArray(Class<T[]> tClass) {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonParser.parse(new FileReader(loadFile(tClass)));
        } catch (FileNotFoundException e) {
            LoggerUtil.log(e);
        }
        return GSON.fromJson(element, tClass);
    }

    public static HashMap<String, Object>[] getJSONArrayMapped(String fileName) {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonParser.parse(new FileReader(loadFile(fileName)));
        } catch (FileNotFoundException e) {
            LoggerUtil.log(e);
        }

        return GSON.fromJson(GSON.toJson(GSON.fromJson(element, JsonElement.class)), HashMap[].class);

    }

    public static List<HashMap<String, Object>> getJSONListMapped(String fileName) throws IOException {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonParser.parse(new FileReader(loadFile(fileName)));
        } catch (FileNotFoundException e) {
            LoggerUtil.log(e);
        }
        ObjectMapper objectMapper=new ObjectMapper();
        List<HashMap<String, Object>> myObjects = objectMapper.readValue(element.toString() , new TypeReference<List<HashMap<String, Object>>>(){});
        return myObjects;

    }


    public static HashMap<String, Object>[] getJSONArrayMapped1(String fileName) throws IOException {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonParser.parse(new FileReader(loadFile(fileName)));
        } catch (FileNotFoundException e) {
            LoggerUtil.log(e);
        }
        System.out.println("*****"+element);
        System.out.println("*####****"+element.toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        //TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<>() {};
        HashMap<String, Object> o[] = mapper.readValue(element.toString(), typeRef);
        return o;
    }


    public static String[] getJSONArrayAsAString(String fileName) {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = null;
        try {
            element = jsonParser.parse(new FileReader(loadFile(fileName)));
        } catch (FileNotFoundException e) {
            LoggerUtil.log(e);
        }
        return GSON.fromJson(GSON.toJson(GSON.fromJson(element, JsonElement.class)), String[].class );

    }

    public static JSONObject getJsonFromMap(Map<String, Object> map) throws JSONException {
        JSONObject jsonData = new JSONObject();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Map<?, ?>) {
                value = getJsonFromMap((Map<String, Object>) value);
            }
            jsonData.put(key, value);
        }
        return jsonData;
    }

    public static String getKeyValue(JSONObject json, String path) {
        // If needed to implement as a List, user below.
       /* List<String> authors = JsonPath.read(json.toString(), path);
        LoggerUtil.info(authors.get(0));*/
        LoggerUtil.log("Json Object : " + json.toString());

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json.toString());
        return JsonPath.read(document, path).toString();
    }

    private static <T> String loadFile(String tClass) {
        String jsonFileName = tClass + ".json";
        String fileLoadPath = "";
        Optional<String> optional = result.stream().filter(x -> x.toLowerCase().endsWith(jsonFileName.toLowerCase())).findFirst();
        if (optional.isPresent()) {
            fileLoadPath = optional.get();
        }
        LoggerUtil.log("JSON File Path {0} " + fileLoadPath);
        return fileLoadPath;
    }

    private static <T> String loadFile(Class<T> tClass) {
        String[] classNameArr = tClass.getName().split("\\.");
        String className = classNameArr[classNameArr.length - 1].replace(";", "").trim();
        String jsonFileName = className + ".json";
        String fileLoadPath = "";
        Optional<String> optional = result.stream().filter(x -> x.toLowerCase().endsWith(jsonFileName.toLowerCase())).findFirst();
        if (optional.isPresent()) {
            fileLoadPath = optional.get();
        }
        LoggerUtil.log("JSON File Path {0} " + fileLoadPath);
        return fileLoadPath;
    }

    public static <T> String objectToJson(T objectToJson) {
          return GSON.toJson(objectToJson);
    }

    public static <T> T getObjectFromJsonString(String jsonString, Class<T> classToMap) {

        return GSON.fromJson(jsonString, classToMap);
    }

    public static <T> T[] getObjectArrayFromJsonString(String jsonString, Class<T[]> classToMap) {

        return GSON.fromJson(jsonString, classToMap);
    }

    public static  JSONArray getResponseData(String response){
        return new JSONObject(response).getJSONArray("records").getJSONObject(0).getJSONArray("data");
    }

}
