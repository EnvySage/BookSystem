package com.xs.booksystem.mapper;

import com.xs.booksystem.pojo.DTO.BorrowRecordDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 敛
* @description 针对表【borrow_record(借阅记录表)】的数据库操作Mapper
* @createDate 2025-12-16 13:41:50
* @Entity com.xs.booksystem.pojo.DTO.BorrowRecord
*/
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecordDTO> {


    List<BorrowRecordVO> selectBorrowRecordWithDetails(Map<String, Object> params);
}
