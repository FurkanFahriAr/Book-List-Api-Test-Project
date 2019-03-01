import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleGetTest extends APITestCase {

    @Test
    public void verifyAPIEmptyStore() {
        // At the beginning of a test case, there should be no books stored on the server.
        Response response = httpRequest.request(Method.GET);
        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }

    @Test
    public void verifyTitleRequired() {
        //PUT on /api/books/ should return an error Field '<field_name>' is required.
        JSONObject requestParams = new JSONObject();
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "400");

        String errorCode = response.jsonPath().get("error");
        Assert.assertEquals( "Field 'title' is required", errorCode);
    }
    @Test
    public void verifyAuthorRequired() {
        //PUT on /api/books/ should return an error Field '<field_name>' is required.
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "400");

        String errorCode = response.jsonPath().get("error");
        Assert.assertEquals( "Field 'author' is required", errorCode);
    }

    @Test
    public void verifyTitleCanNotEmpty() {
        //PUT on /api/books/ should return an error Field '<field_name>' cannot be empty
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "");
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "400");

        String errorCode = response.jsonPath().get("error");
        Assert.assertEquals( "Field 'title' cannot be empty", errorCode);
    }

    @Test
    public void verifyAuthorCanNotEmpty() {
        //PUT on /api/books/ should return an error Field '<field_name>' cannot be empty
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        requestParams.put("author", "");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "400");

        String errorCode = response.jsonPath().get("error");
        Assert.assertEquals( "Field 'author' cannot be empty", errorCode);
    }

    @Test
    public void verifyIdReadOnly() {
        //You shouldn't be able to send it in the PUT request to /api/books/.
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "400");

        String errorCode = response.jsonPath().get("error");
        Assert.assertEquals( "Field 'author' cannot be empty", errorCode);
    }

    @Test
    public void verifyCanCreateNewBookViaPUT() {
        //The book should be returned in the response.
        // GET on /api/books/<book_id>/ should return the same book.
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "201");

        Response getResponse = httpRequest.request(Method.GET);
        String responseBody = getResponse.getBody().asString();
        System.out.println(responseBody);
        int getStatusCode = getResponse.getStatusCode();
        Assert.assertEquals(getStatusCode, 200);

        String authorCode = response.jsonPath().get("author");
        String titleCode = response.jsonPath().get("title");

        Assert.assertEquals( requestParams.get("author"), authorCode);
        Assert.assertEquals( requestParams.get("title"), titleCode);
    }

    @Test
    public void verifyCanNotCreateDuplicateBook() {
        //PUT on /api/books/ should return an error: Another book with similar title and author already exist
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams.toJSONString());
        Response response = httpRequest.put("/1");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "201");

        JSONObject requestParams2 = new JSONObject();
        requestParams.put("title", "Reliability of late night deployments");
        requestParams.put("author", "John Smith");
        httpRequest.body(requestParams2.toJSONString());
        Response response2 = httpRequest.post("/2");

        int statusCode2 = response.getStatusCode();
        Assert.assertEquals(statusCode2, "400");
        String errorCode2 = response2.jsonPath().get("error");
        Assert.assertEquals( "Another book with similar title and author already exist", errorCode2);
    }

}