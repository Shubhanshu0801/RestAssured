package com.jiraApis;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class JiraTest3 {
    public static void main(String[] args) {
        RestAssured.baseURI = "http://localhost:8080";
        //Login Scenario
        SessionFilter session = new SessionFilter();
        String response = given().log().all().headers("Content-Type", "application/json")
                .body("{ \"username\": \"anshu.dubey4\", \"password\": \"Incredible@9\" }")
                .filter(session).when().post("/rest/auth/1/session").then().log().all()
                .extract().response().asString();

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

        JsonPath js = new JsonPath(addIssueResponse);
        String issueKey = js.get("key");
        System.out.println(issueKey);
        given().relaxedHTTPSValidation().log().all().pathParam("key", issueKey).headers("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"This is my first comment.\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}").filter(session).when().post("/rest/api/2/issue/{key}/comment")
                .then().assertThat().statusCode(201);

        String getResponse = given().log().all().pathParam("key", issueKey)
                .queryParam("fields", "comment").filter(session)
                .when().get("/rest/api/2/issue/{key}").then().log().all()
                .extract().response().asString();
        JsonPath js1 = new JsonPath(getResponse);
        String comment = js1.get("fields.comment.comments[0].body");
        System.out.println(comment);
    }
}
