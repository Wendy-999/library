import com.itmk.web.sys_notice.entity.SysNotice;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SysNoticeControllerTest {

    // 初始化测试环境
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/notice";
    }

    // 测试新增通知接口
    @Test
    public void testAddNotice() {
        // 准备测试数据
        SysNotice sysNotice = new SysNotice();
        sysNotice.setTitle("Test Notice");
        sysNotice.setContent("This is a test notice.");
        // 发送请求并验证响应
        given()
                .contentType(ContentType.JSON)
                .body(sysNotice)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("message", equalTo("新增成功"));
    }

    // 测试编辑通知接口
    @Test
    public void testEditNotice() {
        // 准备测试数据
        SysNotice sysNotice = new SysNotice();
        sysNotice.setId(1L);
        sysNotice.setTitle("Updated Test Notice");
        sysNotice.setContent("This is an updated test notice.");
        // 发送请求并验证响应
        given()
                .contentType(ContentType.JSON)
                .body(sysNotice)
                .when()
                .put()
                .then()
                .statusCode(200)
                .body("message", equalTo("编辑成功"));
    }

    // 测试删除通知接口
    @Test
    public void testDeleteNotice() {
        // 发送请求并验证响应
        given()
                .when()
                .delete("/{noticeId}", 1L)
                .then()
                .statusCode(200)
                .body("message", equalTo("删除成功"));
    }

    // 测试获取通知列表接口
    @Test
    public void testGetNoticeList() {
        // 发送请求并验证响应
        given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .body("message", equalTo("查询成功"));
    }

    // 测试获取顶部通知列表接口
    @Test
    public void testGetTopNoticeList() {
        // 发送请求并验证响应
        given()
                .when()
                .get("/getTopList")
                .then()
                .statusCode(200)
                .body("message", equalTo("查询成功"));
    }

    // 清理测试环境
    @AfterAll
    public static void tearDown() {
        // 清理测试数据
    }
}
