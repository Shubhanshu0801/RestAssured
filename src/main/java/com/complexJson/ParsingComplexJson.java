package com.complexJson;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ParsingComplexJson {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(Payload.courseDetails());
        //1. Print the number of courses.
        int courseCount = js.getInt("courses.size()");
        System.out.println(courseCount);//size() is applied only on arrays.

        //2. Print purchase amount.
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        //3. Print title of the first course.
        //For String getString() is available but get() by default pull String value.
        String firstCourseTitle = js.get("courses[0].title");
        System.out.println(firstCourseTitle);

        //4. Print all course titles and their respective prices.
        for(int i=0; i<courseCount; i++) {
            String courseTitle = js.get("courses["+i+"].title");
            int coursePrice = js.getInt("courses["+i+"].price");
            System.out.println(courseTitle + "- " + coursePrice);
        }

        //5. Print number of copies sold by RPA course.
        for(int i=0; i<courseCount; i++) {
            String courseTitle = js.get("courses["+i+"].title");
            if(courseTitle.equals("RPA")) {
                int copiesCount = js.getInt("courses["+i+"].copies");
                System.out.println(copiesCount);
                break;
            }
        }
    }
}
