import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmk.web.sys_role.entiy.SaveAssign;
import com.itmk.web.sys_role_menu.service.RoleMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest @AutoConfigureMockMvc public class SysRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleMenuService roleMenuService;

    @Test
    public void testAssignSave() throws Exception {
        // 创建 SaveAssign 对象
        SaveAssign saveAssign = new SaveAssign();
        saveAssign.setRoleId(1L); // 设置角色ID
        List<Long> menuIds = new ArrayList<>();
        menuIds.add(1L); // 添加菜单ID
        saveAssign.setList(menuIds);

        // 配置 RoleMenuService 的行为
        doNothing().when(roleMenuService).assignSave(anyLong(), anyList());

        // 发送 POST 请求并验证响应
        mockMvc.perform(post("/api/role/assignSave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAssign)))
                .andExpect(statu().isOk());
    }

    private StatusResultMatchers statu() {
        return null;
    }
}
