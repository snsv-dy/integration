package edu.iis.mto.blog.rest.test;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherTests extends FunctionalTests {
    private static final String USER_API = "/blog/user";

    @BeforeAll
    public static void setUp() {

    }

    @Test
    void emailShouldBeUnique_WhenAddingNewUser() {
        JSONObject jsonObj = new JSONObject().put("email", "brian@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API);
    }

    @Test
    void addingPostByConfirmedUser_addingShouldBeAllowed() {
        int user_id = 1;
        JSONObject jsonObj = new JSONObject().put("entry", "post content (not important).");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API + "/" + user_id + "/post");
    }


    @Test
    void addingPostByNewUser_addingShouldNotBeAllowed() {
        int user_id = 3;
        JSONObject jsonObj = new JSONObject().put("entry", "post content (not important).");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API + "/" + user_id + "/post");
    }

    @Test
    void addingLikeByConfirmedUser_shouldSucceed() {
        int user_id = 1;
        int post_id = 1;
//        JSONObject jsonObj = new JSONObject().put("entry", "post content (not important).");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
//                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + "/" + user_id + "/like/" + post_id);
    }


    @Test
    void addingLikeByNewUser_shouldFail() {
        int user_id = 3;
        int post_id = 1;
//        JSONObject jsonObj = new JSONObject().put("entry", "post content (not important).");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
//                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/" + user_id + "/like/" + post_id);
    }

    @Test
    void repeatedLiking_SouldNotChangePostsLikeCount(){
        int post_id = 1;
        String res =
                given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get("/blog/post/" + post_id)
                .asString();
        int likes_before = from(res).get("likesCount");

        int user_id = 1;
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + "/" + user_id + "/like/" + post_id);

        res = given().accept(ContentType.JSON)
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .expect()
                        .log()
                        .all()
                        .statusCode(HttpStatus.SC_OK)
                        .when()
                        .get("/blog/post/" + post_id)
                        .asString();
        int likes_after = from(res).get("likesCount");

        assertEquals(likes_before, likes_after);
    }

    @Test
    void searchingForRemovedUserPosts_shouldFail() {
        int user_id = 4;
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .when()
                .get(USER_API + "/" + user_id + "/post");
    }

    @Test
    void searchingForUserPosts_shouldAlsoReturnLikes() {
        int user_id = 2;
        String res = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + "/" + user_id + "/post")
                .asString();
        List<Object> likes = from(res).get("likesCount");
        assertEquals(1, likes.size());
    }

    @Test
    void searchingForExistingUser_shouldSucced(){
        String searchString = "Miss";
        String res = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", searchString)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + "/find")
                .asString();
        List<Object> likes = from(res).get("$");
        assertEquals(1, likes.size());
    }


    @Test
    void searchingForRemovedUser_shouldFail(){
        String searchString = "Jeff";

        String res = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", searchString)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .when()
                .get(USER_API + "/find")
                .asString();
        List<Object> likes = from(res).get("$");
        assertEquals(0, likes.size());
    }
}
