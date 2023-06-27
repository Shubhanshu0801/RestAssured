package com.googleApis;

import files.Payload;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class AddPlace {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(Payload.addPlace()).when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200);
    }
}
