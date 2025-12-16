package com.xs.booksystem.service.impl;

import com.xs.booksystem.pojo.DTO.BookCategoryDTO;
import com.xs.booksystem.pojo.VO.BookCategoryVO;
import com.xs.booksystem.service.BookCategoryService;
import com.xs.booksystem.mapper.BookCategoryMapper;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 敛
* @description 针对表【book_category(图书分类表)】的数据库操作Service实现
* @createDate 2025-12-16 13:41:50
*/
@Service
public class BookCategoryServiceImpl implements BookCategoryService{
    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Override
    public List<BookCategoryVO> queryBookCategory() {
        List<BookCategoryDTO> bookCategoryDTOS = bookCategoryMapper.selectList(null);
        List<BookCategoryVO> bookCategoryVOS = bookCategoryDTOS.stream().map(dto -> {
            BookCategoryVO bookCategoryVO = new BookCategoryVO();
            BeanUtils.copyProperties(dto, bookCategoryVO);
            return bookCategoryVO;
        }).collect(Collectors.toList());
        return bookCategoryVOS;
    }

    @Override
    public BookCategoryVO queryBookCategoryById(Integer categoryId) {
        BookCategoryDTO bookCategoryDTO = bookCategoryMapper.selectById(categoryId);
        if (bookCategoryDTO == null) {
            return null;
        }
        BookCategoryVO bookCategoryVO = new BookCategoryVO();
        BeanUtils.copyProperties(bookCategoryDTO, bookCategoryVO);
        return bookCategoryVO;
    }
}
