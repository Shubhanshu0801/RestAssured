package com.pojoDeserialization;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Deserialization {
    public static void main(String[] args) {
        //To get code
        String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2F0AbUR2VMtVfM6PoPKu" +
                "xXYkrXFxuwVCkO2nrDhXjRYTR3VeUdvKUpa2s0UtA1Vb4RE7t4eOw&scope=email+https%3A%2F%2Fwww.googleapis." +
                "com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];

        //To get the access token
        /*urlEncodingEnabled() is used to explicitly tell to RestAssured not to perform any encoding for
        special characters.*/
        String accessTokenResponse = given().urlEncodingEnabled(false).queryParam("code", code)
                .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.a" +
                        "pps.googleusercontent.com")
                .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath js = new JsonPath(accessTokenResponse);
        String accessToken = js.getString("access_token");

        GetCourse response = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
        System.out.println(response.getInstructor());
        System.out.println(response.getLinkedIn());
        //To get the price of SoapUI testing
        List<Api> apiCourses = response.getCourses().getApi();
        for(int i=0; i<apiCourses.size(); i++) {
            if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
                apiCourses.get(i).getPrice();
            }
        }
        //To the course names of WebAutomation
        List<WebAutomation> webAutomationList = response.getCourses().getWebAutomation();
        for(int i=0; i<webAutomationList.size(); i++) {
            System.out.println(webAutomationList.get(i).getCourseTitle());
        }
    }
}
