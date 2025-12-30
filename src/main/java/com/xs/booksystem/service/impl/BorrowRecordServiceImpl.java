package com.xs.booksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xs.booksystem.Context.BaseContext;
import com.xs.booksystem.pojo.DO.BorrowRecordDO;
import com.xs.booksystem.pojo.DTO.BookDTO;
import com.xs.booksystem.pojo.DTO.BorrowRecordDTO;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;
import com.xs.booksystem.service.BookService;
import com.xs.booksystem.service.BorrowRecordService;
import com.xs.booksystem.mapper.BorrowRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 敛
* @description 针对表【borrow_record(借阅记录表)】的数据库操作Service实现
* @createDate 2025-12-16 13:41:50
*/
@Service
public class BorrowRecordServiceImpl implements BorrowRecordService{
    private static final Logger logger = LoggerFactory.getLogger(BorrowRecordServiceImpl.class);
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private BookService bookService;
    @Override
    public List<BorrowRecordVO> queryBorrowRecords(Integer userId) {
        if (BaseContext.getCurrentRole() == "ADMIN"){
            logger.info("查询所有借阅记录");
            List<BorrowRecordDTO> borrowRecordDTOList = borrowRecordMapper.selectList(null);
            List<BorrowRecordVO> borrowRecordVOList = borrowRecordDTOList.stream().map(dto -> {
                BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(dto.getBookId());
                bookDTO = bookService.getBookById(bookDTO);
                BeanUtils.copyProperties(dto, borrowRecordVO);
                borrowRecordVO.setBookName(bookDTO.getTitle());
                return borrowRecordVO;
            }).collect(Collectors.toList());
            return borrowRecordVOList;
        }
        else {
            logger.info("查询用户ID为 {} 的借阅记录", userId);
            LambdaQueryWrapper<BorrowRecordDTO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BorrowRecordDTO::getUserId,userId);
            List<BorrowRecordDTO> borrowRecordDTOList = borrowRecordMapper.selectList(queryWrapper);
            List<BorrowRecordVO> borrowRecordVOList = borrowRecordDTOList.stream().map(dto -> {
                BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(dto.getBookId());
                bookDTO = bookService.getBookById(bookDTO);
                BeanUtils.copyProperties(dto, borrowRecordVO);
                borrowRecordVO.setBookName(bookDTO.getTitle());
                return borrowRecordVO;
            }).collect(Collectors.toList());
            logger.info("查询到 {} 条借阅记录", borrowRecordVOList.size());
            return borrowRecordVOList;
        }
    }

    @Transactional
    @Override
    public Integer deleteBorrowRecords(List<BorrowRecordDO> borrowRecords) {
        logger.info("删除借阅记录，记录数: {}", borrowRecords != null ? borrowRecords.size() : 0);
        if (borrowRecords != null) {
            Set<Integer> ids = borrowRecords.stream().map(BorrowRecordDO::getId).collect(Collectors.toSet());
            Integer result = borrowRecordMapper.deleteBatchIds(ids);
            logger.info("成功删除 {} 条借阅记录", result);
            return borrowRecordMapper.deleteBatchIds(ids);
        }
        logger.warn("删除借阅记录参数为空");
        return 0;
    }

    @Transactional
    @Override
    public BorrowRecordVO addBorrowRecord(BorrowRecordDO borrowRecordDO) {
        logger.info("添加借阅记录: 用户ID={}, 图书ID={}", borrowRecordDO.getUserId(), borrowRecordDO.getBookId());
        BorrowRecordDTO borrowRecordDTO = new BorrowRecordDTO();
        BeanUtils.copyProperties(borrowRecordDO, borrowRecordDTO);
        borrowRecordMapper.insert(borrowRecordDTO);
        logger.info("借阅记录更新成功");
        BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
        BeanUtils.copyProperties(borrowRecordDTO, borrowRecordVO);
        return borrowRecordVO;
    }

    @Transactional
    @Override
    public BorrowRecordVO updateBorrowRecord(BorrowRecordDO borrowRecordDO) {
        logger.info("更新借阅记录: 记录ID={}", borrowRecordDO.getId());
        BorrowRecordDTO borrowRecordDTO = new BorrowRecordDTO();
        BeanUtils.copyProperties(borrowRecordDO, borrowRecordDTO);
        borrowRecordMapper.updateById(borrowRecordDTO);
        logger.info("借阅记录更新成功");
        BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
        BeanUtils.copyProperties(borrowRecordDTO, borrowRecordVO);
        return borrowRecordVO;
    }
}
