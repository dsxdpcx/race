package com.ashuo.scms.service;

import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Borrow;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BorrowService extends IService<Borrow> {
//    public boolean returnEquipment(Borrow borrow) throws Exception;

    IPage<Borrow> getBorrowByCondition(Page<Borrow> page, Borrow borrow);

    List selectByUserId(Integer userId);
}
