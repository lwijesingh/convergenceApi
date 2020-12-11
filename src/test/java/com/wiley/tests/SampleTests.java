package com.wiley.tests;


import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.wiley.common.CompareData;
import com.wiley.common.DbUnitJSONParser;
import com.wiley.common.LoggerUtil;
import com.wiley.model.api.request.CourseDetails;
import com.wiley.model.api.request.StudentDetails;
import com.wiley.model.api.response.StudentResults;
import com.wiley.model.db.response.ExamResults;
import com.wiley.utils.TestBase;
import com.wiley.utils.api.RequestUtil;
import com.wiley.utils.api.ResponseUtil;
import com.wiley.utils.db.DBUtil;
import com.wiley.utils.json.JsonUtil;
import org.dbunit.dataset.ITable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;

public class SampleTests extends TestBase {
    DBUtil dbUtil;

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
        ExamResults examResults = dbUtil.getQueryData(dbUtil.DB2, sqlQuery, ExamResults.class);

       LoggerUtil.log(examResults.getQHACTI());
    }

    @Test
    public void testDBRequestAndResponseByXpath() {
        //Reading a lengthy String Json response

        HashMap<String, Object> map[] = JsonUtil.getJSONArrayMapped("AllCourseDetails");

        String CouchebaseQueary = JsonUtil.getKeyValue(JsonUtil.getJsonFromMap(map[1]), "$..bicycle.price");

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

            expectedTable = dbUtil.getDBUnitQueryData(DBUtil.MYSQL,scenario,sqlQuery);

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
}
