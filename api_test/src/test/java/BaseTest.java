import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;

public class BaseTest {

    @Parameters("baseUrl")
    @BeforeClass
    public void setup(@Optional("https://localhost:9000") String baseUrl ) {

        RestAssured.baseURI = baseUrl;
    }

}
