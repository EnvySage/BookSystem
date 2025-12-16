package com.xs.booksystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xs.booksystem.pojo.DO.BorrowRecordDO;
import com.xs.booksystem.pojo.DTO.BorrowRecordDTO;
import com.xs.booksystem.pojo.VO.BorrowRecordVO;
import com.xs.booksystem.service.BorrowRecordService;
import com.xs.booksystem.mapper.BorrowRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Override
    public List<BorrowRecordVO> queryBorrowRecords(Integer userId) {
        LambdaQueryWrapper<BorrowRecordDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowRecordDTO::getUserId,userId);
        List<BorrowRecordDTO> borrowRecordDTOList = borrowRecordMapper.selectList(queryWrapper);
        List<BorrowRecordVO> borrowRecordVOList = borrowRecordDTOList.stream().map(dto -> {
            BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
            BeanUtils.copyProperties(dto, borrowRecordVO);
            return borrowRecordVO;
        }).collect(Collectors.toList());
        return borrowRecordVOList;
    }

    @Override
    public Integer deleteBorrowRecords(List<BorrowRecordDO> borrowRecords) {
        if (borrowRecords != null) {
            Set<Integer> ids = borrowRecords.stream().map(BorrowRecordDO::getId).collect(Collectors.toSet());
            return borrowRecordMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public BorrowRecordVO addBorrowRecord(BorrowRecordDO borrowRecordDO) {
        BorrowRecordDTO borrowRecordDTO = new BorrowRecordDTO();
        BeanUtils.copyProperties(borrowRecordDO, borrowRecordDTO);
        borrowRecordMapper.insert(borrowRecordDTO);
        BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
        BeanUtils.copyProperties(borrowRecordDTO, borrowRecordVO);
        return borrowRecordVO;
    }

    @Override
    public BorrowRecordVO updateBorrowRecord(BorrowRecordDO borrowRecordDO) {
        BorrowRecordDTO borrowRecordDTO = new BorrowRecordDTO();
        BeanUtils.copyProperties(borrowRecordDO, borrowRecordDTO);
        borrowRecordMapper.updateById(borrowRecordDTO);
        BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
        BeanUtils.copyProperties(borrowRecordDTO, borrowRecordVO);
        return borrowRecordVO;
    }
}
