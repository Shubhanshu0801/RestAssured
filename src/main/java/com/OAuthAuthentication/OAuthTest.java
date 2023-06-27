package com.OAuthAuthentication;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static io.restassured.RestAssured.*;

public class OAuthTest {
    public static void main(String[] args) throws InterruptedException {
        //To get the authorization code
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://" +
//                "www.googleapis.com/auth/userinfo.email&" +
//                "auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22h" +
//                "jcfhcr43pj.apps.googleusercontent.com&response_type=code&" +
//                "redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
//        driver.findElement(By.xpath("//input[@id='identifierId']")).sendKeys("anshu.dubey4@gmail.com");
//        driver.findElement(By.xpath("//button[@jscontroller='soHxf']//span[text()='Next']")).click();
//        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Incredible@9");
//        driver.findElement(By.xpath("//button[@jscontroller='soHxf']//span[text()='Next']")).click();
//        Thread.sleep(5000);
//        String url = driver.getCurrentUrl();
        //Since Google has disabled gmail login through automation
        String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss" +
                "&code=4%2F0AbUR2VOw2mkbxmkFGN3RB-70oumAJFnfs1XdxBGmPSrrq1NJdw4w9qiKWOiMQEp-SCm34A" +
                "&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=1&prompt=consent";
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];
        System.out.println(code);

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
        System.out.println(accessToken);

        String response = given().queryParam("access_token", accessToken)
                .when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
        System.out.println(response);
    }
}
