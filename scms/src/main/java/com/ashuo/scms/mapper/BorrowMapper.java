package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Borrow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface BorrowMapper extends BaseMapper<Borrow> {

    IPage<Borrow> queryBorrowByBorrowCondition(Page<Borrow> page, Borrow borrow);

}
