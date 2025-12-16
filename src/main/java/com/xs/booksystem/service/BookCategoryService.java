package com.xs.booksystem.service;

import com.xs.booksystem.pojo.DTO.BookCategoryDTO;
import com.xs.booksystem.pojo.VO.BookCategoryVO;
import org.springframework.beans.BeanMetadataAttribute;

import java.util.List;


/**
* @author 敛
* @description 针对表【book_category(图书分类表)】的数据库操作Service
* @createDate 2025-12-16 13:41:50
*/
public interface BookCategoryService{

    List<BookCategoryVO> queryBookCategory();

    BookCategoryVO queryBookCategoryById(Integer categoryId);
}
