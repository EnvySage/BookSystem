package com.xs.booksystem.service;

import com.xs.booksystem.pojo.DO.BorrowRecordDO;
import com.xs.booksystem.pojo.DTO.BorrowRecordDTO;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;

import java.util.List;

/**
* @author 敛
* @description 针对表【borrow_record(借阅记录表)】的数据库操作Service
* @createDate 2025-12-16 13:41:50
*/
public interface BorrowRecordService {

    List<BorrowRecordVO> queryBorrowRecords(Integer userId);

    Integer deleteBorrowRecords(List<BorrowRecordDO> borrowRecords);

    BorrowRecordVO addBorrowRecord(BorrowRecordDO borrowRecordDO);

    BorrowRecordVO updateBorrowRecord(BorrowRecordDO borrowRecordDO);
}
