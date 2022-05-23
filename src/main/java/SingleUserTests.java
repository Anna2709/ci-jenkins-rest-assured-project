import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SingleUserTests {
    public final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec404());
        given()
                .when()
                .get("api/users/23")
                .then().log().all().statusCode(404);
    }
}
