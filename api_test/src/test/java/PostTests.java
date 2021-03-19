import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import pojo.Article;
import pojo.Post;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import java.util.Map;

import static io.restassured.RestAssured.given;

import static org.hamcrest.core.IsEqual.equalTo;

public class PostTests extends BaseTest {
    private static String resourcePath = "/v1/post";
    private static Integer createdPost  = 0;


    @BeforeGroups(groups = {"delete_post","create_post"})
    public int createPost() {
        Map<String ,String> pepe;
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath);

        JsonPath jsonPathEvaluator = response.jsonPath();
        createdPost = jsonPathEvaluator.get("id");
        return createdPost;
    }

    @Test
    public void Test_Creat_Post_Success() {

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Test_Creat_Post_Unsuccessful() {

        Post testPost = new Post("", "");
        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath)
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }


    @Test
    public void Test_Get_All_Post_Success() {

        given()
                .spec(RequestSpecs.generateToken())
                //.body(testPost)
                .get(String.format("%ss", resourcePath))
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Test_Get_All_Post_Unsuccessful() {

        given()
                .spec(RequestSpecs.basicAuthentication())
                .get(String.format("%ss", resourcePath))
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test (groups = "create_post")
    public void Test_One_Post_Success() {

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + createdPost)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_post")
    public void Test_One_Post_Unsuccessful() {

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + -1)
                .then()
                .body("Message", equalTo("Post not found"))
                .and()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_post")
    public void Test_last_Post_created() {

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + createdPost)
                .then()
                .body("data.id", equalTo(createdPost));
    }

    @Test (groups = "create_post")
    public void Test_Update_Post_Success() {

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .put(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Post updated"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_post")
    public void Test_Update_Post_Unsuccessful() {
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .put(resourcePath + "/" + -10)
                .then()
                .body("message", equalTo("Post could not be updated"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test(groups = "delete_post")
    public void Test_Delete_Post_Success() {

        given()
                .spec(RequestSpecs.generateToken())
                .delete(resourcePath + "/" + createdPost)
                .then()
                .body("message", equalTo("Post deleted"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "delete_post")
    public void Test_Delete_Post_Unsuccessful() {

        given()
                .spec(RequestSpecs.generateToken())
                .delete(resourcePath + "/" + -10)
                .then()
                .body("message", equalTo("Post could not be deleted"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }


}
