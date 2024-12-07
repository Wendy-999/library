import com.itmk.utils.ResultVo;
import com.itmk.web.book_borrow.entity.BorrowBook;
import com.itmk.web.book_borrow.entity.BorrowParm;
import com.itmk.web.book_borrow.entity.ExceptionParm;
import com.itmk.web.book_borrow.entity.ListParm;
import com.itmk.web.book_borrow.entity.ReturnParm;
import com.itmk.web.book_borrow.service.BorrowBookService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BorrowBookControllerTest {

    @InjectMocks
    private BorrowBookController borrowBookController;

    @Mock
    private BorrowBookService borrowBookService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplyBook() {
        BorrowBook borrowBook = new BorrowBook();
        borrowBook.setBorrowId(1L);

        when(borrowBookService.updateById(borrowBook)).thenReturn(true);

        ResultVo result = borrowBookController.applyBook(borrowBook);
        assertEquals("审核成功!", result.getMessage());
    }

    @Test
    public void testBorrow() {
        BorrowParm borrowParm = new BorrowParm();
        String token = "valid-token";
        Claims claims = mock(Claims.class);

        when(request.getHeader("token")).thenReturn(token);
        when(jwtUtils.getClaimsFromToken(token)).thenReturn(claims);
        when(claims.get("userType")).thenReturn("0");
        when(borrowBookService.borrow(any(BorrowParm.class), any(String.class))).thenReturn(true);

        ResultVo result = borrowBookController.borrow(borrowParm, request);
        assertEquals("借书成功!", result.getMessage());
    }

    @Test
    public void testGetBorrowList() {
        ListParm listParm = new ListParm();
        when(borrowBookService.getBorrowList(any(ListParm.class))).thenReturn(null); // Mock the return value

        ResultVo result = borrowBookController.getBorrowList(listParm);
        assertEquals("查询成功", result.getMessage());
    }

    @Test
    public void testReturnBooks() {
        ReturnParm returnParm = new ReturnParm();
        when(borrowBookService.returnBook(anyList())).thenReturn(true);

        ResultVo result = borrowBookController.returnBooks(Collections.singletonList(returnParm));
        assertEquals("还书成功!", result.getMessage());
    }

    @Test
    public void testExceptionBooks() {
        ExceptionParm exceptionParm = new ExceptionParm();
        when(borrowBookService.exceptionBook(any(ExceptionParm.class))).thenReturn(true);

        ResultVo result = borrowBookController.exceptionBooks(exceptionParm);
        assertEquals("还书成功!", result.getMessage());
    }

    @Test
    public void testGetLookBorrowList() {
        String token = "valid-token";
        Claims claims = mock(Claims.class);
        LookParm lookParm = new LookParm();

        when(request.getHeader("token")).thenReturn(token);
        when(jwtUtils.getClaimsFromToken(token)).thenReturn(claims);
        when(claims.get("userType")).thenReturn("0");
        when(borrowBookService.getReaderLookBorrowList(any(LookParm.class))).thenReturn(null); // Mock the return value

        ResultVo result = borrowBookController.getLookBorrowList(lookParm, request);
        assertEquals("查询成功", result.getMessage());
    }

    @Test
    public void testAddTime() {
        BorrowParm borrowParm = new BorrowParm();
        borrowParm.setBorrowId(1L);
        borrowParm.setReturnTime(new Date());

        when(borrowBookService.updateById(any(BorrowBook.class))).thenReturn(true);

        ResultVo result = borrowBookController.addTime(borrowParm);
        assertEquals("续期成功!", result.getMessage());
    }

    @Test
    public void testGetBorrowApplyCount() {
        String userType = "0";
        Long userId = 1L;
        when(borrowBookService.count(any())).thenReturn(5);

        ResultVo result = borrowBookController.getBorrowApplyCount(userType, userId);
        assertEquals(5, result.getData());
    }

    @Test
    public void testGetBorrowReturnCount() {
        String userType = "0";
        Long userId = 1L;
        when(borrowBookService.count(any())).thenReturn(3);

        ResultVo result = borrowBookController.getBorrowReturnCount(userType, userId);
        assertEquals(3, result.getData());
    }
}