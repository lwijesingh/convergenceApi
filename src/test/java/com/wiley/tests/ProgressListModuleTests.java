package com.wiley.tests;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.wiley.common.LoggerUtil;
import com.wiley.data.postgreSql.ResultContext;
import com.wiley.utils.TestBase;
import com.wiley.utils.db.DBConnections;
import com.wiley.utils.json.JsonUtil;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class ProgressListModuleTests extends TestBase {
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
        //... which is consistent with a get (RYOW)
        //Below Test data fails
        // dbConnections.getQueryData("POSTGRES_QA","", RequestUtil.class);
    }

}
