package com.libraryApi;

import files.Payload;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddBookUsingDataProvider {
    @DataProvider
    public Object[][] addBookData() {
        Object[][] data = new Object[][]{
            {"hdj", "369"},
            {"dbi", "370"},
            {"sjo", "479"}
        };
        return data;
    }
    @Test(dataProvider = "addBookData")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
    }
}
