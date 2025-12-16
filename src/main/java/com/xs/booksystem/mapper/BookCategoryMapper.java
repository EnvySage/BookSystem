package com.xs.booksystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xs.booksystem.pojo.DTO.BookCategoryDTO;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 敛
* @description 针对表【book_category(图书分类表)】的数据库操作Mapper
* @createDate 2025-12-16 13:41:50
* @Entity com.xs.booksystem.pojo.DTO.BookCategory
*/
@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategoryDTO> {


}
