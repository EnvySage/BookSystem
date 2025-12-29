package com.xs.booksystem.service;

import com.xs.booksystem.pojo.DO.BookDO;
import com.xs.booksystem.pojo.DTO.BookDTO;
import com.xs.booksystem.pojo.VO.BookVO;

import java.util.List;

/**
* @author 敛
* @description 针对表【book(图书表)】的数据库操作Service
* @createDate 2025-12-16 13:41:50
*/
public interface BookService{

    Integer addBook(BookDO bookDO);

    Integer deleteBook(List<BookDO> bookDOs);

    BookVO updateBook(BookDO bookDO);

    List<BookVO> queryBook(BookDO bookDO);

    List<BookVO> queryAllBooks();

    Integer queryStockById(Integer bookId);

    List<BookVO> queryRecommendedBooks();

    BookDTO getBookById(BookDTO bookDTO);
}
