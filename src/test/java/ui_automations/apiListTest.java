package ui_automations;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class apiListTest {
	int lstID;
    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://localhost:3000/";
    }
    
    @Test(priority = 1)
    public void postRequest() {
    
    	String jsonPayload = "{\n" +
    		    "  \"boardId\": \"1\",\n" +
    		    "  \"name\": \"test_1\",\n" +
    		    "  \"order\": 34\n" +
    		    "}";
    	
    	Response response = given()
        	.contentType(ContentType.JSON)
        	.accept(ContentType.JSON) 
        	.body(jsonPayload)
        	
        	
    	
        	.when()
        	.post("api/lists")
    	
        	.then()
        	.statusCode(201)
        	.log().all()
        	.extract()
        	.response();
    	
    	lstID = response.jsonPath().getInt("id");
    	//deleteRequest(lstID);
    	System.out.println(lstID + "asdf");
    	
    }
    
    @Test(priority = 2, dependsOnMethods = "postRequest")
    public void deleteRequest() {
    	given()
    	.when()
    	.delete("api/lists/"+ lstID)
    	.then()
    	.statusCode(200)
    	.log().all();
    	
    }

}
