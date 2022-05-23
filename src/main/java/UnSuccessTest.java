import objects.Register;
import objects.UnSuccessReg;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UnSuccessTest {
    public final static String URL = "https://reqres.in/";


    @Test
    public void unSuccessRegTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec400());
        UnSuccessReg unSuccessReg = given()
                .body(new Register("sydney@fife", ""))
                .when()
                .post("api/register")
                .then()
                .log()
                .all()
                .extract().as(UnSuccessReg.class);
        Assert.assertNotNull(unSuccessReg);
        Assert.assertEquals(unSuccessReg.getError(), "Missing password");
    }

    @Test
    public void unSuccessLoginTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec400());
        UnSuccessReg unSuccessReg = given()
                .body(new Register("peter@klaven", ""))
                .when()
                .post("api/login")
                .then()
                .log()
                .all()
                .extract().as(UnSuccessReg.class);
        Assert.assertNotNull(unSuccessReg);
        Assert.assertEquals(unSuccessReg.getError(), "Missing password");
    }
}
