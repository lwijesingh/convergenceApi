package com.wiley.tests;

import com.jayway.restassured.response.Response;
import com.wiley.model.api.request.CourseDetails;
import com.wiley.model.api.request.StudentDetails;
import com.wiley.model.api.response.Results;
import com.wiley.model.db.response.StudentResults;
import com.wiley.utils.TestBase;
import com.wiley.utils.api.RequestUtil;
import com.wiley.utils.api.ResponseUtil;
import com.wiley.utils.db.DBConnections;
import com.wiley.utils.db.DBUtil;
import com.wiley.utils.json.JsonUtil;
import org.testng.annotations.Test;

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

        //Mapping Response to an Object
        Results results = ResponseUtil.getResponseAsMapped(response001, Results.class);
    }

    @Test
    public void testDBRequestAndResponse() {
        String sqlQuery = "Select * from Student";
        StudentResults studentResults = dbUtil.getQueryData(dbUtil.DB2,sqlQuery,StudentResults.class);
        System.out.println(studentResults.getQHACTI());
    }
}
