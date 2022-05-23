import objects.UserData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SingleUserTests {
    public final static String URL = "https://reqres.in/";

    @Test
    public void checkSingleUserTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        UserData user = given()
                .when()
                .get("api/users/2")
                .then().log().all().extract().body().jsonPath().getObject("data", UserData.class);

        Assert.assertTrue(user.getAvatar().contains(user.getId().toString()));
        Assert.assertTrue(user.getEmail().endsWith("@reqres.in"));
    }
}
