package com.eComm;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class ECommerceAPITest {
    public static void main(String[] args) {
        //1. Login
        RequestSpecification reqLogin = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("anshu.practice@gmail.com");
        loginRequest.setUserPassword("Practice@12345");

        LoginResponse responseLogin = given().relaxedHTTPSValidation().log().all().spec(reqLogin).body(loginRequest).when()
                .post("/api/ecom/auth/login").then().log().all().extract().response()
                .as(LoginResponse.class);
        String token = responseLogin.getToken();
        String userId = responseLogin.getUserId();

        //2.Create Product
        RequestSpecification createProductBaseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", token)
                .build();

        RequestSpecification reqCreateProduct = given().relaxedHTTPSValidation().log().all().spec(createProductBaseReq).param("productName", "Shirt")
                .param("productAddedBy", userId).param("productCategory", "fashion")
                .param("productSubCategory", "Shirts").param("productPrice", "4500")
                .param("productDescription", "Levis").param("productFor", "Men")
                .multiPart("ProductImage", new File("C:\\Users\\Ishani\\Downloads\\shirt.jpg"));

        String responseCreateProduct = reqCreateProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath js = new JsonPath(responseCreateProduct);
        String productId = js.getString("productId");

        //3.Create Order
        RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
                .addHeader("authorization", token)
                .setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productId);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        Orders orders = new Orders();
        orders.setOrders(orderDetailList);

        RequestSpecification createOrderReq = given().spec(createOrderBaseReq).body(orders);
        String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order")
                .then().extract().response().asString();
        System.out.println(responseAddOrder);

        //4. Delete Order
        RequestSpecification deleteProductBaseReq = new RequestSpecBuilder()
                .addHeader("authorization", token)
                .setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        RequestSpecification deleteProductReq = given().log().all().spec(deleteProductBaseReq)
                .pathParam("productId", productId);

        String deleteProductResponse = deleteProductReq.when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();

        JsonPath js1 = new JsonPath(deleteProductResponse);
        Assert.assertEquals("Product Deleted Successfully", js1.getString("message"));
    }
}
