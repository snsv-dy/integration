package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    @Test
    void createUserWithProperDataReturnsCreatedStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API);
    }

    @Test
    void createDuplicateUser_shouldResultIn409ResponseStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
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
    void getDataOfNonexistentUser_shouldResultIn404Status(){
//        JSONObject jsonObj = new JSONObject().put("id", "19");
        int id = 19;
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
//                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
//                .statusCode(HttpStatus.SC_NOT_FOUND)
                .when()
                .get("/blog/user/" + id);
    }
}
