package com.googleApis;

import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddUpdateAndGetPlace {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        //Add Place
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(Payload.addPlace()).when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope" ,equalTo("APP"))
                .extract().response().asString();
        System.out.println("Place added successfully");
        System.out.println(response);
        //Takes String and convert it into json
        //Parsing json
        JsonPath js = ReusableMethods.rawToJson(response);
        String placeId = js.getString("place_id");
        System.out.println(placeId);

        //Update place
        String newAddress = "Summer Walk, South Africa";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200);
        System.out.println("Address updated successfully");

        //Get Place
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json").then().log().all().statusCode(200).extract()
                .response().asString();
        System.out.println("Place got successfully");
        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
        String actualAddress = js1.getString("address");
        System.out.println(actualAddress);
        Assert.assertEquals(actualAddress, newAddress);
    }
}