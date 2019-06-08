package test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;


public class APITest {

    String token;
    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = "http://pragrablog.herokuapp.com";
        RestAssured.basePath = "/api";  
    }

    @BeforeClass
    public void auth(){
       ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("username","admin");
        objectNode.put("password","admin");


        JsonPath path =  RestAssured.given().headers("Content-Type", ContentType.JSON).body(objectNode).when().post("/authenticate").then().extract().jsonPath();
       token  =  path.get("id_token");
        System.out.println("Token Value is "+ token);

    }

    @Test
    public void testGetAccount() {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addHeader("Authorization", "Bearer " + token);
        specBuilder.addHeader("Content-Type", "application/json");
        RestAssured.given(specBuilder.build()).when()
                .get("/account")
                .then()
                .statusCode(200)
                .assertThat()
                .body("firstName", equalTo("string1"))
                .body("lastName", equalTo("string"));
    }
}
