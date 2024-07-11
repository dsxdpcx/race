package com.ashuo.scms.service;

import com.ashuo.scms.entity.Football;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


public interface FootballService {
    IPage<Football> getAllFootball(Page<Football> page, Football football);

    Football getFootballByCondition(Football football);

    int addFootball(Football football);

    int modifyFootball(Football football);

    int removeFootball(int schedule_id);
}
