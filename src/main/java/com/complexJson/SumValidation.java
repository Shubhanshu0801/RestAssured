package com.complexJson;

import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {
    @Test
    public void sumValidation() {
        //6. Verify if sum of all course prices matches with purchase amount.
        JsonPath js = new JsonPath(Payload.courseDetails());
        int expectedPurchaseAmount = js.getInt("dashboard.purchaseAmount");
        int courseCount = js.getInt("courses.size()");
        int actualPurchaseAmount = 0;
        for(int i=0; i<courseCount; i++) {
            int coursePrice = js.getInt("courses["+i+"].price");
            int copiesSold = js.getInt("courses["+i+"].copies");
            actualPurchaseAmount = actualPurchaseAmount + (coursePrice * copiesSold);
        }
        Assert.assertEquals(actualPurchaseAmount, expectedPurchaseAmount, "Amount is matched");
    }
}
