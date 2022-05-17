import io.restassured.http.ContentType;
import objects.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    public final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all().extract().body().jsonPath().getList("data", UserData.class);

        users.stream().forEach(user -> Assert.assertTrue(user.getAvatar().contains(user.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(user -> user.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void successRegTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessReq successReq = given()
                .body(user)
                .when()
                .post("api/register")
                .then()
                .log()
                .all()
                .extract().as(SuccessReq.class);
        Assert.assertNotNull(successReq);
        Assert.assertEquals(successReq.getId(), id);
        Assert.assertEquals(successReq.getToken(), token);

    }

    @Test
    public void unSuccessRegTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec400());
        Register user = new Register("sydney@fife", "");
        UnSuccessReg unSuccessReg = given()
                .body(user)
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
    public void resourceYearOrderTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        List<ResourceData> resource = given()
                .when()
                .get("api/unknown")
                .then().log().all().extract().body().jsonPath().getList("data", ResourceData.class);
        List<Integer> years = resource.stream().map(item -> item.getYear()).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(years, sortedYears);

    }

    @Test
    public void removeTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Test
    public void timeTest() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec(200));
        UserTime userTime = new UserTime("morpheus", "zion resident");
        ResponseTime responseTime = given()
                .body(userTime)
                .when()
                .log().all()
                .put("api/users/2")
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).log().all().extract().as(ResponseTime.class);
        String regex = "(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        Assert.assertEquals(responseTime.getUpdatedAt().replaceAll(regex, ""), currentTime);
    }

    @Test
    public void checkSuccessLogin() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        Register register = new Register("eve.holt@reqres.in", "eve.holt@reqres.in");

        ResponseLogin responseLogin = given()
                .body(register)
                .when()
                .post("api/login")
                .then().log().all().extract().as(ResponseLogin.class);
        Assert.assertEquals(responseLogin.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    public void checkResourceList() {
        Specifications.installSpecifications(Specifications.requestSpec(URL), Specifications.respSpec200());
        List<ResourceData> resourceDatas = given()
                .when()
                .log().all()
                .get("api/unknown")
                .then()
                .log().all().extract().body().jsonPath().getList("data", ResourceData.class);
        Assert.assertTrue(resourceDatas.stream().anyMatch(item -> item.getColor().equals("#7BC4C4")));
    }
}
