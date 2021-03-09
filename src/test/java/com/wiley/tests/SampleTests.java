package com.wiley.tests;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.wiley.common.CompareData;
import com.wiley.common.DbUnitJSONParser;
import com.wiley.common.LoggerUtil;
import com.wiley.data.postgreSql.ResultContext;
import com.wiley.model.api.request.CourseDetails;
import com.wiley.model.api.request.StudentDetails;
import com.wiley.model.api.response.StudentResults;
import com.wiley.model.db.response.ExamResults;
import com.wiley.utils.TestBase;
import com.wiley.utils.api.RequestUtil;
import com.wiley.utils.api.ResponseUtil;
import com.wiley.utils.db.DBConnections;
import com.wiley.utils.db.DBUtil;
import com.wiley.utils.json.JsonUtil;
import org.dbunit.dataset.ITable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;

public class SampleTests extends TestBase {
    DBUtil dbUtil;
    static DBConnections dbConnections;

    @BeforeClass
    public static void initiate(ITestContext iTestContext) {
        iTestContext.setAttribute("feature", "Contract Tests - POAcknowledgementFunctionalTest");
        dbConnections = new DBConnections();
    }

    @Test
    public void testPostgreSql() throws Exception {
        String sqlQuery = "select id,result_context_id,item_id,status,calculated_score,points_earned,points_possible from item_result where result_context_id='b7159f987837a1b280ede728a853e819e229e90537d2d52b14608c9d1c6c32fb'";
        ResultContext resultContext=dbConnections.getQueryData("POSTGRES_QA",sqlQuery, ResultContext.class);
        System.out.println("#######"+resultContext.getId());
        System.out.println("#######"+resultContext.getItem_id());
        System.out.println("#######"+resultContext.getStatus());
        System.out.println("#######"+resultContext.getCalculated_score());
        System.out.println("#######"+resultContext.getPoints_earned());
        System.out.println("#######"+resultContext.getPoints_possible());
    }

    @Test
    public void testCouchbaseDocumentUpdate() throws Exception {
        //String key = "question::39e41de3-0e55-4573-885a-f371284574b6";
        //assessment::b7159f987837a1b280ede728a853e819e229e90537d2d52b14608c9d1c6c32fb
        String key = "assessment::25ce5e95d55b4847530982fa8de181f6fec07f74068b650358ec8bbe457c273a";
        JsonDocument document;

        //Read Test data Json and convert it to a String
        List<HashMap<String, Object>> map = JsonUtil.getJSONListMapped("AssessmentQA");
        String jsonFile = JsonUtil.objectToJson(map.get(0));

        //Converting JsonFile to a JsonObject
        JsonTranscoder trans = new JsonTranscoder();
        final JsonObject jsonObj = trans.stringToJsonObject((jsonFile));
        Thread.sleep(20000);
        //see that inserting it fails because it already exists
        Bucket bucket = dbConnections.openBucket("COUCHBASE");
        try {
            bucket.insert(JsonDocument.create(key, jsonObj));
        } catch (DocumentAlreadyExistsException e) {
            LoggerUtil.log("Couldn't insert it, DocumentAlreadyExists... Let's try to replace it");
        }
        Thread.sleep(20000);
        //on the other hand, updating works (it would have failed if the key was not in database)
        document = bucket.replace(JsonDocument.create(key, jsonObj));
        LoggerUtil.log("Replaced the old document by the new one: " + document);
        //notice the document's CAS changed again...

        LoggerUtil.log("Got the following from database: " + bucket.get(key));

    }

    @Test
    public void testRequestAndResponse() {
        Response response001;
        //Assigning Json request file to an object
        //Class name and Json file name must be the same name
        StudentDetails studentDetails[] = JsonUtil.getJSONArray(StudentDetails[].class);
        CourseDetails courseDetails[] = JsonUtil.getJSONArray(CourseDetails[].class);

        //If you want you can change the Json values as below
        studentDetails[0].firstName = "Tharanga";

        studentDetails[0].coursesAttended.add(courseDetails[0]);
        response001 = RequestUtil.sendRequest("", RequestUtil.CONSUMER_2, studentDetails[0]);

        // 1.) Mapping Response to an Object
        StudentResults studentResults = ResponseUtil.getResponseAsMapped(response001, StudentResults.class);

        // 2.) Directly getting values as xpth
        JsonPath jsonPathEvaluator02 = response001.jsonPath();
        int halmlessTotal = jsonPathEvaluator02.getInt("data.attributes.last_analysis_stats.harmless");

    }

