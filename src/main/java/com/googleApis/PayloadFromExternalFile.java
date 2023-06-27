package com.googleApis;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;

public class PayloadFromExternalFile {
    @Test
    public void addPlace() throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Ishani\\Desktop\\jsonvalidator.json"))))
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200);
    }
}
