package com.pojoSerialization;

import io.restassured.RestAssured;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class Serialization {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy(50);
        addPlace.setAddress("29, side layout, cohen 09");
        addPlace.setLanguage("French-IN");
        addPlace.setName("Frontline house");
        addPlace.setPhone_number("(+91) 983 893 3937");
        addPlace.setWebsite("http://google.com");
        List<String> typesList = new ArrayList<>();
        typesList.add("shoe park");
        typesList.add("shop");
        addPlace.setTypes(typesList);
        Location l = new Location();
        l.setLat(-38.383494);
        l.setLng(33.427362);
        addPlace.setLocation(l);
        String response = given().log().all().queryParam("key", "qaclick123").body(addPlace)
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
    }
}
