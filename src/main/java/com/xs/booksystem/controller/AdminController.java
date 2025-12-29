package com.xs.booksystem.controller;

import com.xs.booksystem.Context.BaseContext;
import com.xs.booksystem.pojo.DO.BookDO;
import com.xs.booksystem.pojo.DO.BorrowRecordDO;
import com.xs.booksystem.pojo.DO.UserDO;
import com.xs.booksystem.pojo.Result;
import com.xs.booksystem.pojo.VO.BookCategoryVO;
import com.xs.booksystem.pojo.VO.BookVO;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;
import com.xs.booksystem.pojo.VO.UserVO;
import com.xs.booksystem.service.BookCategoryService;
import com.xs.booksystem.service.BookService;
import com.xs.booksystem.service.BorrowRecordService;
import com.xs.booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    BorrowRecordService borrowRecordService;
    @Autowired
    private BookCategoryService bookCategoryService;
    /**
     * 添加图书
     * @param bookDO
     * isbn
     * title
     * author
     * publisher
     * publishDate
     * categoryId
     * description
     * coverImage
     * totalCopies
     * availableCopies
     * isRecommended
     * @return
     */
    @PostMapping("/addBook")
    public Result<BookDO> addBook(@RequestBody BookDO bookDO){
        if (bookDO == null) return Result.error("参数错误");
        Integer success = bookService.addBook(bookDO);
        if (success == 0){
            return Result.error("添加失败");
        }else return Result.success("添加成功",bookDO);
    }
    /**
     * 删除图书
     * @param bookDOs
     * id
     * @return
     */
    @PostMapping("/deleteBook")
    public Result<String> deleteBook(@RequestBody List<BookDO> bookDOs){
        if (bookDOs == null) return Result.error("参数错误");
        Integer success = bookService.deleteBook(bookDOs);
        if (success == 0){
            return Result.error("删除失败");
        }else return Result.success("删除成功");
    }
    /**
     * 修改图书
     * @param bookDO
     * id
     * isbn
     * title
     * author
     * publisher
     * publishDate
     * categoryId
     * description
     * coverImage
     * totalCopies
     * availableCopies
     * isRecommended
     * @return
     */
    @PostMapping("/updateBook")
    public Result<BookVO> updateBook(@RequestBody BookDO bookDO){
        if (bookDO == null) return Result.error("参数错误");
        BookVO bookVO = bookService.updateBook(bookDO);
        if (bookVO == null){
            return Result.error("修改失败");
        }else return Result.success("修改成功",bookVO);
    }

    /**
     * 查询图书
     * @param bookDO
     * isbn
     * title
     * author
     * publisher
     * publishDate
     * categoryId
     * description
     * coverImage
     * totalCopies
     * availableCopies
     * isRecommended
     * @return
     */
    @PostMapping("/queryBooks")
    public Result<List<BookVO>> queryBook(@RequestBody BookDO bookDO){
        if (bookDO == null) return Result.error("参数错误");
        List<BookVO> bookVOs = bookService.queryBook(bookDO);
        for (BookVO bookVO : bookVOs){
            bookVO.setCategoryName(bookCategoryService.queryBookCategoryById(bookVO.getCategoryId()).getName());
        }
        if (bookVOs.size() == 0){
            return Result.error("查询失败");
        }else return Result.success("查询成功",bookVOs);
    }

    /**
     * 查询所有图书
     * @return
     */
    @GetMapping("/queryAllBooks")
    public Result<List<BookVO>> queryAllBooks(){
        List<BookVO> bookVOs = bookService.queryAllBooks();
        for (BookVO bookVO : bookVOs){
            bookVO.setCategoryName(bookCategoryService.queryBookCategoryById(bookVO.getCategoryId()).getName());
        }
        if (bookVOs.size() == 0){
            return Result.error("查询失败");
        }else return Result.success("查询成功",bookVOs);
    }

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/queryAllUsers")
    public Result<List<UserVO>> queryAllUsers(){
        List<UserVO> users = userService.queryAllUsers();
        if (users == null){
            return Result.error("查询失败");
        }else return Result.success("查询成功",users);
    }

    /**
     * 删除用户
     * @param userDOs
     *  id
     * @return
     */
    @PostMapping("/deleteUsers")
    public Result<String> deleteUsers(@RequestBody List<UserDO> userDOs){
        if (userDOs == null) return Result.error("参数错误");
        Integer success = userService.deleteUsers(userDOs);
        if (success == 0){
            return Result.error("删除失败");
        }else return Result.success("删除成功");
    }

    /**
     * 查询借阅记录
     * @param
     * @return borrowRecords
     */
    @GetMapping("/queryBorrowRecords")
    public Result<List<BorrowRecordVO>> queryBorrowRecords(){
        List<BorrowRecordVO> borrowRecords = borrowRecordService.queryBorrowRecords(BaseContext.getCurrentId());
        if (borrowRecords == null){
            return Result.error("查询失败");
        }else return Result.success("查询成功",borrowRecords);
    }

    /**
     * 删除借阅记录
     * @param borrowRecords
     *  id
     * @return
     */
    @PostMapping("/deleteBorrowRecords")
    public Result<String> deleteBorrowRecords(@RequestBody List<BorrowRecordDO> borrowRecords){
        if (borrowRecords == null) return Result.error("参数错误");
        Integer success = borrowRecordService.deleteBorrowRecords(borrowRecords);
        if (success == 0){
            return Result.error("删除失败");
        }else return Result.success("删除成功");
    }

    /**
     * 添加借阅记录
      * @param borrowRecordDO
     * id
     * userId
     * bookId
     * borrowTime
     * returnTime
     * @return
     */
    @PostMapping("/addBorrowRecord")
    public Result<BorrowRecordVO> addBorrowRecord(@RequestBody BorrowRecordDO borrowRecordDO){
        if (borrowRecordDO == null) return Result.error("参数错误");
        BorrowRecordVO borrowRecordVO = borrowRecordService.addBorrowRecord(borrowRecordDO);
        if (borrowRecordVO == null){
             return Result.error("添加失败");
        }else return Result.success("添加成功",borrowRecordVO);
    }
    /**
     * 修改借阅记录
     * @param borrowRecordDO
     * id
     * userId
     * bookId
     * borrowTime
     * returnTime
     * status
     * @return
     */
    @PostMapping("/updateBorrowRecord")
    public Result<BorrowRecordVO> updateBorrowRecord(@RequestBody BorrowRecordDO borrowRecordDO){
        if (borrowRecordDO == null) return Result.error("参数错误");
        BorrowRecordVO borrowRecordVO = borrowRecordService.updateBorrowRecord(borrowRecordDO);
        if (borrowRecordVO == null){
            return Result.error("修改失败");
        }else return Result.success("修改成功",borrowRecordVO);
    }
    /**
     * 获取分类列表
     * @return
     */
    @GetMapping("/queryBookCategory")
    public Result<List<BookCategoryVO>> queryBookCategory(){
        List<BookCategoryVO> bookCategoryVOS = bookCategoryService.queryBookCategory();
        if (bookCategoryVOS == null){
            return Result.error("查询失败");
        }else return Result.success("查询成功",bookCategoryVOS);
    }

    /**
     * 获取推荐图书列表
     * @return bookVOs
     */
    @GetMapping("/queryRecommendedBooks")
    public Result<List<BookVO>> queryRecommendedBooks(){
        List<BookVO> bookVOs = bookService.queryRecommendedBooks();
        if (bookVOs == null){
            return Result.error("查询失败");
        }else return Result.success("查询成功",bookVOs);
    }
}
