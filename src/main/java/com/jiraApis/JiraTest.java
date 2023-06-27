package com.jiraApis;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

import static io.restassured.RestAssured.*;

public class JiraTest {
    /*Steps to automate in Jira application:
    1. Login to Jira to create session using Login API.
    2. Add an Issue.
    */
    public static void main(String[] args) {
        RestAssured.baseURI = "http://localhost:8080";
        //1. Login Scenario
        SessionFilter session = new SessionFilter();
        String response = given().log().all().headers("Content-Type", "application/json")
                .body("{ \"username\": \"anshu.dubey4\", \"password\": \"Incredible@9\" }")
                .filter(session).when().post("/rest/auth/1/session").then().log().all()
                .extract().response().asString();

        //2. Add an Issue.
        given().log().all().headers("Content-Type", "application/json")
                .filter(session).body("{\n" +
                        "            \"fields\": {\n" +
                        "            \"project\": {\n" +
                        "                \"key\": \"RES\"\n" +
                        "            },\n" +
                        "            \"summary\": \"Debit card defect\",\n" +
                        "                    \"description\": \"Creating my first bug\",\n" +
                        "                    \"issuetype\": {\n" +
                        "                \"name\": \"Bug\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "        }\n")
                .when().post(" /rest/api/2/issue").then().assertThat().statusCode(201);
    }
}
