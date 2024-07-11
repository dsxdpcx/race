package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.Football;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface FootballMapper {
    //增加football
    int insertFootball(Football football);

    //修改football
    int updateFootball(Football football);

    //删除football
    int deleteFootball(int schedule_id);

    //按条件查询football
    IPage<Football> queryByFootballName(Page<Football> page, @Param("football") Football football);


    Football queryByFootballCondition(@Param("football") Football football);

}
