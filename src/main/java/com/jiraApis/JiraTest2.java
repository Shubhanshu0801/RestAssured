package com.jiraApis;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraTest2 {
    public static void main(String[] args) {
        RestAssured.baseURI = "http://localhost:8080";
        //1. Login Scenario
        SessionFilter session = new SessionFilter();
        String response = given().log().all().headers("Content-Type", "application/json")
                .body("{ \"username\": \"anshu.dubey4\", \"password\": \"Incredible@9\" }")
                .filter(session).when().post("/rest/auth/1/session").then().log().all()
                .extract().response().asString();

        //2. Adding an Issue
        String addIssueResponse = given().log().all().headers("Content-Type", "application/json")
                .filter(session).body("{\n" +
                        "            \"fields\": {\n" +
                        "            \"project\": {\n" +
                        "                \"key\": \"RES\"\n" +
                        "            },\n" +
                        "            \"summary\": \"Visa card defect\",\n" +
                        "                    \"description\": \"Creating my third bug\",\n" +
                        "                    \"issuetype\": {\n" +
                        "                \"name\": \"Bug\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "        }\n")
                .when().post(" /rest/api/2/issue").then().assertThat().statusCode(201).extract().response().asString();

        //3. Adding a comment.
        JsonPath js = new JsonPath(addIssueResponse);
        String issueKey = js.get("key");
        System.out.println(issueKey);
        given().log().all().pathParam("key", issueKey).headers("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"This is my first comment.\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}").filter(session).when().post("/rest/api/2/issue/{key}/comment")
                .then().assertThat().statusCode(201);

        //4. Adding an attachment/file.
        given().log().all().headers("X-Atlassian-Token", "no-check").pathParam("key", issueKey)
                .header("Content-Type", "multipart/form-data").filter(session)
                .multiPart("file", new File("C:\\Users\\ishan\\OneDrive\\Desktop\\" +
                        "Shubhanshu\\eclipse-workspace\\RestAssured\\jira.txt")).when()
                .post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
    }
}
