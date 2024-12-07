import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginControllerTest {

    private static final String BASE_URL = "http://localhost:8089/api/system";

    @Test
    public void testLogin() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(BASE_URL + "/login");
        JsonObject loginParm = new JsonObject();
        loginParm.addProperty("username", "admin");
        loginParm.addProperty("password", "666666");
        loginParm.addProperty("userType", "1");

        StringEntity entity = new StringEntity(new Gson().toJson(loginParm));
        entity.setContentType("application/json");
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test POST /login: Passed");
    }

    @Test
    public void testGetInfo() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/getInfo?userId=3");
        // 假设token已经获取
        request.setHeader("token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJUeXBlIjoiMSIsImV4cCI6MTczMzU1OTI4OCwiaWF0IjoxNzMzNTU3NDg4Nzg1fQ.3ALnV5FS2lxJHpS6riFtO5VcSc2x7ZR-GxnUEaBjU3uAb_waJINpiIMFNzekKyTF58rTo0YMzrWg_-X9Ao93QQ");

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test GET /getInfo: Passed");
    }

    @Test
    public void testGetMenuList() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/getMenuList");
        // 假设token已经获取
        request.setHeader("token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJUeXBlIjoiMSIsImV4cCI6MTczMzU1OTI4OCwiaWF0IjoxNzMzNTU3NDg4Nzg1fQ.3ALnV5FS2lxJHpS6riFtO5VcSc2x7ZR-GxnUEaBjU3uAb_waJINpiIMFNzekKyTF58rTo0YMzrWg_-X9Ao93QQ");

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test GET /getMenuList: Passed");
    }
}