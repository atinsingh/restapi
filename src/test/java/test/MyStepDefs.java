package test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.CucumberOptions;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.auth.AUTH;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:resources",
        glue = "test"
)
public class MyStepDefs {
    String token ;
    Response response;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://pragrablog.herokuapp.com";
        RestAssured.basePath = "/api";
    }

    @Given("^User is authorized for the request with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void userIsAuthorizedForTheRequestWithUsernameAndPassword(String user, String pass) throws Throwable {
         ObjectMapper mapper = new ObjectMapper();
         ObjectNode objectNode = mapper.createObjectNode();
         objectNode.put("username",user).put("password", pass);

        JsonPath jsonPath =  RestAssured.given().headers("Content-Type", "application/json").body(objectNode).when().post("/authenticate").then().extract().jsonPath();
        token = jsonPath.get("id_token");
    }

    @When("^User calls the \"([^\"]*)\" with path param as (\\d+)$")
    public void userCallsTheWithPathParamAs(String endpoint, int id) throws Throwable {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader(Constants.AUTH, "Bearer " + token);
        builder.addPathParam("id", id);
        response = RestAssured.given(builder.build())
                .when()
                .get(endpoint);
    }
    @Then("^Response should have status code as (\\d+)$")
    public void responseShouldHaveStatusCodeAs(int statusCode) throws Throwable {
        response.then().assertThat().statusCode(statusCode);
    }

    @And("^resposne should have name as  \"([^\"]*)\"$")
    public void resposneShouldHaveNameAs(String name) throws Throwable {
       response.then().assertThat().body("name", equalTo(name));
    }
}
