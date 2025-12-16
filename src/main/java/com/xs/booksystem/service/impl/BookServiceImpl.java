package com.xs.booksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xs.booksystem.pojo.DO.BookDO;
import com.xs.booksystem.pojo.DTO.BookDTO;
import com.xs.booksystem.pojo.VO.BookVO;
import com.xs.booksystem.service.BookService;
import com.xs.booksystem.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 敛
* @description 针对表【book(图书表)】的数据库操作Service实现
* @createDate 2025-12-16 13:41:50
*/
@Service
@Slf4j
public class BookServiceImpl implements BookService{
    @Autowired
    private BookMapper bookMapper;

    @Override
    public Integer addBook(BookDO bookDO) {
        if (bookDO != null){
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(bookDO,bookDTO);
            bookMapper.insert(bookDTO);
            log.info("新增图书{}成功",bookDTO.getTitle());
            return 1;
        }
        return 0;
    }

    @Override
    public Integer deleteBook(List<BookDO> bookDOs) {
        if (bookDOs != null) {
            Set<Integer> ids = bookDOs.stream().map(BookDO::getId).collect(Collectors.toSet());
            bookMapper.deleteBatchIds(ids);
            log.info("删除图书{}成功",ids);
            return 1;
        }
        return 0;
    }

    @Override
    public BookVO updateBook(BookDO bookDO) {
        if (bookDO != null){
            BookDTO bookDTO = new BookDTO();
            BeanUtils.copyProperties(bookDO,bookDTO);
            bookMapper.updateById(bookDTO);
            log.info("修改图书{}成功",bookDTO.getId());
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(bookDTO,bookVO);
            return bookVO;
        }
        return null;
    }


    @Override
    public List<BookVO> queryBook(BookDO bookDO) {
        QueryWrapper<BookDTO> queryWrapper = new QueryWrapper<>();

        if (bookDO.getTitle() != null && !bookDO.getTitle().isEmpty()) {
            queryWrapper.like("title", bookDO.getTitle());
        }

        if (bookDO.getAuthor() != null && !bookDO.getAuthor().isEmpty()) {
            queryWrapper.like("author", bookDO.getAuthor());
        }

        if (bookDO.getIsbn() != null && !bookDO.getIsbn().isEmpty()) {
            queryWrapper.eq("isbn", bookDO.getIsbn());
        }

        if (bookDO.getCategoryId() != null) {
            queryWrapper.eq("category_id", bookDO.getCategoryId());
        }

        if (bookDO.getPublisher() != null && !bookDO.getPublisher().isEmpty()) {
            queryWrapper.like("publisher", bookDO.getPublisher());
        }

        // 执行查询
        List<BookDTO> bookDTOList = bookMapper.selectList(queryWrapper);

        // 转换为VO对象并返回
        return bookDTOList.stream().map(dto -> {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(dto, bookVO);
            return bookVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BookVO> queryAllBooks() {
        List<BookDTO> bookDTOList = bookMapper.selectList(null);
        return bookDTOList.stream().map(dto -> {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(dto, bookVO);
            return bookVO;
        }).collect(Collectors.toList());
    }

}