    @Test
    public void testDBRequestAndResponse() {
        String sqlQuery = "Select * from Student";
        String sqlQuery3 = "Select * from Exam";
        ExamResults examResults = dbConnections.getQueryData(dbConnections.DB2, sqlQuery, ExamResults.class);

       LoggerUtil.log(examResults.getQHACTI());
    }

    @Test
    public void testDBRequestAndResponseByXpath(){
        //Reading a lengthy String Json response

        HashMap<String, Object> map[] = JsonUtil.getJSONArrayMapped("AssessmentQA");

        String points = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.points");
        LoggerUtil.log("Total Points : "+points);

        String pointsEarned = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.pointsEarned");
        LoggerUtil.log("Total PointsEarned : "+pointsEarned);

        String score = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.score");
        LoggerUtil.log("Total Score : "+score);

        String status = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.status");
        LoggerUtil.log("Total Status : "+status);

        String questionId = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.items[0].questionId");
        LoggerUtil.log("Total questionId : "+questionId);

        String noOfQuestions = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[0]), "$.items[*].numAttempts");
        LoggerUtil.log("Total noOfQuestions as an array : "+noOfQuestions);

        int noOfQuestions2 = JsonUtil.getElementCount2((JsonUtil.getJsonFromMap(map[0])), "items");
        LoggerUtil.log("Total noOfQuestions2 : "+noOfQuestions2);

        ArrayList<String> noOfQuestions3 = JsonUtil.getKeyValue(JsonUtil.getElementCount3((JsonUtil.getJsonFromMap(map[0])), "items"),"$.score.grade");
        LoggerUtil.log("List within a nested array : "+noOfQuestions3);

    }

    @Test
    public void testDBRequestAndResponseByObject() {
        boolean flag = true;
        String msg = "";
        ITable expectedTable = null;
        ITable migratedTable = null;

       // From json : sortColumns, sql1, sql2, Scenario
        String sortColumns="Get data from Json testcase";
        String esQuery="Get data from Json testcase";
        String esUrlPath="Get data from Json testcase";
        String sqlQuery="Get data from Json testcase";
        String scenario="Get the Table name from Json";

        JSONObject data = new JSONObject();

        try {
            String response = RequestUtil.sendRequestAsString(esUrlPath,RequestUtil.CONSUMER_2,esQuery).toString();
            JSONArray jso = JsonUtil.getResponseData(response);

            if (jso.length()!=0){
                data.put(scenario, jso);
                migratedTable = new DbUnitJSONParser(new ByteArrayInputStream(data.toString().getBytes(StandardCharsets.UTF_8))).getTable(scenario);
            }

            expectedTable = dbConnections.getDBUnitQueryData(dbConnections.MYSQL,scenario,sqlQuery);

            if (expectedTable == null || expectedTable.getRowCount() == 0) {
                if (migratedTable == null) {
                    msg = "Both ES & SQL queries do not return data";
                    flag = false;
                } else {
                    msg = "SQL does not return data but ES has data and ES returns " + migratedTable.getRowCount() + " rows \n";
                    flag = false;
                }
            } else {
                if (migratedTable == null && expectedTable.getRowCount() != 0) {
                    msg = "ES does not return data but SQL has data and SQL returns " + expectedTable.getRowCount() + " rows \n";
                    flag = false;

                }
            }

            if (flag == true) {

                String[] columnsToSort = sortColumns.split(",");
                String result = CompareData.assertEquals(expectedTable, migratedTable, columnsToSort);

                if (result.isEmpty()) {
                    msg = "Data matched  for test case \n";
                } else {
                    msg = "Data mismatches exist for test case \n";
                    msg = msg + result;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterMethod(alwaysRun = true)
    public void closeConnections(ITestResult result) {
        dbConnections.closeCluster();
    }
}
