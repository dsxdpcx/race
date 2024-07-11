package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.Borrow;
import com.ashuo.scms.entity.Result;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ResultMapper extends BaseMapper<Result> {

   List<Result> findAll();

}
