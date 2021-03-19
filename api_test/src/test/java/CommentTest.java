import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import pojo.Comment;
import pojo.Post;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentTest extends BaseTest {
    private static String resourcePath = "/v1/comment";
    private static Integer createdComment = 0;
    private static Integer createdPost = 0;

    @BeforeClass
    public void updateStaticVariables(){
        //create a new Post to use for comments
        PostTests post = new PostTests();
        createdPost = post.createPost();
    }

    @AfterGroups(groups = "delete_comment")
    @BeforeGroups(groups = {"delete_comment","create_comment"})
    public void createComment() {
        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.basicAuthentication())
                .body(testComment)
                .post(resourcePath+"/"+createdPost);

        JsonPath jsonPathEvaluator = response.jsonPath();
        createdComment = jsonPathEvaluator.get("id");
    }

    @Test
    public void Test_Creat_Comment_Success() {
        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.basicAuthentication())
                .body(testComment)
                .post(resourcePath+"/"+createdPost)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Test_Creat_Comment_Unsuccessful() {
        Comment testComment = new Comment("", "");
        given()
                .spec(RequestSpecs.basicAuthentication())
                .body(testComment)
                .post(resourcePath+"/"+createdPost)
                .then()
                .body("message", equalTo("Invalid form"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test(groups = "create_comment")
    public void Test_Get_All_Comments_Success() {
        System.out.println(resourcePath+"s/"+createdPost);
        given()
                .spec(RequestSpecs.basicAuthentication())
                .get(resourcePath+"s/"+createdPost)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test(groups = "create_comment")
    public void Test_Get_All_Comments_Unsuccessful() {
        System.out.println(resourcePath+"s/"+createdPost);
        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath+"s/"+createdPost)
                .then()
                .body("message", equalTo("Please login first"))
                .and()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_comment")
    public void Test_One_Comment_Success() {
        System.out.println(resourcePath + "/" + createdPost + "/" + createdComment);
        given()
                .spec(RequestSpecs.basicAuthentication())
                //.body(testPost)
                .get(resourcePath + "/" + createdPost + "/" + createdComment)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_comment")
    public void Test_One_Comment_Unsuccessful() {
        given()
                .spec(RequestSpecs.basicAuthentication())
                //.body(testPost)
                .get(resourcePath + "/" + createdPost + "/" + 0)
                .then()
                .body("Message", equalTo("Comment not found"))
                .and()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_comment")
    public void Test_last_Comments_created() {
        System.out.println(resourcePath + "/" + createdPost + "/" + createdComment);
        given()
                .spec(RequestSpecs.basicAuthentication())
                .get(resourcePath + "/" + createdPost + "/" + createdComment)
                .then()
                .body("data.id", equalTo(createdComment));
    }

    @Test (groups = "create_comment")
    public void Test_Update_Comment_Success() {

        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuthentication())
                .body(testComment)
                .put(resourcePath + "/" + createdPost + "/" + createdComment)
                .then()
                .body("message", equalTo("Comment updated"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test (groups = "create_comment")
    public void Test_Update_Comment_Unsuccessful() {
        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuthentication())
                .body(testComment)
                .put(resourcePath + "/" + createdPost + "/" + -10)
                .then()
                .body("message", equalTo("Comment could not be updated"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test(groups = "delete_comment")
    public void Test_Delete_Comment_Success() {

        given()
                .spec(RequestSpecs.basicAuthentication())
                .delete(resourcePath + "/" + createdPost + "/" + createdComment)
                .then()
                .body("message", equalTo("Comment deleted"))
                .and()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }
//
    @Test (groups = "delete_comment")
    public void Test_Delete_Comment_Unsuccessful() {

        given()
                .spec(RequestSpecs.basicAuthentication())
                .delete(resourcePath + "/" + createdPost + "/" + -10)
                .then()
                .body("message", equalTo("Comment could not be deleted"))
                .and()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

}
