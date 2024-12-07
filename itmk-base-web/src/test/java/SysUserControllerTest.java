import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SysUserController.class)
public class SysUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SysUserService sysUserService;

    @MockBean
    private SysRoleService sysRoleService;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private SysReaderService sysReaderService;

    @InjectMocks
    private SysUserController sysUserController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysUserController).build();
    }

    @Test
    void testAddUser() throws Exception {
        SysUser user = new SysUser();
        user.setUsername("testuser");
        user.setPassword("password");

        when(sysUserService.getOne(any())).thenReturn(null);
        doNothing().when(sysUserService).addUser(any());

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("新增用户成功!"));
    }

    @Test
    void testEditUser() throws Exception {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("newpassword");

        SysUser existingUser = new SysUser();
        existingUser.setUserId(1L);
        existingUser.setUsername("testuser");

        when(sysUserService.getOne(any())).thenReturn(existingUser);
        doNothing().when(sysUserService).editUser(any());

        mockMvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("编辑用户成功!"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        when(sysUserService.removeById(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("删除用户成功!"));
    }

    @Test
    void testGetList() throws Exception {
        PageParm parm = new PageParm(); 
        IPage<SysUser> list = new Page<>(); 
       when(sysUserService.list(parm)).thenReturn(list);

        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("查询成功"));
    }

    @Test
    void testGetRoleList() throws Exception {
        List<SysRole> roles = new ArrayList<>(); 
        when(sysRoleService.list()).thenReturn(roles);

        mockMvc.perform(get("/api/user/getRoleList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("查询成功"));
    }

    @Test
    void testGetRoleId() throws Exception {
        Long userId = 1L;
        UserRole role = new UserRole(); 
        when(userRoleService.getOne(any())).thenReturn(role);

        mockMvc.perform(get("/api/user/getRoleId")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("查询成功"));
    }

    @Test
    void testUpdatePassword() throws Exception {
        UpdatePasswordParm parm = new UpdatePasswordParm(); 
        parm.setOldPassword("oldpassword");
        parm.setPassword("newpassword");
        parm.setUserId(1L);

        Claims claims = mock(Claims.class);         

when(jwtUtils.getClaimsFromToken(anyString())).thenReturn(claims);
        when(claims.get("userType")).thenReturn("0"); 
        SysReader reader = new SysReader(); 
        when(sysReaderService.getById(parm.getUserId())).thenReturn(reader);
        when(sysReaderService.updateById(any())).thenReturn(true);

        mockMvc.perform(post("/api/user/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("密码修改成功!"));
    }

    @Test
    void testResetPassword() throws Exception {
        SysUser user = new SysUser();
        user.setUserId(1L);
        when(sysUserService.updateById(any())).thenReturn(true);

        mockMvc.perform(post("/api/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("重置密码成功!"));
    }
}
