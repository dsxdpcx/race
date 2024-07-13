package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Cheer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheerMapper extends BaseMapper<Cheer> {

   /* int updateShenhe(Cheer cheer) {
    };
    IPage<Cheer> queryCheerByCheerCondition(Page<Cheer> page, Cheer cheer);*/
}
