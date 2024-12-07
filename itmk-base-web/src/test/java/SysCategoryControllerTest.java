import com.itmk.BooksApplication;
import com.itmk.web.sys_category.controller.SysCategoryController;
import com.itmk.web.sys_category.entity.CategoryEcharts;
import com.itmk.web.sys_category.entity.CategoryVo;
import com.itmk.web.sys_category.entity.SysCategory;
import com.itmk.web.sys_category.service.SysCategoryService;
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

@SpringBootTest(classes = BooksApplication.class)
public class SysCategoryControllerTest {

    @Autowired
    private SysCategoryController sysCategoryController;

    @MockBean
    private SysCategoryService sysCategoryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysCategoryController).build();
    }

    // 测试新增分类
    @Test
    public void testAddCategory() throws Exception {
        SysCategory category = new SysCategory();
        category.setCategoryName("Test Category");

        when(sysCategoryService.save(category)).thenReturn(true);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"Test Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("新增成功!"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试编辑分类
    @Test
    public void testEditCategory() throws Exception {
        SysCategory category = new SysCategory();
        category.setCategoryId(1L);
        category.setCategoryName("Updated Category");

        when(sysCategoryService.updateById(category)).thenReturn(true);

        mockMvc.perform(put("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\":1, \"categoryName\":\"Updated Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("编辑成功!"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试删除分类
    @Test
    public void testDeleteCategory() throws Exception {
        Long categoryId = 1L;
        when(sysCategoryService.removeById(categoryId)).thenReturn(true);

        mockMvc.perform(delete("/api/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("删除成功!"))
                .andExpect(jsonPath("$.code").value(200));
    }

    // 测试获取分类列表
    @Test
    public void testGetCateList() throws Exception {
        // 使用无参构造方法并设置属性
        List<SysCategory> categoryList = new ArrayList<>();
        SysCategory category1 = new SysCategory();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");
        category1.setOrderNum(1L);

        SysCategory category2 = new SysCategory();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category 2");
        category2.setOrderNum(2L);

        categoryList.add(category1);
        categoryList.add(category2);

        // 模拟 sysCategoryService.list() 返回该列表
        when(sysCategoryService.list()).thenReturn(categoryList);

        // 执行请求并验证结果
        mockMvc.perform(get("/api/category/cateList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("查询成功"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].categoryName").value("Category 1"))
                .andExpect(jsonPath("$.data[1].categoryName").value("Category 2"));
    }

    // 测试获取分类统计
    @Test
    public void testCategoryCount() throws Exception {
        // 创建一个模拟的 CategoryEcharts 对象
        CategoryEcharts categoryEcharts = new CategoryEcharts();

        // 创建模拟的 CategoryVo 数据
        List<CategoryVo> categoryVoList = new ArrayList<>();
        CategoryVo categoryVo1 = new CategoryVo();
        categoryVo1.setCategoryName("Category 1");
        categoryVo1.setBookCount(100);

        CategoryVo categoryVo2 = new CategoryVo();
        categoryVo2.setCategoryName("Category 2");
        categoryVo2.setBookCount(150);

        categoryVoList.add(categoryVo1);
        categoryVoList.add(categoryVo2);

        // 设置返回的 categoryVo 数据
        categoryEcharts.setNames(List.of("Category 1", "Category 2"));
        categoryEcharts.setCounts(List.of(100, 150));

        // 模拟 sysCategoryService.getCategoryVo() 返回该对象
        when(sysCategoryService.getCategoryVo()).thenReturn(categoryEcharts);

        // 执行请求并验证返回结果
        mockMvc.perform(get("/api/category/categoryCount"))
                .andExpect(status().isOk())  // 期望返回 200 状态码
                .andExpect(jsonPath("$.msg").value("查询成功"))  // 验证 msg
                .andExpect(jsonPath("$.code").value(200))  // 验证 code
                .andExpect(jsonPath("$.data.names").isArray())  // 确保 names 是数组
                .andExpect(jsonPath("$.data.names.length()").value(2))  // 确保 names 数组长度为 2
                .andExpect(jsonPath("$.data.names[0]").value("Category 1"))  // 验证第一个分类名
                .andExpect(jsonPath("$.data.names[1]").value("Category 2"))  // 验证第二个分类名
                .andExpect(jsonPath("$.data.counts[0]").value(100))  // 验证第一个分类书籍数量
                .andExpect(jsonPath("$.data.counts[1]").value(150));  // 验证第二个分类书籍数量
    }

}
