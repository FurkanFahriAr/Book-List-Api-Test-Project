import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class APITestCase {
    public static RequestSpecification httpRequest;

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "http://restapi.store.com/api/books/";
        httpRequest = RestAssured.given();
    }

    @AfterTest
    public void tearDown() {
    }
}
