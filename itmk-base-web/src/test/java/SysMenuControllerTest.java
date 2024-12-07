import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SysMenuControllerTest {

    private static final String BASE_URL = "http://localhost:8089/api/menu";

    @Test
    public void testAddMenu() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(BASE_URL);
        JsonObject menu = new JsonObject();
        menu.addProperty("name", "Test Menu");
        menu.addProperty("parentId", 0);

        StringEntity entity = new StringEntity(new Gson().toJson(menu));
        entity.setContentType("application/json");
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test POST /menu: Passed");
    }

    @Test
    public void testEditMenu() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut(BASE_URL);
        JsonObject menu = new JsonObject();
        menu.addProperty("id", 1);
        menu.addProperty("name", "Updated Test Menu");

        StringEntity entity = new StringEntity(new Gson().toJson(menu));
        entity.setContentType("application/json");
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test PUT /menu: Passed");
    }

    @Test
    public void testDeleteMenu() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpDelete request = new HttpDelete(BASE_URL + "/1");

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test DELETE /menu/{menuId}: Passed");
    }

    @Test
    public void testGetList() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/list");

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test GET /menu/list: Passed");
    }

    @Test
    public void testGetParentList() throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/parent");

        HttpResponse response = client.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
        String responseBody = EntityUtils.toString(response.getEntity());
        assertNotNull(responseBody);
        System.out.println("Test GET /menu/parent: Passed");
    }
}