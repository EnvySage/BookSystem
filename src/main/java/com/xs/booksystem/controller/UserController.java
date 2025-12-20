package com.xs.booksystem.controller;

import com.xs.booksystem.pojo.DO.BookDO;
import com.xs.booksystem.pojo.DO.BorrowRecordDO;
import com.xs.booksystem.pojo.Result;
import com.xs.booksystem.pojo.VO.BookCategoryVO;
import com.xs.booksystem.pojo.VO.BookVO;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;
import com.xs.booksystem.service.BookCategoryService;
import com.xs.booksystem.service.BookService;
import com.xs.booksystem.service.BorrowRecordService;
import com.xs.booksystem.service.impl.BorrowRecordServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookCategoryService bookCategoryService;

    @Autowired
    private BorrowRecordService borrowRecordService;
    private static final Logger logger = LoggerFactory.getLogger(BorrowRecordServiceImpl.class);
    /**
     * 查询图书
     * @param bookDO
     * 传入参数：title
     * @return
     */
    @PostMapping("/queryBooks")
    public Result<List<BookVO>> queryBook(@RequestBody BookDO bookDO){
        logger.info("收到查询图书请求");
        if (bookDO == null) return Result.error("参数错误");
        List<BookVO> bookVOs = bookService.queryBook(bookDO);
        for (BookVO bookVO : bookVOs){
            bookVO.setCategoryName(bookCategoryService.queryBookCategoryById(bookVO.getCategoryId()).getName());
        }
        if (bookVOs.size() == 0){
            logger.info("未查询到符合条件的图书");
            return Result.error("查询失败");
        }else {
            logger.info("成功查询到 {} 本图书，包括", bookVOs.size());
            return Result.success("查询成功",bookVOs);
        }
    }

    /**
     * 查询所有图书

     * @return
     */
    @GetMapping("/queryAllBooks")
    public Result<List<BookVO>> queryAllBooks(){
        logger.info("收到查询所有图书请求");
        List<BookVO> bookVOs = bookService.queryAllBooks();
        for (BookVO bookVO : bookVOs){
            bookVO.setCategoryName(bookCategoryService.queryBookCategoryById(bookVO.getCategoryId()).getName());
        }
        if (bookVOs.size() == 0){
            logger.info("未查询到任何图书");
            return Result.error("查询失败");
        }else {
            logger.info("成功查询到 {} 本图书", bookVOs.size());
            return Result.success("查询成功",bookVOs);
        }
    }

    /**
     * 获取分类列表
     * @return
     */
    @GetMapping("/queryBookCategory")
    public Result<List<BookCategoryVO>> queryBookCategory(){
        List<BookCategoryVO> bookCategoryVOS = bookCategoryService.queryBookCategory();
        if (bookCategoryVOS == null){
            logger.info("查询图书分类失败");
            return Result.error("查询失败");
        }else {
            logger.info("成功查询到 {} 个图书分类", bookCategoryVOS.size());
            return Result.success("查询成功",bookCategoryVOS);
        }
    }

    /**
     * 查询借阅记录
     * @param userId
     * @return
     */
    @GetMapping("/queryBorrowRecords/{userId}")
    public Result<List<BorrowRecordVO>> queryBorrowRecords(@PathVariable("userId") Integer userId){
        List<BorrowRecordVO> borrowRecords = borrowRecordService.queryBorrowRecords(userId);
        if (borrowRecords == null){
            logger.info("查询借阅记录失败");
            return Result.error("查询失败");
        }else {
            logger.info("成功查询到 {} 条借阅记录", borrowRecords.size());
            return Result.success("查询成功", borrowRecords);
        }
    }

    /**
     * 添加借阅记录
      * @param borrowRecordDO
     * 传输参数：bookId, userId, borrowTime, dueTime, status
     * @return
     */
    @PostMapping("/addBorrowRecord")
    public Result<BorrowRecordVO> addBorrowRecord(@RequestBody BorrowRecordDO borrowRecordDO){
        BorrowRecordVO borrowRecordVO = borrowRecordService.addBorrowRecord(borrowRecordDO);
        if (borrowRecordVO == null){
            logger.info("添加借阅记录失败");
            return Result.error("添加失败");
        }else {
            logger.info("添加借阅记录成功");
            return Result.success("添加成功", borrowRecordVO);
        }
    }

    /**
     * 修改借阅记录
     * @param borrowRecordDO
     * 传输参数：dueTime
     * @return
     */
     @PostMapping("/updateBorrowRecord")
    public Result<BorrowRecordVO> updateBorrowRecord(@RequestBody BorrowRecordDO borrowRecordDO){
        BorrowRecordVO borrowRecordVO = borrowRecordService.updateBorrowRecord(borrowRecordDO);
        if (borrowRecordVO == null){
            logger.info("修改借阅记录失败");
            return Result.error("修改失败");
        }else {
            logger.info("修改借阅记录成功");
            return Result.success("修改成功", borrowRecordVO);
        }
    }

    /**
     * 完成借阅
     * @param borrowRecordDO
     * 传输参数：userId, bookId, returnTime, status
     * @return
     */
     @PostMapping("/returnBook")
    public Result<BorrowRecordVO> returnBook(@RequestBody BorrowRecordDO borrowRecordDO){
        BorrowRecordVO borrowRecordVO = borrowRecordService.updateBorrowRecord(borrowRecordDO);
        if (borrowRecordVO == null){
            logger.info("归还失败");
            return Result.error("归还失败");
        }else {
            logger.info("归还成功");
            return Result.success("归还成功", borrowRecordVO);
        }
    }
}
