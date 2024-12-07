import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itmk.BooksApplication;
import com.itmk.web.sys_books.controller.SysBooksController;
import com.itmk.web.sys_books.entity.BookVo;
import com.itmk.web.sys_books.entity.SysBooks;
import com.itmk.web.sys_books.service.SysBooksService;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BooksApplication.class)  // 确保指定启动类
public class SysBooksControllerTest {

    @Autowired
    private SysBooksController sysBooksController;

    @MockBean
    private SysBooksService sysBooksService; // Mock the service

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysBooksController).build();
    }


    // 测试新增
    @Test
    public void testAddBook() throws Exception {
        SysBooks book = new SysBooks();
        book.setBookName("Test Book");
        book.setBookAuther("Test Author");

        when(sysBooksService.save(book)).thenReturn(true);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookName\":\"Test Book\", \"bookAuther\":\"Test Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("新增成功"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试编辑
    @Test
    public void testEditBook() throws Exception {
        SysBooks book = new SysBooks();
        book.setBookId(1L);
        book.setBookName("Updated Book");
        book.setBookAuther("Updated Author");

        when(sysBooksService.updateById(book)).thenReturn(true);

        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1, \"bookName\":\"Updated Book\", \"bookAuther\":\"Updated Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("编辑成功"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试删除
    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 1L;
        when(sysBooksService.removeById(bookId)).thenReturn(true);

        mockMvc.perform(delete("/api/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("删除成功"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试获取列表
    @Test
    public void testGetList() throws Exception {
        when(sysBooksService.getList(any())).thenReturn(mock(IPage.class)); // Mock the service method

        mockMvc.perform(get("/api/books/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("查询成功"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testGetHotBook() throws Exception {
        // 创建一个模拟的热门书籍列表
        List<BookVo> hotBooks = new ArrayList<>();
        BookVo book1 = new BookVo();
        book1.setName("Test Book 1");
        book1.setValue(100);

        BookVo book2 = new BookVo();
        book2.setName("Test Book 2");
        book2.setValue(200);

        hotBooks.add(book1);
        hotBooks.add(book2);

        // 模拟 sysBooksService.getHotBook() 返回该列表
        when(sysBooksService.getHotBook()).thenReturn(hotBooks);

        // 执行请求，期望返回 200 状态码和有效的数据
        mockMvc.perform(get("/api/books/getHotBook"))
                .andExpect(status().isOk())  // 期望返回状态 200
                .andExpect(jsonPath("$.msg").value("查询成功"))  // 期望 msg 为 "查询成功"
                .andExpect(jsonPath("$.code").value(200))  // 期望 code 为 200
                .andExpect(jsonPath("$.data").isArray())  // 确保 data 是数组
                .andExpect(jsonPath("$.data.length()").value(2))  // 确保返回的数组长度为 2
                .andExpect(jsonPath("$.data[0].name").value("Test Book 1"))  // 验证第一个书名
                .andExpect(jsonPath("$.data[1].name").value("Test Book 2"));  // 验证第二个书名
    }



}
